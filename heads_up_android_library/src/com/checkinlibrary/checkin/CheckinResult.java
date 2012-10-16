package com.checkinlibrary.checkin;

public class CheckinResult {

	private Boolean success;
	private Double checkin_amount;
	private String error_msg;
	private int checkin_count;

	public int getCheckin_count() {
		return checkin_count;
	}

	public void setCheckin_count(int checkin_count) {
		this.checkin_count = checkin_count;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Double getCheckin_amount() {
		return checkin_amount;
	}

	public void setCheckin_amount(Double checkin_amount) {
		this.checkin_amount = checkin_amount;
	}

	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}
}
