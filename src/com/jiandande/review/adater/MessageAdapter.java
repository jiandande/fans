package com.jiandande.review.adater;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiandande.money.R;
import com.jiandande.review.bean.MessageBean;
import com.jiandande.review.util.StringUtil;
/**
 * @author hj
 * @version 创建时间：2014-4-24 下午3:38:18
 * 说明：
 */

public class MessageAdapter extends BaseAdapter 
{
	
	private ArrayList<MessageBean> mList = null;
	private Context mContext= null;
	private LayoutInflater layoutInflater;
	String str1="";
	
	public MessageAdapter(Context context){
		mContext = context;
		layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList=new ArrayList<MessageBean>();
	}
	
	 public void setDatas(ArrayList<MessageBean> datas,boolean isAdd) {
		   datas=  MessageBean.createMessage(datas);
			if(isAdd){
				mList.addAll(datas);
			}else{
				mList.clear();
				mList.addAll(datas);
			}
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
		MessageItemHolder holder = null;
		if(convertView == null )
		{
			holder = new MessageItemHolder();
			convertView = layoutInflater.inflate(R.layout.message_item, null);
			holder.sendUserText = (TextView) convertView.findViewById(R.id.title);
			holder.timeText = (TextView) convertView.findViewById(R.id.time);
			holder.contentText = (TextView) convertView.findViewById(R.id.msg);
			convertView.setTag(holder);
		}else{
			holder = (MessageItemHolder) convertView.getTag();
		}
		MessageBean message=mList.get(position);
		holder.sendUserText.setText(message.sendUserName);
	//	holder.timeText.setText(StringUtil.formatTime(message.time));
		holder.contentText.setText(message.content);
		if(message.msgid>0){
			holder.contentText.setTextColor(mContext.getResources().getColor(R.color.green));
		}	
		return convertView;
	}
	
	private static class MessageItemHolder{
		private TextView sendUserText;
		private TextView timeText;
		private TextView contentText;
	}


}

