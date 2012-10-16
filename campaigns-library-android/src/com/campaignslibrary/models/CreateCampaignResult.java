package com.campaignslibrary.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class CreateCampaignResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean success;
	private Campaign campaign;
	private String error;

	public CreateCampaignResult(HashMap<String, String> campgnParams,
			List<CampgnPhotos> mCampgnPhotosList) {
		campaign=new Campaign(campgnParams,mCampgnPhotosList);
	}
	public CreateCampaignResult() {
		
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Campaign getCampaign() {
		return campaign;
	}
	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public class Campaign implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int id;
		String end_date;
		String fb_link;
		String created_at;
		int category_id;
		String campaign_sub_type;
		String start_date;
		Boolean is_active;
		String description;
		String donation_type;
		String video_link;
		String pledge_for;
		String campaign_type;
		String updated_at;
		String name;
		int goal;
		List<CampgnPhotos> photos;
		String public_link;
		Boolean is_featured;
		String supported_by;
		CampgnOrganization organization;		

		public Campaign(HashMap<String, String> campgnParams,
				List<CampgnPhotos> mCampgnPhotosList) {
			this.id=Integer.valueOf(campgnParams.get("remote_id"));
			this.end_date=campgnParams.get("end_date");
			this.fb_link=campgnParams.get("fb_link");
			this.created_at=campgnParams.get("created_at");
			this.category_id=Integer.valueOf(campgnParams.get("category_id"));
			this.campaign_sub_type=campgnParams.get("campaign_sub_type");
			this.start_date=campgnParams.get("start_date");
			this.is_active=(Integer.valueOf(campgnParams.get("is_active"))==1)?true:false;
			this.description=campgnParams.get("description");
			this.donation_type=campgnParams.get("donation_type");
			this.video_link=campgnParams.get("video_link");
			this.pledge_for=campgnParams.get("pledge_for");
			this.campaign_type=campgnParams.get("campaign_type");
			this.updated_at=campgnParams.get("updated_at");
			this.name=campgnParams.get("name");
			this.goal=Integer.valueOf(campgnParams.get("goal"));
		    this.photos=mCampgnPhotosList;
		    this.public_link = campgnParams.get("public_link");
		    this.is_featured = Boolean.valueOf(campgnParams.get("is_featured"));
		    this.supported_by = campgnParams.get("supported_by");
		    this.organization=new CampgnOrganization(campgnParams.get("organization_name"),
		    		Integer.valueOf(campgnParams.get("organization_id")));
		}
		
		public Boolean getIs_featured() {
			return is_featured;
		}

		public void setIs_featured(Boolean is_featured) {
			this.is_featured = is_featured;
		}

		public String getSupported_by() {
			return supported_by;
		}

		public void setSupported_by(String supported_by) {
			this.supported_by = supported_by;
		}

		public CampgnOrganization getOrganization() {
			return organization;
		}

		public void setOrganization(CampgnOrganization organization) {
			this.organization = organization;
		}
		
		public String getPublic_link() {
			return public_link;
		}

		public void setPublic_link(String public_link) {
			this.public_link = public_link;
		}

		public List<CampgnPhotos> getPhotos() {
			return photos;
		}

		public void setPhotos(List<CampgnPhotos> photos) {
			this.photos = photos;
		}

		public String getEnd_date() {
			return end_date;
		}

		public void setEnd_date(String end_date) {
			this.end_date = end_date;
		}

		public String getFb_link() {
			return fb_link;
		}

		public void setFb_link(String fb_link) {
			this.fb_link = fb_link;
		}

		public String getCreated_at() {
			return created_at;
		}

		public void setCreated_at(String created_at) {
			this.created_at = created_at;
		}

		public int getCategory_id() {
			return category_id;
		}

		public void setCategory_id(int category_id) {
			this.category_id = category_id;
		}

		public String getCampaign_sub_type() {
			return campaign_sub_type;
		}

		public void setCampaign_sub_type(String campaign_sub_type) {
			this.campaign_sub_type = campaign_sub_type;
		}

		public String getStart_date() {
			return start_date;
		}

		public void setStart_date(String start_date) {
			this.start_date = start_date;
		}

		public Boolean getIs_active() {
			return is_active;
		}

		public void setIs_active(Boolean is_active) {
			this.is_active = is_active;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getDonation_type() {
			return donation_type;
		}

		public void setDonation_type(String donation_type) {
			this.donation_type = donation_type;
		}

		public String getVideo_link() {
			return video_link;
		}

		public void setVideo_link(String video_link) {
			this.video_link = video_link;
		}

		public String getPledge_for() {
			return pledge_for;
		}

		public void setPledge_for(String pledge_for) {
			this.pledge_for = pledge_for;
		}

		public String getCampaign_type() {
			return campaign_type;
		}

		public void setCampaign_type(String campaign_type) {
			this.campaign_type = campaign_type;
		}

		public String getUpdated_at() {
			return updated_at;
		}

		public void setUpdated_at(String updated_at) {
			this.updated_at = updated_at;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public int getGoal() {
			return goal;
		}

		public void setGoal(int goal) {
			this.goal = goal;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}
	
	public class CampgnPhotos implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		String image_file_name;
		String image_link;
		int id;
		
		public CampgnPhotos(String imgName, String imgLink, int iCampId) {
			this.image_file_name=imgName;
			this.image_link=imgLink;
			this.id=iCampId;
		}

		public String getImage_file_name() {
			return image_file_name;
		}
		public void setImage_file_name(String image_file_name) {
			this.image_file_name = image_file_name;
		}
		public String getImage_link() {
			return image_link;
		}
		public void setImage_link(String image_link) {
			this.image_link = image_link;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}

	public class CampgnOrganization implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		String name;
		int id;

		public CampgnOrganization(String name, int id) {
			this.name = name;
			this.id = id;
		}
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}
}
