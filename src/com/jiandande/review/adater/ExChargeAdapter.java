package com.jiandande.review.adater;


import com.jiandande.money.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class ExChargeAdapter extends BaseAdapter
{
	
	private String[] mList = null;
	private Context mContext= null;
	private LayoutInflater layoutInflater;
	public ExChargeAdapter(Context context)
	{
		mContext = context;
		layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList=new String[]{"支付宝转账","话费充值","Q币充值"};
	}
	
	@Override
	public int getCount() {
		return mList.length;
	}

	@Override
	public Object getItem(int position) {
		return mList[position];
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
			convertView = layoutInflater.inflate(R.layout.exchange_item, null);
			holder.itemText = (TextView) convertView.findViewById(R.id.title);
			holder.iconView = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(holder);
		}else{
			holder = (ExchargeItemHolder) convertView.getTag();
		}
		if(position==0){
			holder.iconView.setImageResource(R.drawable.zhifubao);
		}else if(position==1){
			holder.iconView.setImageResource(R.drawable.mobile);
		}else if(position==2){
			holder.iconView.setImageResource(R.drawable.qq);
		}
		holder.itemText.setText(mList[position]);
		holder.itemText.setTextSize(16.0f);
		holder.itemText.setTextColor(mContext.getResources().getColor(R.color.black));
		return convertView;
	}
	
	private static class ExchargeItemHolder{
		private TextView itemText;
		private ImageView iconView;
	}

}
