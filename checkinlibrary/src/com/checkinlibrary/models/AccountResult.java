package com.checkinlibrary.models;

import java.util.List;

import com.checkinlibrary.models.LoginResult.MB_User;


public interface AccountResult {
	public Double getCheckinAmount();
	public void setCheckinAmount(Double checkinAmount);
	public Boolean getLogin();
	public void setLogin(Boolean login);
	public String getApiKey();
	public void setApiKey(String apiKey);
	public String getMessage();
	public void setMessage(String message);
	public List<OrganizationResult> getOrganizations();
	public int getCheckin_count();
	public void setCheckin_count(int checkin_count);
	public MB_User getMobile_user();
	public void setMobile_user(MB_User mobile_user);
	public VideoResult getVideo_links();
	public void setVideo_links(VideoResult video_links);
}
