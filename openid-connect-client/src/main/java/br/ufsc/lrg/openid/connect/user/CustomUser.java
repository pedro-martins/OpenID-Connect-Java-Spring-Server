package br.ufsc.lrg.openid.connect.user;

import org.mitre.openid.connect.model.Address;
import org.mitre.openid.connect.model.UserInfo;

import br.com.srs.gsonld.SemanticClass;
import br.com.srs.gsonld.SemanticProperty;

import com.google.gson.JsonObject;
@SemanticClass("http://schema.org/Person")
public class CustomUser implements UserInfo {

@SemanticProperty("http://schema.org/name")
	private String nome;
@SemanticProperty("http://schema.org/alternateName")
	private String otroNome;
@SemanticProperty("http://schema.org/email")
	private String endereco;

	
	@Override
	public String getSub() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSub(String sub) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPreferredUsername() {
		// TODO Auto-generated method stub
		return otroNome;
	}

	@Override
	public void setPreferredUsername(String preferredUsername) {
		// TODO Auto-generated method stub
		otroNome = preferredUsername;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return nome;
	}

	@Override
	public void setName(String name) {
		nome = name;
		
	}

	@Override
	public String getGivenName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGivenName(String givenName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFamilyName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFamilyName(String familyName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMiddleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMiddleName(String middleName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getNickname() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNickname(String nickname) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getProfile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProfile(String profile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPicture() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPicture(String picture) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getWebsite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWebsite(String website) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getEmail() {
		// TODO Auto-generated method stub
		return endereco;
	}

	@Override
	public void setEmail(String email) {
		// TODO Auto-generated method stub
		endereco = email;
	}

	@Override
	public Boolean getEmailVerified() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEmailVerified(Boolean emailVerified) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getGender() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGender(String gender) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getZoneinfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setZoneinfo(String zoneinfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLocale(String locale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPhoneNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPhoneNumber(String phoneNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean getPhoneNumberVerified() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPhoneNumberVerified(Boolean phoneNumberVerified) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Address getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAddress(Address address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUpdatedTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUpdatedTime(String updatedTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getBirthdate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBirthdate(String birthdate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonObject toJson() {
		JsonObject obj = new JsonObject();

		obj.addProperty("sub", this.getSub());

		obj.addProperty("name", this.getName());
		obj.addProperty("preferred_username", this.getPreferredUsername());
		obj.addProperty("given_name", this.getGivenName());
		obj.addProperty("family_name", this.getFamilyName());
		obj.addProperty("middle_name", this.getMiddleName());
		obj.addProperty("nickname", this.getNickname());
		obj.addProperty("profile", this.getProfile());
		obj.addProperty("picture", this.getPicture());
		obj.addProperty("website", this.getWebsite());
		obj.addProperty("gender", this.getGender());
		obj.addProperty("zone_info", this.getZoneinfo());
		obj.addProperty("locale", this.getLocale());
		obj.addProperty("updated_time", this.getUpdatedTime());
		obj.addProperty("birthdate", this.getBirthdate());

		obj.addProperty("email", this.getEmail());
		obj.addProperty("email_verified", this.getEmailVerified());

		obj.addProperty("phone_number", this.getPhoneNumber());
		obj.addProperty("phone_number_verified", this.getPhoneNumberVerified());

		if (this.getAddress() != null) {

			JsonObject addr = new JsonObject();
			addr.addProperty("formatted", this.getAddress().getFormatted());
			addr.addProperty("street_address", this.getAddress().getStreetAddress());
			addr.addProperty("locality", this.getAddress().getLocality());
			addr.addProperty("region", this.getAddress().getRegion());
			addr.addProperty("postal_code", this.getAddress().getPostalCode());
			addr.addProperty("country", this.getAddress().getCountry());

			obj.add("address", addr);
		}

		return obj;
	}

}
