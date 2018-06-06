package com.jiandande.review.activity;

import com.jiandande.money.R;
import com.jiandande.review.bean.ExchangeBean;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.StringUtil;
import com.jiandande.review.util.Constants.OrderType;
import com.jiandande.review.util.Constants.ParamName;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;



public class ExChangeOrderActivity extends AbstractActivity implements OnClickListener{

	
	
	TextView txtorderId;
	TextView txtorderType;
	TextView txtamount;
	TextView txtfee;
	TextView txtaccountName;
	TextView txtsubmitTime;
	TextView txtlastUpdateTime;
	TextView txtAdmin;//
	TextView txtphoneNo;
	TextView txtphoneNLabel;
	TextView txtContentLabel;
	TextView txtattribute;	
	TextView txtstatus;
	TextView txtremark;// 说明
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			 super.onBackPressed();
			 finish();
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detail);
		initView();
		addEventLister();
	}
	
	

	@Override
	protected void initView() {
		     super.initView();
		     txtorderId=(TextView)findViewById(R.id.orderid);
			 txtorderType=(TextView)findViewById(R.id.ordertype);
			 txtamount=(TextView)findViewById(R.id.amount);			
			 txtaccountName=(TextView)findViewById(R.id.apply_user);
			 txtsubmitTime=(TextView)findViewById(R.id.trans_date);			
			 txtAdmin=(TextView)findViewById(R.id.admin);
			 txtphoneNLabel=(TextView)findViewById(R.id.detail_title);
			 txtphoneNo =(TextView)findViewById(R.id.detail_content);
			 txtContentLabel=(TextView)findViewById(R.id.detail2_title);
			 txtattribute=(TextView)findViewById(R.id.detail2_content);
			
			 txtstatus=(TextView)findViewById(R.id.status);
			 txtremark=(TextView)findViewById(R.id.remark);
	    	 ExchangeBean order=getIntent().getParcelableExtra(ParamName.ORDER);
	    	  	 
	   // 	 txtorderId.setText(order.orderNo);
	    		if(order.pname.equals("积分兑换礼品"))
	    		{
			 txtorderType.setText(StringUtil.formatTransType(order.orderType));//StringUtil.GBK2Unicode(order.pname));
	    		}
	    		else
	    		{
	    			 txtorderType.setText(StringUtil.formatTransType(order.orderType)+"["+order.pname+"]");	
	    		}
			 txtamount.setText(order.amount+"");		
		//	 txtaccountName.setText(order.submiterAccountName+"");
			 txtsubmitTime.setText(StringUtil.formatTime(order.submitTime));		
		//	 txtAdmin.setText(order.adminId+"");
		/*	 
			 if(order.orderType==OrderType.TYPE_ALIPAY){
				 txtphoneNLabel.setText("支付宝帐号：");
				 txtphoneNo.setText(order.alpayNo);
				 txtContentLabel.setText("支付宝账户名：");
				 txtattribute.setText(order.alpayName);
			}else if(order.orderType==OrderType.TYPE_PHONE){
				 txtphoneNLabel.setText("充值手机号：");
				 txtphoneNo.setText(order.phoneNo);
				 txtContentLabel.setText("归属地：");
				 txtattribute.setText(order.attribute);
			}else if(order.orderType==OrderType.TYPE_QQ){
				 txtphoneNLabel.setText("QQ帐号：");
				 txtphoneNo.setText(order.qqNo);
				 txtContentLabel.setText("QQ昵称：");
				 txtattribute.setText(order.qqName);
			}*/
			 txtstatus.setText(StringUtil.formatTransStatus(order.status));
			//	LogUtil.i("ExChangeOrderActivity", "88888888888888888888888888:" );
		//	 txtremark.setText(order.remark);
		 
	}
	


	@Override
	protected void addEventLister() {
		super.addEventLister();
		btnBack.setOnClickListener(this);
	}
}

