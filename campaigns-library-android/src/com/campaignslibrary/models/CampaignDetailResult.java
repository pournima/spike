package com.campaignslibrary.models;

import java.util.List;

public class CampaignDetailResult {

	private Boolean success;
	private List<CreateCampaignResult> featured_campaigns;
	private List<CreateCampaignResult> current_campaigns;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public List<CreateCampaignResult> getFeatured_campaigns() {
		return featured_campaigns;
	}

	public void setFeatured_campaigns(List<CreateCampaignResult> featured_campaigns) {
		this.featured_campaigns = featured_campaigns;
	}

	public List<CreateCampaignResult> getCurrent_campaigns() {
		return current_campaigns;
	}

	public void setCurrent_campaigns(List<CreateCampaignResult> current_campaigns) {
		this.current_campaigns = current_campaigns;
	}
}
