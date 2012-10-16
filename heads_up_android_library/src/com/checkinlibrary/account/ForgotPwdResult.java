package com.checkinlibrary.account;

public class ForgotPwdResult {

	private String message;
	private Boolean is_sent;

	public Boolean getStatus() {
		return is_sent;
	}

	public void setStatus(Boolean status) {
		this.is_sent = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
