package com.checkinlibrary.models;

import java.io.Serializable;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.db.OrganizationDbAdapter;


public class OrganizationResult implements Serializable{
	private Organization organization;


	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public OrganizationResult(cause_profile_result cause_profile,int support_count,String name,
			int users_count,int id,Boolean supported){
		organization=new Organization(cause_profile,support_count,name,
				users_count,id,supported);
	}
	
	public void setSupported(Boolean isSupported, CheckinLibraryActivity context) {
        organization.setSupported(isSupported);
        //Update our cache if we have it
        OrganizationDbAdapter adapter = new OrganizationDbAdapter(context);
        adapter.updateSupported(isSupported,organization.getId());
    }

	public static class Organization implements Serializable{
		cause_profile_result cause_profile;
		int support_count;
		String name;
		int users_count;
		int id;
		Boolean supported;

		public cause_profile_result getCause_profile() {
			return cause_profile;
		}

		public void setCause_profile(cause_profile_result cause_profile) {
			this.cause_profile = cause_profile;
		}

		public int getSupport_count() {
			return support_count;
		}

		public void setSupport_count(int support_count) {
			this.support_count = support_count;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getUsers_count() {
			return users_count;
		}

		public void setUsers_count(int users_count) {
			this.users_count = users_count;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public Boolean getSupported() {
			return supported;
		}

		public void setSupported(Boolean supported) {
			this.supported = supported;
		}


		public Organization(cause_profile_result cause_profile,int support_count,String name,
				int users_count,int id,Boolean supported){
			this.cause_profile=cause_profile;
			this.support_count=support_count;
			this.name=name;
			this.users_count=users_count;
			this.id=id;
			this.supported=supported;   
		}
	}

	public static class cause_profile_result implements Serializable{
		String title;
		String about_us;
		String image_link;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getAbout_us() {
			return about_us;
		}

		public void setAbout_us(String about_us) {
			this.about_us = about_us;
		}

		public String getImage_link() {
			return image_link;
		}

		public void setImage_link(String image_link) {
			this.image_link = image_link;
		}

		public cause_profile_result(String title,String about_us,String image_link){
			this.title=title;
			this.about_us=about_us;
			this.image_link=image_link;
		}
		
		public cause_profile_result(){
		}
	}
}