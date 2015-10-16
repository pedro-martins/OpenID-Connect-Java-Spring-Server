/*******************************************************************************
 * Copyright 2015 The MITRE Corporation
 *   and the MIT Kerberos and Internet Trust Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
/**
 * 
 */
package org.mitre.oauth2.web;

import java.lang.reflect.Field;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mitre.oauth2.model.ClientDetailsEntity;
import org.mitre.oauth2.model.SystemScope;
import org.mitre.oauth2.service.ClientDetailsEntityService;
import org.mitre.oauth2.service.SystemScopeService;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.service.ScopeClaimTranslationService;
import org.mitre.openid.connect.service.StatsService;
import org.mitre.openid.connect.service.UserInfoService;
import org.mitre.openid.connect.view.HttpCodeView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.ufsc.lrg.openid.connect.OpenIdConnectJsonSerializer;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;

import static org.mitre.openid.connect.request.ConnectRequestParameters.CSRF;
import static org.mitre.openid.connect.request.ConnectRequestParameters.PROMPT;
import static org.mitre.openid.connect.request.ConnectRequestParameters.PROMPT_CONSENT;
import static org.mitre.openid.connect.request.ConnectRequestParameters.PROMPT_NONE;
import static org.mitre.openid.connect.request.ConnectRequestParameters.PROMPT_SEPARATOR;

/**
 * @author jricher
 *
 */
@Controller
@SessionAttributes("authorizationRequest")
public class OAuthConfirmationController {


	private OpenIdConnectJsonSerializer openIdConnectJson;
	@Autowired
	private ClientDetailsEntityService clientService;

	@Autowired
	private SystemScopeService scopeService;

	@Autowired
	private ScopeClaimTranslationService scopeClaimTranslationService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private StatsService statsService;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(OAuthConfirmationController.class);

	public OAuthConfirmationController() {

	}

	public OAuthConfirmationController(ClientDetailsEntityService clientService) {
		this.clientService = clientService;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping("/oauth/confirm_access")
	public String confimAccess(Map<String, Object> model, @ModelAttribute("authorizationRequest") AuthorizationRequest authRequest,
			Principal p) {

		// Check the "prompt" parameter to see if we need to do special processing

		String prompt = (String)authRequest.getExtensions().get(PROMPT);
		List<String> prompts = Splitter.on(PROMPT_SEPARATOR).splitToList(Strings.nullToEmpty(prompt));
		if (prompts.contains(PROMPT_NONE)) {
			// we're not supposed to prompt, so "return an error"
			logger.info("Client requested no prompt, returning 403 from confirmation endpoint");
			model.put(HttpCodeView.CODE, HttpStatus.FORBIDDEN);
			return HttpCodeView.VIEWNAME;
		}

		if (prompts.contains(PROMPT_CONSENT)) {
			model.put(PROMPT_CONSENT, true);
		}

		//AuthorizationRequest clientAuth = (AuthorizationRequest) model.remove("authorizationRequest");

		ClientDetailsEntity client = null;

		try {
			client = clientService.loadClientByClientId(authRequest.getClientId());
		} catch (OAuth2Exception e) {
			logger.error("confirmAccess: OAuth2Exception was thrown when attempting to load client", e);
			model.put(HttpCodeView.CODE, HttpStatus.BAD_REQUEST);
			return HttpCodeView.VIEWNAME;
		} catch (IllegalArgumentException e) {
			logger.error("confirmAccess: IllegalArgumentException was thrown when attempting to load client", e);
			model.put(HttpCodeView.CODE, HttpStatus.BAD_REQUEST);
			return HttpCodeView.VIEWNAME;
		}

		if (client == null) {
			logger.error("confirmAccess: could not find client " + authRequest.getClientId());
			model.put(HttpCodeView.CODE, HttpStatus.NOT_FOUND);
			return HttpCodeView.VIEWNAME;
		}

		model.put("auth_request", authRequest);
		model.put("client", client);

		String redirect_uri = authRequest.getRedirectUri();

		model.put("redirect_uri", redirect_uri);


		// pre-process the scopes
		Set<SystemScope> scopes = scopeService.fromStrings(authRequest.getScope());

		Set<SystemScope> sortedScopes = new LinkedHashSet<SystemScope>(scopes.size());
		Set<SystemScope> systemScopes = scopeService.getAll();

		// sort scopes for display based on the inherent order of system scopes
		for (SystemScope s : systemScopes) {
			if (scopes.contains(s)) {
				sortedScopes.add(s);
			}
		}

		// add in any scopes that aren't system scopes to the end of the list
		sortedScopes.addAll(Sets.difference(scopes, systemScopes));

		model.put("scopes", sortedScopes);

		// get the userinfo claims for each scope
		UserInfo user = userInfoService.getByUsername(p.getName());
		Map<String, Map<String, String>> claimsForScopes = new HashMap<String, Map<String, String>>();
		if (user != null) {
			
			Field[] fields = user.getClass().getFields();

			for (SystemScope systemScope : sortedScopes) {
				Map<String, String> claimValues = new HashMap<String, String>();

				Set<String> claims = scopeClaimTranslationService.getClaimsForScope(systemScope.getValue());
				for (String claim : claims) {
					Field field = getFieldByClaim(fields, claim);
					if (field !=null && isFieldPrimitive(field)) {
						// TODO: this skips the address claim						
						Object object = objectFromField(user, field);
						if(object != null)
						claimValues.put(claim,object.toString() );
					}
				}

				claimsForScopes.put(systemScope.getValue(), claimValues);
			}
		}

		model.put("claims", claimsForScopes);

		// client stats
		Integer count = statsService.getCountForClientId(client.getId());
		model.put("count", count);


		// contacts
		if (client.getContacts() != null) {
			String contacts = Joiner.on(", ").join(client.getContacts());
			model.put("contacts", contacts);
		}

		// if the client is over a week old and has more than one registration, don't give such a big warning
		// instead, tag as "Generally Recognized As Safe" (gras)
		Date lastWeek = new Date(System.currentTimeMillis() - (60 * 60 * 24 * 7 * 1000));
		if (count > 1 && client.getCreatedAt() != null && client.getCreatedAt().before(lastWeek)) {
			model.put("gras", true);
		} else {
			model.put("gras", false);
		}

		// inject a random value for CSRF purposes
		model.put("csrf", authRequest.getExtensions().get(CSRF));

		return "approve";
	}

	private Object objectFromField(UserInfo user, Field field){
		Object object = null;
		try {
			object = field.get(user);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("Null user field");
		}
		return object;
	}
	private boolean isFieldPrimitive(Field field){
		return field.getClass().isPrimitive() || 
			   String.class.isAssignableFrom(field.getClass()) ||
			   Number.class.isAssignableFrom(field.getClass()) ||
			   Character.class.isAssignableFrom(field.getClass()) ||
			   Boolean.class.isAssignableFrom(field.getClass());
	}
	private Field getFieldByClaim(Field[] fields,String claim){
		for(int i = 0; i < fields.length; i++){
			if(fields[i].getName().equals(claim)){
				return fields[i];
			}
		}return null;
	}

	/**
	 * @return the clientService
	 */
	public ClientDetailsEntityService getClientService() {
		return clientService;
	}

	/**
	 * @param clientService the clientService to set
	 */
	public void setClientService(ClientDetailsEntityService clientService) {
		this.clientService = clientService;
	}


}
