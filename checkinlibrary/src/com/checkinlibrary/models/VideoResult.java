package com.checkinlibrary.models;

public class VideoResult {
	
	videoClass app_user_link;
	videoClass grow_business_link;
	videoClass overview_link;
	
	public videoClass getApp_user_link() {
		return app_user_link;
	}

	public void setApp_user_link(videoClass app_user_link) {
		this.app_user_link = app_user_link;
	}

	public videoClass getGrow_business_link() {
		return grow_business_link;
	}

	public void setGrow_business_link(videoClass grow_business_link) {
		this.grow_business_link = grow_business_link;
	}

	public videoClass getOverview_link() {
		return overview_link;
	}

	public void setOverview_link(videoClass overview_link) {
		this.overview_link = overview_link;
	}
	
	public class videoClass{
		private String link;
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
		public String getCover() {
			return cover;
		}
		public void setCover(String cover) {
			this.cover = cover;
		}
		private String cover;
	}
}
