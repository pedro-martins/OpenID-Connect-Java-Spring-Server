# MITREid Connect with Semantic
---

We enabled the use of semantics in the attributes disseminated by the Identity provider of OpenId Connect federations.
 
In order to achieve that, we created the class  “br.ufsc.lrg.openid.connect.OpenIdConnectJson”. This class has two properties: 
* Gson  - if you choose the Gson property, your application will run without adding semantic values to the JSON that represents users’ attributes;
* GsonLD -  if you choose GsonLD, your application will run with semantic. That will add semantic that are embedded in the implementation of UserInfo that you should create.
 
An example of how your class can be created to using semantics can be found in br.ufsc.lrg.openid.connect.user.CustomUser .
 
To set the property in the br.ufsc.lrg.openid.connect.OpenIdConnectJson, you will need configure the application-context.xml as the example below: 
 
<bean id="jsonld" class="br.com.srs.gsonld.GsonLD"></bean>
<bean class="br.ufsc.lrg.openid.connect.OpenIdConnectJson">
<property name="gsonLD" ref="jsonld"/>
</bean>
 
The ontologies supported by the IdP can be found in http://idpaddress/idppath/.well-known/openid-configuration/ ,you just need to search by "semantic_databases".
 
To add the ontologies supported by IdP in the /.well-known/openid-configuration endpoint response, we created the method getSemanticValues() at class org.mitre.openid.connect.model.DefaultUserInfo. This method basically search classes that implement UserInfo and look for their semantic annotation.

### Test Applications
---
 
We created two tests applications, one that emulates a SP attribute query (SPemulator) and another that emulates an IdP response (IdPemulator).
 
The IdPemulator application write the Gson/GsonLD serialize time in a file specified by the user.
This application uses two configurations that can be found in the web.xml: 
* pathfile - this property sets the file that the application will write the Gson/GsonLD serialize time;
* dataset  - this property indicates the file that will be read to populate the DataSet of IdP. An example of PII dataset can be in the project (“userdata.txt”).
 
 
Here is an example of the dataset file format:
 
```name;email;city;birthday;phone;bankacc;id\n```
```Hayden;metus.In.nec@interdum.ca;Grimbergen;Jul 16, 2015;068-952-4015;PT24473852634320477629224;5160158472112375\n```
 
The application IdPemulator needs a container to be initialized such as Tomcat. After deployed the application in the tomcat, you just need to start it. Do not forget to configure the application properly in the web.xml.
 
The SPemulator application has two main classes:
* br.ufsc.lrg.tests.JsonPerformanceTest
* br.ufsc.lrg.tests.SemanticJsonPerformanceTest.
 
Those classes, should be executed when the IdP application is running. They emulate a SP query to the IdP to retrieve users PII data. Both classes receive two parameters to run, the first parameter is to define the base url where the IdPemulator is hosted. The second parameter is the path of the file that the application will write the Gson/GsonLD deserialization time, the Json/JsonLD size and the overall runtime.
