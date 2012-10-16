package com.campaignslibrary.models;

public class LaunchCampaignResult {
	Boolean success;
	String public_link;
	boolean is_active;
	int campaign_id;
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getPublic_link() {
		return public_link;
	}
	public void setPublic_link(String public_link) {
		this.public_link = public_link;
	}
	public boolean isIs_active() {
		return is_active;
	}
	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}
	public int getCampaign_id() {
		return campaign_id;
	}
	public void setCampaign_id(int campaign_id) {
		this.campaign_id = campaign_id;
	}
}
