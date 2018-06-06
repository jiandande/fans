package com.jiandande.review.activity;

import com.jiandande.money.R;
import com.jiandande.review.activity.PhoneChargeActivity.AmountSelectedListener;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.bean.UserMoreInfo;
import com.jiandande.review.transcation.ApplyOrderAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.CheckViewUtil;
import com.jiandande.review.util.Constants;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.StringUtil;
import com.jiandande.review.util.SystemUtil;
import com.jiandande.review.util.Constants.ParamName;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class AlipayChargeActivity extends AbstractActivity implements OnClickListener{

	
	ApplyOrderAction applyOrder;
	EditText edtAlipay;
	EditText edtReAlipay;
	EditText edtAlipayName;
	EditText edtAmount;
	TextView txtFee;
	TextView txtOtherAmount;
	
	double fee;
	int selectPosition;
    private ArrayAdapter<String> adapter;
	Button  btnConfirm;
	private Spinner spAmountList;
	RadioGroup radSetType;
	double otherAmount=0.0;
	String settingType="1";
	protected void addEventLister(){
		btnConfirm.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	
	
	protected void initView(){
		super.initView();
		btnConfirm=(Button)findViewById(R.id.confirm);
		edtAlipay=(EditText)findViewById(R.id.qqet);
	//	edtReAlipay=(EditText)findViewById(R.id.reqqet);
		edtAlipayName=(EditText)findViewById(R.id.qqnum);
		 spAmountList=(Spinner)findViewById(R.id.amount_list);
	//	txtFee=(TextView)findViewById(R.id.fee_txt);
	//	 edtAmount=(EditText)findViewById(R.id.amount);22222
	//	 radSetType=(RadioGroup)findViewById(R.id.setting_type);
	//	 txtOtherAmount=(TextView)findViewById(R.id.other_amount);
		 
		 selectPosition=0;
		 spAmountList=(Spinner)findViewById(R.id.amount_list);
			 //将可选内容与ArrayAdapter连接起来  
			String [] data=new String[]{"8元","28元","88元"};
	        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,data);            
	        //设置下拉列表的风格  
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
	        //将adapter 添加到spinner中  
	        spAmountList.setAdapter(adapter);           
	        //添加事件Spinner事件监听    
	        spAmountList.setOnItemSelectedListener(new AmountSelectedListener());
		 
		 
	
		 if(app.getUserInfo().bindStatus==UserInfo.BINDED){
			 UserMoreInfo moreinfo=app.getUserInfo().userMoreInfo;
			 if(moreinfo!=null){
				/* edtAlipay.setText(moreinfo.alipayNo);
				 edtReAlipay.setText(moreinfo.alipayNo);
				 edtAlipayName.setText(moreinfo.alipayName); 
				 edtAlipay.setEnabled(false);
				 edtReAlipay.setEnabled(false);
				 edtAlipayName.setEnabled(false);*/
			 }
		 }
	}
	
	@Override
	public void onClick(View v) {
		 if(v.getId()==R.id.back){
			 super.onBackPressed();
			 finish();
		 }else if(v.getId()==R.id.confirm){
			 if(super.checkValue(true)){
				     String aliPayNo=null;
				     
				 	UserInfo user=app.getUserInfo();
				 	if(user.bindStatus!=UserInfo.BINDED){
					
						Toast.makeText(AlipayChargeActivity.this, "绑定手机后才能兑换", Toast.LENGTH_LONG).show();	
						Intent intent=new Intent(AlipayChargeActivity.this,BindMoreInfoActivity.class);
					    startActivity(intent);
					    return;
					}
					if(CheckViewUtil.checkCardNotNull(edtAlipay, "支付宝帐号", dialog)){
						aliPayNo=edtAlipay.getText().toString();
						applyOrder.setAliPayno(aliPayNo.trim());
					}else{
						Toast.makeText(AlipayChargeActivity.this, "请输入支付宝帐号", Toast.LENGTH_LONG).show();
					    return;
					}
		
					
					
					if(CheckViewUtil.checkCardNotNull(edtAlipayName, "支付宝账户名", dialog)){
						 applyOrder.setAliPayName(edtAlipayName.getText().toString().trim());
					}else{
						 Toast.makeText(AlipayChargeActivity.this, "请输入支付宝账户名", Toast.LENGTH_LONG).show();
						return ;
					}
					
				
			
					
						if(selectPosition==0){
							applyOrder.setAmount(8+"");
						}else if(selectPosition==1){
							applyOrder.setAmount(28+"");
						}
						else if(selectPosition==2){
							applyOrder.setAmount(88+"");
						}	
					applyOrder.setTransferPayResponse(applyOrderResponse);			
					if (loadingDialog.isShowing()) {
						loadingDialog.setMessage("正在通讯中请稍候");
					} else {
						loadingDialog.show("正在通讯中请稍候");
					}
					applyOrder.setSettingType(settingType);
					String imei=SystemUtil.getPhoneIMEI2(this);//PreferenceUtil.getStrValue(AlipayChargeActivity.this, ParamName.IMEI, "");
					applyOrder.setImei(imei);
					applyOrder.setFee(fee+"");
					applyOrder.transStart();				
				}
		 }
	}

	TransferPayResponse  applyOrderResponse=new TransferPayResponse() {
		
		public void transComplete() {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			if (HttpCodeHelper.RESPONSE_SUCCESS
					.equals(applyOrder.respondData.respCode)) {
				// 保存
				Toast.makeText(AlipayChargeActivity.this,
						applyOrder.respondData.respDesc, Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(AlipayChargeActivity.this,
						applyOrder.respondData.respCode+"##"+applyOrder.respondData.respDesc, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			Toast.makeText(AlipayChargeActivity.this, errorInfo, Toast.LENGTH_LONG)
					.show();
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhifubao_recharge);
		initView();
		addEventLister();
		applyOrder=new ApplyOrderAction();
		applyOrder.setType(Constants.OrderType.TYPE_ALIPAY);
	}
	
	
	
	class AmountSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			selectPosition=position;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
		
	}
	
}
