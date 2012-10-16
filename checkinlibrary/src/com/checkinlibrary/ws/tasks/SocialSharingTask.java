package com.checkinlibrary.ws.tasks;

import android.os.AsyncTask;

import com.checkinlibrary.account.LoginActivity;
import com.checkinlibrary.facebook.android.FacebookShare;
import com.checkinlibrary.settings.DelinkShareFragment;
import com.checkinlibrary.settings.ShareListAdapter;
import com.checkinlibrary.twitter.PrepareRequestTokenActivity;
import com.checkinlibrary.ws.services.SocialSharingWebService;


public class SocialSharingTask extends AsyncTask<String, Integer, Boolean>{

	private ShareListAdapter Sharecontext;
	private FacebookShare fb_context;
	private PrepareRequestTokenActivity tw_context;
	private LoginActivity loginContext;
	private DelinkShareFragment delinkFragContext;
	
	private int mWebSeviceFlag; //0 =sharepreferences, 1= updatepref , 2=getPref
	private int mIFileFlag; // 0 from ShareListAdapter 1=FacebookShare //2 for twitter file(PrepareRequestTokenActivity) // 3 for loginActivity
	                        // 4 for dlinkFragment
	
	public final int SHARE_LIST_ADAPTER=0;
	public final int FACEBOOK_SHARE=1;
	public final int TWITTER_SHARE=2;
	public final int LOGIN_ACT=3;
	public final int DELINK_FRAG=4;
	
	public static final int POST_PREF   =0;
	public static final int UPDATE_PREF =1;
	public static final int GET_PREF    =2;

	public SocialSharingTask(ShareListAdapter shareContext,int iWebSerViceFlag) {
		this.Sharecontext=shareContext;
		this.mWebSeviceFlag=iWebSerViceFlag;//1;
		this.mIFileFlag=SHARE_LIST_ADAPTER;
	}

	public SocialSharingTask(FacebookShare FBContext,int iWebSerViceFlag) {
		this.fb_context=FBContext;
		this.mWebSeviceFlag=iWebSerViceFlag;//0;
		this.mIFileFlag=FACEBOOK_SHARE;
	}
	
	public SocialSharingTask(PrepareRequestTokenActivity TWContext,int iWebSerViceFlag) {
		this.tw_context=TWContext;
		this.mWebSeviceFlag=iWebSerViceFlag;//=0;
		this.mIFileFlag=TWITTER_SHARE;
	}
	
	public SocialSharingTask(LoginActivity LoginContext,int iWebSerViceFlag) {
		this.loginContext=LoginContext;
		this.mWebSeviceFlag=iWebSerViceFlag;//2;
		this.mIFileFlag=LOGIN_ACT;
	}
	
	public SocialSharingTask(DelinkShareFragment deLinkContext,int iWebSerViceFlag) {
		this.delinkFragContext=deLinkContext;
		this.mWebSeviceFlag=iWebSerViceFlag;//0;
		this.mIFileFlag=DELINK_FRAG;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		if(this.mWebSeviceFlag == POST_PREF) {//share pref.
			return SocialSharingWebService.doSharePreferences(params);
		} else if(this.mWebSeviceFlag == UPDATE_PREF) {//update pref.
			return SocialSharingWebService.doUpdatePreferences(params); 
		} else if(this.mWebSeviceFlag == GET_PREF) {//get pref.
			return SocialSharingWebService.getSharedPreferences(params);
		} else 
			return false;
	}

	protected void onPostExecute(Boolean result) {
		if(mIFileFlag == SHARE_LIST_ADAPTER)
			this.Sharecontext.onAuthenticationResult(result);
		else if(mIFileFlag == FACEBOOK_SHARE) 
			this.fb_context.onAuthenticationResult(result);
		else if(mIFileFlag == TWITTER_SHARE) 
			this.tw_context.onAuthenticationResult(result);
		else if(mIFileFlag == LOGIN_ACT) 
			this.loginContext.onAuthenticationResult(result);
		else if(mIFileFlag == DELINK_FRAG) 
			this.delinkFragContext.onAuthenticationResult(result);
	} 
}
