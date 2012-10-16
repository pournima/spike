package com.checkinlibrary.models;

public class CauseProfileResult {
	
	private String title;
	private String users_count;
	private String businesses_count;
	private String image_url;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	private String description;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public CauseProfileResult() {
		
	}
	public String getUsers_count() {
		return users_count;
	}
	public void setUsers_count(String users_count) {
		this.users_count = users_count;
	}
	public String getBusinesses_count() {
		return businesses_count;
	}
	public void setBusinesses_count(String businesses_count) {
		this.businesses_count = businesses_count;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
}
