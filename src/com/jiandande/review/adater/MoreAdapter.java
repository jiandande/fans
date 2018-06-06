package com.jiandande.review.adater;


import com.jiandande.money.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author shimingzheng
 * @version 创建时间：2012-7-19 下午3:18:34
 * 说明：
 */

public class MoreAdapter extends BaseAdapter implements OnTouchListener
{
	
	private String[] mList = null;
	private Context mContext= null;
	private LayoutInflater layoutInflater;
	public MoreAdapter(Context context,String[] list)
	{
		mContext = context;
		layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = list;
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
		MoreItemHolder holder = null;
		if(convertView == null )
		{
			holder = new MoreItemHolder();
			convertView = layoutInflater.inflate(R.layout.more_item_layout, null);
			holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.parent_layout_id);
			holder.itemText = (TextView) convertView.findViewById(R.id.item_id);
			convertView.setTag(holder);
		}else{
			holder = (MoreItemHolder) convertView.getTag();
		}
		holder.linearLayout.setBackgroundResource(R.drawable.form2_mid);
		if(position == 0)
		{
			holder.linearLayout.setBackgroundResource(R.drawable.form2_top);
		}else if(position == (getCount() - 1))
		{
			holder.linearLayout.setBackgroundResource(R.drawable.form2_bottom);
		}
		holder.itemText.setText(mList[position]);
		holder.itemText.setTextSize(16.0f);
		holder.itemText.setTextColor(mContext.getResources().getColor(R.color.black));
		return convertView;
	}
	
	private static class MoreItemHolder{
		private TextView itemText;
		private LinearLayout linearLayout;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
