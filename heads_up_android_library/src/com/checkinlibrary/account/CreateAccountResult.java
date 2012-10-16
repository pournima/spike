package com.checkinlibrary.account;

import java.util.List;

import com.checkinlibrary.account.LoginResult.MB_User;
import com.checkinlibrary.orgs.CauseResult;


public class CreateAccountResult implements AccountResult {

	private Boolean login;
	private String api_key;
	private String message;
	private Double checkin_amount;

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

	public void setApiKey(String apiKey) {
		api_key = apiKey;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/*@Override
	public List<CauseResult> getOrganizations() {
		return null;
	}*/

	@Override
	public int getCheckin_count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCheckin_count(int checkin_count) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<CauseResult> getOrganizations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isIs_user_exist() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MB_User getMobile_user() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMobile_user(MB_User mobile_user) {
		// TODO Auto-generated method stub
		
	}
}