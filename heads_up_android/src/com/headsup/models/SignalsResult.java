package com.headsup.models;

import java.util.HashMap;
import java.util.List;

public class SignalsResult {
	
	Boolean success;
	List<SignalCategory> catagories;
	
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public List<SignalCategory> getCatagories() {
		return catagories;
	}

	public void setCatagories(List<SignalCategory> catagories) {
		this.catagories = catagories;
	}


	public class SignalCategory{
		int catagory_name;
		public String image;
		public String image_thumbnail;
		String text_heading;
		String text_description;
		
		public int getCatagory_name() {
			return catagory_name;
		}

		public void setCatagory_name(int catagory_name) {
			this.catagory_name = catagory_name;
		}

		public SignalCategory(HashMap<String, String> signalParams) {
			// TODO Auto-generated constructor stub
			this.catagory_name = Integer.valueOf(signalParams.get("catagory"));
			this.image_thumbnail = signalParams.get("image_link_small");
			this.image = signalParams.get("image_link_large");
			this.text_heading = signalParams.get("text_heading");
			this.text_description = signalParams.get("text_desc");
		}
		
		public String getImage_link_large() {
			return image;
		}
		public void setImage_link_large(String image_link_large) {
			this.image = image_link_large;
		}
		public String getImage_link_small() {
			return image_thumbnail;
		}
		public void setImage_link_small(String image_link_small) {
			this.image_thumbnail = image_link_small;
		}
		public String getText_heading() {
			return text_heading;
		}
		public void setText_heading(String text_heading) {
			this.text_heading = text_heading;
		}
		public String getText_desc() {
			return text_description;
		}
		public void setText_desc(String text_desc) {
			this.text_description = text_desc;
		}	
		
	}
	
}
