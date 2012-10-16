package com.checkinlibrary.settings;



import java.util.HashMap;

import com.google.myjson.Gson;

public class SocialSharingResult {

	public FacebookSocialNet facebookObject;
	public TwitterSocialNet twitterObject;

	public SocialSharingResult(String facebookString,String twitterString){	
		facebookObject = new Gson().fromJson(facebookString,  FacebookSocialNet.class);
		twitterObject = new Gson().fromJson(twitterString,  TwitterSocialNet.class);
	}

	public static class FacebookSocialNet {
		Boolean exist;
		String access_secret;
		String access_token;
		Boolean active;
		
		public Boolean getExist() {
			return exist;
		}

		public void setExist(Boolean exist) {
			this.exist = exist;
		}

		public String getAccess_secret() {
			return access_secret;
		}

		public void setAccess_secret(String access_secret) {
			this.access_secret = access_secret;
		}

		public String getAccess_token() {
			return access_token;
		}

		public void setAccess_token(String access_token) {
			this.access_token = access_token;
		}

		public Boolean getActive() {
			return active;
		}

		public void setActive(Boolean active) {
			this.active = active;
		}

		public FacebookSocialNet(HashMap<String, String> facebookObject){
        this.exist=Boolean.valueOf(facebookObject.get("exist"));
        this.active=Boolean.valueOf(facebookObject.get("active"));
        this.access_secret=facebookObject.get("access_secret");
        this.access_token=facebookObject.get("access_token");
		}
	}

	public static class TwitterSocialNet {
		Boolean exist;	
		String access_secret;
		String access_token;
		Boolean active;
		
		public Boolean getExist() {
			return exist;
		}

		public void setExist(Boolean exist) {
			this.exist = exist;
		}

		public String getAccess_secret() {
			return access_secret;
		}

		public void setAccess_secret(String access_secret) {
			this.access_secret = access_secret;
		}

		public String getAccess_token() {
			return access_token;
		}

		public void setAccess_token(String access_token) {
			this.access_token = access_token;
		}

		public Boolean getActive() {
			return active;
		}

		public void setActive(Boolean active) {
			this.active = active;
		}

		public TwitterSocialNet(HashMap<String, String> twitterObject){
			this.exist=Boolean.valueOf(twitterObject.get("exist"));
	        this.active=Boolean.valueOf(twitterObject.get("active"));
	        this.access_secret=twitterObject.get("access_secret");
	        this.access_token=twitterObject.get("access_token");
		}
	}
}
