package com.jiandande.review.adater;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiandande.money.R;
import com.jiandande.review.bean.AppBean;



public class ChargeAdapter  extends BaseAdapter 
{
	
	private ArrayList<AppBean> mList = null;
	private Context mContext= null;
	private LayoutInflater layoutInflater;
	String str1="";
	
	public ChargeAdapter(Context context){
		mContext = context;
		layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList=AppBean.findBeanList();
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ExchargeItemHolder holder = null;
		if(convertView == null )
		{
			holder = new ExchargeItemHolder();
			convertView = layoutInflater.inflate(R.layout.app_list_item, null);
			holder.titleText = (TextView) convertView.findViewById(R.id.title);
			holder.descText = (TextView) convertView.findViewById(R.id.awardbean);
			convertView.setTag(holder);
		}else{
			holder = (ExchargeItemHolder) convertView.getTag();
		}
		AppBean app=mList.get(position);
		holder.titleText.setText(app.title);
		holder.descText.setText(app.desc);
		return convertView;
	}
	
	private static class ExchargeItemHolder{
		private TextView titleText;
		private TextView descText;
	}


}
