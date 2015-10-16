package br.ufsc.lrg.openid.connect;

import org.springframework.beans.factory.InitializingBean;

import br.com.srs.gsonld.GsonLD;

import com.google.gson.Gson;


public class OpenIdConnectJson implements InitializingBean{

	private Gson gson;
	private GsonLD gsonLD;
	
	public String toJson(Object o){
		if(gson == null){
			return gsonLD.toJsonLD(o);
		}
		return gson.toJson(o);
	}
	public <T> T fromJson(String json,Class<T> clazz){
		if(gson == null){
			return gsonLD.fromJsonLD(json, clazz);
		}
		return gson.fromJson(json,clazz);
	}
 
	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	public GsonLD getGsonLD() {
		return gsonLD;
	}

	public void setGsonLD(GsonLD gsonLD) {
		this.gsonLD = gsonLD;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(gson == null && gsonLD == null){
			throw new RuntimeException("You need to set at least one OpenIdConnectJson property");
		}
		if(gson != null && gsonLD != null){
			throw new RuntimeException("You can set just --<!*! ONE !*!>-- OpenIdConnectJson property");
		}
		
	}
}
