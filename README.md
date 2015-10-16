# MITREid Connect with Semantic
---

We enabled the use of semantics in the attributes disseminated by the Identity provider of OpenId Connect federations.
 
In order to achieve that, we created the class  “br.ufsc.lrg.openid.connect.OpenIdConnectJson”. This class has two properties: 
* Gson  - if you choose the Gson property, your application will run without adding semantic values to the JSON that represents users’ attributes,
* GsonLD -  if you choose GsonLD, your application will run with semantic. That will add semantic that are embedded in the implementation of UserInfo that you should create.
 
An example of how your class can be created to using semantics can be found in br.ufsc.lrg.openid.connect.user.CustomUser .
 
To set the property in the br.ufsc.lrg.openid.connect.OpenIdConnectJson, you will need configure the application-context.xml as the example below: 
 
<bean id="jsonld" class="br.com.srs.gsonld.GsonLD"></bean>
<bean class="br.ufsc.lrg.openid.connect.OpenIdConnectJson">
<property name="gsonLD" ref="jsonld"/>
</bean>
 
The ontologies supported by the IdP can be found in http://idpaddress/idppath/.well-known/openid-configuration/ ,you just need to search by "semantic_databases".
 
To add the ontologies supported by IdP in the /.well-known/openid-configuration endpoint response, we created the method getSemanticValues() at class org.mitre.openid.connect.model.DefaultUserInfo. This method basically search classes that implement UserInfo and look for their semantic annotation.
