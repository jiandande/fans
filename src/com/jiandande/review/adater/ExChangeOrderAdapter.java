package com.jiandande.review.adater;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jiandande.money.R;
import com.jiandande.review.activity.BalanceChangeActivity;
import com.jiandande.review.bean.ExchangeBean;
import com.jiandande.review.util.StringUtil;


public class ExChangeOrderAdapter  extends BaseAdapter 
{
	
	private ArrayList<ExchangeBean> mList = null;
	private Context mContext= null;
	private LayoutInflater layoutInflater;
	String str1="";
	
	public ExChangeOrderAdapter(Context context ,ArrayList<ExchangeBean> datas){
		mContext = context;
		layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(datas==null){
			datas=new ArrayList<ExchangeBean>();
		}
		this.mList=datas;
	}

	 public void setDatas(ArrayList<ExchangeBean> datas,boolean isAdd) {
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
		ExchargeOrderItemHolder holder = null;
		if(convertView == null )
		{
		
		
			holder = new ExchargeOrderItemHolder();
			convertView = layoutInflater.inflate(R.layout.myexchangelist_item, null);
			holder.txtOrderId = (TextView) convertView.findViewById(R.id.exchangeid2);
			holder.txtType = (TextView) convertView.findViewById(R.id.gift);
			holder.txtAmount = (TextView) convertView.findViewById(R.id.amount);
			holder.txtStatus = (TextView) convertView.findViewById(R.id.state);
			holder.txtTime = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		}else{
			holder = (ExchargeOrderItemHolder) convertView.getTag();
		}
		ExchangeBean exchange=mList.get(position);
     	holder.txtOrderId.setText(StringUtil.formatTransID(exchange.orderId));//exchange.orderId
     	if(exchange.pname.equals("积分兑换礼品"))
		{
		holder.txtType.setText(StringUtil.formatTransType(exchange.orderType));///StringUtil.GBK2Unicode(exchange.pname));
		}
     	
		else
		{
			holder.txtType.setText(StringUtil.formatTransType(exchange.orderType)+"["+exchange.pname+"]");	
		}
		holder.txtAmount.setText(StringUtil.moneyFormat(exchange.amount/1000)+"元");
		holder.txtStatus.setText(StringUtil.formatTransStatus(exchange.status));
		holder.txtTime.setText(StringUtil.formatTime(exchange.submitTime));
		return convertView;
	}
	
	private static class ExchargeOrderItemHolder{
		private TextView txtOrderId;
		private TextView txtType;
		private TextView txtAmount;
		private TextView txtStatus;
		private TextView txtTime;
	}


}
