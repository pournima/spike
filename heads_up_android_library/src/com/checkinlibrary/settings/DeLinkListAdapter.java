//package checkinforgood.com.settings;
//
//import java.util.ArrayList;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Handler;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//import checkinforgood.com.CheckinNativeActivity;
//import checkinforgood.com.NoConnectivityScreen;
//import checkinforgood.com.R;
//import checkinforgood.com.ws.tasks.SocialSharingTask;
//
//public class DeLinkListAdapter extends BaseAdapter {
//	Context context;
//	ArrayList<String> shareItems;
//
//	static int FACEBOOK=0;
//	static int TWITTER=1;
//	private Button btnClick;
//	Handler mhandler;
//	
//	CheckinNativeActivity mMainContext;
//
//
//	public DeLinkListAdapter(Context context,  ArrayList<String>  shareItems,CheckinNativeActivity mContext) {
//		this.context = context;
//		this.shareItems = shareItems;
//		this.mMainContext=mContext;
//	}
//
//	@Override
//	public int getCount() {
//		if (shareItems != null)
//			return shareItems.size();
//		else
//			return 0;
//	}
//
//	@Override
//	public Object getItem(int index) {
//		return shareItems.get(index);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		LayoutInflater inflater = (LayoutInflater) context
//		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View v = inflater.inflate(R.layout.delink_list_item, parent, false);
//		TextView tv = (TextView) v.findViewById(R.id.testViewDelinkName);
//		tv.setText(shareItems.get(position));
//
//		btnClick=(Button)v.findViewById(R.id.btnDlinkShare);
//		btnClick.setId(position);
//
//		mhandler = new Handler();
//
//
//		btnClick.setOnClickListener(new View.OnClickListener() 
//		{
//			public void onClick(View v) {
//
//				if(v.getId() == FACEBOOK) {
//					onClearFbBtnClicked(v);
//				} else if(v.getId() == TWITTER) {
//					onClearTwBtnClicked(v);
//				}
//			}
//		});
//
//		return v;
//	}
//	
//	private void onClearFbBtnClicked(View view){
//    	Log.i("FB", "Clicked");
//    	
//    	CheckinNativeActivity.appStatus.clearSharedDataWithKey(CheckinNativeActivity.appStatus.FACEBOOK_TOKEN);
//		CheckinNativeActivity.appStatus.clearSharedDataWithKey(CheckinNativeActivity.appStatus.FACEBOOK_ON);
//		
//		postSocialPreferences("facebook");
//    	
//    }
//    
//    private void onClearTwBtnClicked(View view){
//    	Log.i("TW", "Clicked");
//    	
//    	CheckinNativeActivity.appStatus.clearSharedDataWithKey(CheckinNativeActivity
//				.appStatus.TWITTER_TOKEN);
//		CheckinNativeActivity.appStatus.clearSharedDataWithKey(CheckinNativeActivity
//				.appStatus.TWITTER_SECRET);
//		CheckinNativeActivity.appStatus.clearSharedDataWithKey(CheckinNativeActivity
//				.appStatus.TWITTER_ON);
//		
//		postSocialPreferences("twitter");
//    }
//    
//    private void postSocialPreferences(String social_network) {
//		String args[]=new String[5];
//		args[0]=CheckinNativeActivity.mAuthToken;
//		args[1]=social_network;//"facebook"; "twitter"
//		args[2]=Boolean.toString(false);
//		args[3]=null;
//		args[4]=null;
//		CheckinNativeActivity.strProgressMessage="Delinking account...";			
//		
//		if (CheckinNativeActivity.appStatus.isOnline()) {
//			mMainContext.showDialog(0);
//			new SocialSharingTask(this,SocialSharingTask.POST_PREF).execute(args);
//		} else {
//			Intent intent = new Intent(context, NoConnectivityScreen.class);
//			mMainContext.startActivity(intent);
//			Log.v("DelinkShareFragment", "App is not online!");
//		}
//	}
//    
//    public void onAuthenticationResult(Boolean success) {
//		if ( success ) {
//			mMainContext.removeDialog(0);
//			DisplayToast("Preferences cleared!");
//			
//		} else {
//			DisplayToast("Fail to clear preferences!");
//		}
//	}
//    private void DisplayToast(String msg) {
//		Toast.makeText(context, msg,Toast.LENGTH_SHORT).show();
//		mMainContext.removeDialog(0);
//	}     
//}
