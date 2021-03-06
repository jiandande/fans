package com.jiandande.review.adater;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiandande.money.R;
import com.jiandande.review.bean.BalanceChange;
import com.jiandande.review.bean.ExchangeBean;
import com.jiandande.review.util.StringUtil;
public class BalanceChangeAdapter  extends BaseAdapter 
{
	
	private ArrayList<BalanceChange> mList = null;
	private Context mContext= null;
	private LayoutInflater layoutInflater;
	String str1="";
	
	public BalanceChangeAdapter(Context context ,ArrayList<BalanceChange> datas){
		mContext = context;
		layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(datas==null){
			datas=new ArrayList<BalanceChange>();
		}
		this.mList=datas;
	}

	 public void setDatas(ArrayList<BalanceChange> datas,boolean isAdd) {
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
			convertView = layoutInflater.inflate(R.layout.balancechange_item, null);
			//holder.txtRemaider = (TextView) convertView.findViewById(R.id.exchangeid);
			holder.txtOrderId = (TextView) convertView.findViewById(R.id.exchangeid2);
			
			
			holder.txtAmount = (TextView) convertView.findViewById(R.id.gift);
			holder.txtRemark = (TextView) convertView.findViewById(R.id.amount);
			holder.txtTime = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		}else{
			holder = (ExchargeOrderItemHolder) convertView.getTag();
		}
		BalanceChange exchange=mList.get(position);
		//holder.txtRemaider.setText(StringUtil.moneyFormat(exchange.remainder));
		holder.txtOrderId.setText(StringUtil.moneyFormat(exchange.OrderId));
		holder.txtAmount.setText(StringUtil.moneyFormat(exchange.amount));
		holder.txtRemark.setText(exchange.remark);
		holder.txtTime.setText(StringUtil.formatTime(exchange.submitTime));
		return convertView;
	}
	
	private static class ExchargeOrderItemHolder{
		private TextView txtOrderId;//txtRemaider;
		private TextView txtRemark;
		private TextView txtAmount;
		private TextView txtTime;
		
	}


}
