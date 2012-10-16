package com.headsup.safety;

import java.util.ArrayList;

public class ParentsTabItems {
	
	int imgShowHideVideo;
	String txtParentsTabnHeading;
	String txtParentsTabContent;
	String txtVideoLabel;
	String txtParentsTabDesc;
	ArrayList<VideoData> tableVideos;
	
	int iVideosCount;
	
	
	int fImgShowHideVideo;
	int fTxtParentsTabnHeading;
	int fTxtParentsTabContent;
	int fTxtVideoLabel;
	int fTxtParentsTabDesc;
	int fTableVideos;
	
	public ArrayList<VideoData> getTableVideos() {
		return tableVideos;
	}


	public void setTableVideos(ArrayList<VideoData> tableVideos) {
		this.tableVideos = tableVideos;
	}

	
	public int getiVideosCount() {
		return iVideosCount;
	}


	public void setiVideosCount(int iVideosCount) {
		this.iVideosCount = iVideosCount;
	}
	
	public ParentsTabItems() {
		// TODO Auto-generated constructor stub
		txtParentsTabnHeading = "";
		txtParentsTabContent = "";
		txtVideoLabel = "";
		txtParentsTabDesc = "";
	}
	public int getImgShowHideVideo() {
		return imgShowHideVideo;
	}
	public void setImgShowHideVideo(int imgShowHideVideo) {
		this.imgShowHideVideo = imgShowHideVideo;
	}
	public String getTxtParentsTabnHeading() {
		return txtParentsTabnHeading;
	}
	public void setTxtParentsTabnHeading(String txtParentsTabnHeading) {
		this.txtParentsTabnHeading = txtParentsTabnHeading;
	}
	public String getTxtParentsTabContent() {
		return txtParentsTabContent;
	}
	public void setTxtParentsTabContent(String txtParentsTabContent) {
		this.txtParentsTabContent = txtParentsTabContent;
	}
	public String getTxtVideoLabel() {
		return txtVideoLabel;
	}
	public void setTxtVideoLabel(String txtVideoLabel) {
		this.txtVideoLabel = txtVideoLabel;
	}
	public String getTxtParentsTabDesc() {
		return txtParentsTabDesc;
	}
	public void setTxtParentsTabDesc(String txtParentsTabDesc) {
		this.txtParentsTabDesc = txtParentsTabDesc;
	}
	
	public int getfImgShowHideVideo() {
		return fImgShowHideVideo;
	}
	public void setfImgShowHideVideo(int fImgShowHideVideo) {
		this.fImgShowHideVideo = fImgShowHideVideo;
	}
	public int getfTxtParentsTabnHeading() {
		return fTxtParentsTabnHeading;
	}
	public void setfTxtParentsTabnHeading(int fTxtParentsTabnHeading) {
		this.fTxtParentsTabnHeading = fTxtParentsTabnHeading;
	}
	public int getfTxtParentsTabContent() {
		return fTxtParentsTabContent;
	}
	public void setfTxtParentsTabContent(int fTxtParentsTabContent) {
		this.fTxtParentsTabContent = fTxtParentsTabContent;
	}
	public int getfTxtVideoLabel() {
		return fTxtVideoLabel;
	}
	public void setfTxtVideoLabel(int fTxtVideoLabel) {
		this.fTxtVideoLabel = fTxtVideoLabel;
	}
	public int getfTxtParentsTabDesc() {
		return fTxtParentsTabDesc;
	}
	public void setfTxtParentsTabDesc(int fTxtParentsTabDesc) {
		this.fTxtParentsTabDesc = fTxtParentsTabDesc;
	}
	public int getfTableVideos() {
		return fTableVideos;
	}
	public void setfTableVideos(int fTableVideos) {
		this.fTableVideos = fTableVideos;
	}
	
	
	 class VideoData{
		int iVideoCover;
		String strVideoLink;
		
		public VideoData() {
			// TODO Auto-generated constructor stub
		}
		
		public int getiVideoCover() {
			return iVideoCover;
		}
		public void setiVideoCover(int iVideoCover) {
			this.iVideoCover = iVideoCover;
		}
		public String getStrVideoLink() {
			return strVideoLink;
		}
		public void setStrVideoLink(String strVideoLink) {
			this.strVideoLink = strVideoLink;
		}
	}
	

}
