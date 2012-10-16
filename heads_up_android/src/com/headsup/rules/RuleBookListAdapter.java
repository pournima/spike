package com.headsup.rules;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.headsup.R;

public class RuleBookListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	public static String[] rulesStringList;
	Context context;

	public RuleBookListAdapter(Context context, String[] rulesDataList) {
		this.context = context;
		rulesStringList = rulesDataList;
		mInflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		return rulesStringList.length;

	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.main_rule_custom, null);
			holder = new ViewHolder();
			holder.imgLogo = (ImageView) convertView
					.findViewById(R.id.imageLogo);
			holder.txtRules = (TextView) convertView
					.findViewById(R.id.textViewRules);
			holder.btnRuleBook = (ImageView) convertView.findViewById(R.id.imgBtnRuleBook);  
			holder.imgArrow = (ImageView) convertView.findViewById(R.id.imageNext);
			
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txtRules.setText(rulesStringList[position]);
		
		switch (position) {
		case 0:
			holder.imgLogo.setImageResource(R.drawable.heads_up_flag);
			holder.btnRuleBook.setVisibility(View.GONE);
			break;
		case 1:
			holder.imgLogo.setImageResource(R.drawable.signals_icon);
			//holder.btnRuleBook.setImageResource(R.drawable.btn_rules_book);
			holder.btnRuleBook.setVisibility(View.GONE);
			break;
		case 2:
			holder.imgLogo.setImageResource(R.drawable.heads_up_logo_flag);
			holder.btnRuleBook.setVisibility(View.GONE);
			holder.imgArrow.setVisibility(View.GONE);
			break;
		default:
			break;
		}

		holder.btnRuleBook.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.amazon.com/Youth-Football-Rulebook-2012-ebook/dp/B008PEXJCO/ref=sr_1_8?s=books&ie=UTF8&qid=1344605387&sr=1-8"));
				context.startActivity(browserIntent);				
			}
		});
		return convertView;
	}

	static class ViewHolder {
		TextView txtRules;
		ImageView imgLogo;
		ImageView btnRuleBook;
		ImageView imgArrow;
	}
}