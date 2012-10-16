package com.headsup.models;

public class Entry {
	public String title;
	public String description;
	public String pub_date;
	public String descriptionLink;
	public String guid;

	

	public Entry(String title, String description, String descriptionLink,
			String pub_date, String guid) {
		this.title = title;
		this.description = description;
		this.pub_date = pub_date;
		this.descriptionLink = descriptionLink;
		this.guid = guid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPub_date() {
		return pub_date;
	}

	public void setPub_date(String pub_date) {
		this.pub_date = pub_date;
	}

	public String getDescriptionLink() {
		return descriptionLink;
	}

	public void setDescriptionLink(String descriptionLink) {
		this.descriptionLink = descriptionLink;
	}

	public Entry() {

	}
	
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

}
