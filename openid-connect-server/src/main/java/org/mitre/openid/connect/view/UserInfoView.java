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
package org.mitre.openid.connect.view;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.ufsc.lrg.openid.connect.OpenIdConnectJson;

import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.service.ScopeClaimTranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.view.AbstractView;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component(UserInfoView.VIEWNAME)
public class UserInfoView extends AbstractView {

	public static final String REQUESTED_CLAIMS = "requestedClaims";
	public static final String AUTHORIZED_CLAIMS = "authorizedClaims";
	public static final String SCOPE = "scope";
	public static final String USER_INFO = "userInfo";

	public static final String VIEWNAME = "userInfoView";
	
	private static JsonParser jsonParser = new JsonParser();
	
	@Autowired
	private OpenIdConnectJson openIdConnectJson;
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(UserInfoView.class);

	@Autowired
	private ScopeClaimTranslationService translator;

	protected Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {

		@Override
		public boolean shouldSkipField(FieldAttributes f) {

			return false;
		}

		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			// skip the JPA binding wrapper
			if (clazz.equals(BeanPropertyBindingResult.class)) {
				return true;
			}
			return false;
		}

	}).create();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel
	 * (java.util.Map, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {

		UserInfo userInfo = (UserInfo) model.get(USER_INFO);

		Set<String> scope = (Set<String>) model.get(SCOPE);

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);


		JsonObject authorizedClaims = null;
		JsonObject requestedClaims = null;
		if (model.get(AUTHORIZED_CLAIMS) != null) {
			authorizedClaims = jsonParser.parse((String) model.get(AUTHORIZED_CLAIMS)).getAsJsonObject();
		}
		if (model.get(REQUESTED_CLAIMS) != null) {
			requestedClaims = jsonParser.parse((String) model.get(REQUESTED_CLAIMS)).getAsJsonObject();
		}
		String json = toJsonFromRequestObj(userInfo, scope, authorizedClaims, requestedClaims);

		writeOut(json,null,response);
	}

	protected void writeOut(String json,Map<String, Object> model,HttpServletResponse response) {
		try {
			Writer out = response.getWriter();
			out.write(json);
		} catch (IOException e) {

			logger.error("IOException in UserInfoView.java: ", e);

		}

	}

	/**
	 * Build a JSON response according to the request object received.
	 * 
	 * Claims requested in requestObj.userinfo.claims are added to any
	 * claims corresponding to requested scopes, if any.
	 * 
	 * @param ui the UserInfo to filter
	 * @param scope the allowed scopes to filter by
	 * @param authorizedClaims the claims authorized by the client or user
	 * @param requestedClaims the claims requested in the RequestObject
	 * @return the filtered JsonObject result
	 */
	private String toJsonFromRequestObj(UserInfo ui, Set<String> scope, JsonObject authorizedClaims, JsonObject requestedClaims) {

		// get the base object
		Set<String> authorizedByClaims = new HashSet<String>();
		Set<String> requestedByClaims = new HashSet<String>();

		if (authorizedClaims != null) {
			JsonObject userinfoAuthorized = authorizedClaims.getAsJsonObject().get("userinfo").getAsJsonObject();
			for (Entry<String, JsonElement> entry : userinfoAuthorized.getAsJsonObject().entrySet()) {
				authorizedByClaims.add(entry.getKey());
			}
		}
		if (requestedClaims != null) {
			JsonObject userinfoRequested = requestedClaims.getAsJsonObject().get("userinfo").getAsJsonObject();
			for (Entry<String, JsonElement> entry : userinfoRequested.getAsJsonObject().entrySet()) {
				requestedByClaims.add(entry.getKey());
			}
		}
		
		Field[] field = ui.getClass().getFields();
		
		for(int i = 0 ; i < field.length; i++){
			if(!requestedByClaims.contains(field[i].getName())){
				setObjectAtributeToNull(ui, field[i]);
			}
		}

		return openIdConnectJson.toJson(ui);
	}

	private void setObjectAtributeToNull(UserInfo ui, Field field) {
		try {
			if(!Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())){						
				field.set(ui, null);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	} 
}
