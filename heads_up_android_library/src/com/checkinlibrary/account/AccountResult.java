package com.checkinlibrary.account;



import java.util.List;

import com.checkinlibrary.account.LoginResult.MB_User;
import com.checkinlibrary.orgs.CauseResult;

public interface AccountResult {
    public Double getCheckinAmount();
    public void setCheckinAmount(Double checkinAmount);
    public Boolean getLogin();
    public void setLogin(Boolean login);
    public String getApiKey();
    public void setApiKey(String apiKey);
    public String getMessage();
    public void setMessage(String message);
    public List<CauseResult> getOrganizations();
    public int getCheckin_count();
	public void setCheckin_count(int checkin_count);
	public boolean isIs_user_exist();
	public MB_User getMobile_user();
	public void setMobile_user(MB_User mobile_user);
}
