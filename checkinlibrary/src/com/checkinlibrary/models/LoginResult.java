package com.checkinlibrary.models;

import java.util.List;



public class LoginResult implements AccountResult {
    private Boolean login;
    private String api_key;
    private String message;
    private Double checkin_amount;
    private List<OrganizationResult> organizations;
    private int checkin_count;
    private MB_User mobile_user;
    private VideoResult video_links;

    public VideoResult getVideo_links() {
		return video_links;
	}

	public void setVideo_links(VideoResult video_links) {
		this.video_links = video_links;
	}

	public MB_User getMobile_user() {
		return mobile_user;
	}

	public void setMobile_user(MB_User mobile_user) {
		this.mobile_user = mobile_user;
	}

	public class MB_User{
    	private MobileUser mobile_user;
    	public MobileUser getmUserPref() {
    		return mobile_user;
    	}

    	public void setmUserPref(MobileUser mUserPref) {
    		this.mobile_user = mUserPref;
    	}
    }

	public class MobileUser{
    	private String email;
    	private String first_name;
    	public String getFirst_name() {
			return first_name;
		}
		public void setFirst_name(String first_name) {
			this.first_name = first_name;
		}
		public String getLast_name() {
			return last_name;
		}
		public void setLast_name(String last_name) {
			this.last_name = last_name;
		}
		private String last_name;
    	
    	public String getEmail() {
    		return email;
    	}
    	public void setEmail(String email) {
    		this.email = email;
    	}
    }

	public int getCheckin_count() {
		return checkin_count;
	}

	public void setCheckin_count(int checkin_count) {
		this.checkin_count = checkin_count;
	}

	public Double getCheckinAmount() {
        return checkin_amount;
    }

    public void setCheckinAmount(Double checkinAmount) {
        checkin_amount = checkinAmount;
    }

    public Boolean getLogin() {
        return login;
    }

    public void setLogin(Boolean login) {
        this.login = login;
    }

    public String getApiKey() {
        return api_key;
    }

    public void setApiKey(String api_key) {
        this.api_key = api_key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<OrganizationResult> getOrganizations() {
        return this.organizations;
    }
}
