package com.jiandande.review.activity;

import com.jiandande.money.R;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.bean.UserMoreInfo;
import com.jiandande.review.transcation.ApplyOrderAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.CheckViewUtil;
import com.jiandande.review.util.Constants;
import com.jiandande.review.util.SystemUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class PhoneChargeActivity extends AbstractActivity implements OnClickListener{

	
	ApplyOrderAction applyOrder;
	EditText edtPhoneNo;
	EditText edtRePhoneNo;
	EditText edtAttribute;
//	TextView txtFee;
	double fee;
	int selectPosition;
    private ArrayAdapter<String> adapter;
	
	private Spinner spAmountList;
	
	Button  btnConfirm;
	
	
	protected void addEventLister(){
		btnConfirm.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	
	
	protected void initView(){
		super.initView();
		btnConfirm=(Button)findViewById(R.id.confirm);
		edtPhoneNo=(EditText)findViewById(R.id.qqet);
		edtRePhoneNo=(EditText)findViewById(R.id.reqqet);
		edtAttribute=(EditText)findViewById(R.id.qqnum);
		// txtFee=(TextView)findViewById(R.id.fee_txt);
		 selectPosition=0;
		 spAmountList=(Spinner)findViewById(R.id.amount_list);
			 //将可选内容与ArrayAdapter连接起来  
			String [] data=new String[]{"10元","30元","50元"};
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
				/* edtPhoneNo.setText(moreinfo.phoneNo);
				// edtRePhoneNo.setText(moreinfo.phoneNo);
				// edtAttribute.setText(moreinfo.attribute); 
				 edtPhoneNo.setEnabled(false);
				// edtRePhoneNo.setEnabled(false);
				 edtAttribute.setEnabled(false);*/
			 }
		 }
	}
	
	@Override
	public void onClick(View v) {
		 if(v.getId()==R.id.back){
			 super.onBackPressed();
			 finish();
		 }else if(v.getId()==R.id.confirm){
			 UserInfo user=app.getUserInfo();
			 	if(user.bindStatus!=UserInfo.BINDED){
				
					Toast.makeText(PhoneChargeActivity.this, "绑定手机后才能兑换", Toast.LENGTH_LONG).show();	
					Intent intent=new Intent(PhoneChargeActivity.this,BindMoreInfoActivity.class);
				    startActivity(intent);
				    return;
				}
			 if(super.checkValue(true)){
				     String qqNo=null;
					 	
					if(CheckViewUtil.checkCardNotNull(edtPhoneNo, "手机号码", dialog)){
						qqNo=edtPhoneNo.getText().toString();
						applyOrder.setPhoneNo(qqNo.trim());
					}else{
						Toast.makeText(PhoneChargeActivity.this, "请输入手机号码", Toast.LENGTH_LONG).show();
					    return;
					}
					
					if(CheckViewUtil.checkCardNotNull(edtRePhoneNo, "确认手机号码", dialog)){
	                    if(!qqNo.equals(edtRePhoneNo.getText().toString())){
	                    	Toast.makeText(PhoneChargeActivity.this, "2次输入的手机号码不相同", Toast.LENGTH_LONG).show();
	   					    return ;
						}
					}else{
						Toast.makeText(PhoneChargeActivity.this, "确认手机号码不能为空", Toast.LENGTH_LONG).show();
						 return ;
					}
					
					
					
					applyOrder.setTransferPayResponse(applyOrderResponse);			
					if(selectPosition==0){
						applyOrder.setAmount(10+"");
					}else if(selectPosition==1){
						applyOrder.setAmount(30+"");
					}else if(selectPosition==2){
						applyOrder.setAmount(50+"");
				
					}
					if (loadingDialog.isShowing()) {
						loadingDialog.setMessage("正在通讯中请稍候");
					} else {
						loadingDialog.show("正在通讯中请稍候");
					}
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
				Toast.makeText(PhoneChargeActivity.this,
						applyOrder.respondData.respDesc, Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(PhoneChargeActivity.this,
						applyOrder.respondData.respCode+"##"+applyOrder.respondData.respDesc, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			Toast.makeText(PhoneChargeActivity.this, errorInfo, Toast.LENGTH_LONG)
					.show();
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mobile_recharge);
		initView();
		addEventLister();
		applyOrder=new ApplyOrderAction();
		applyOrder.setType(Constants.OrderType.TYPE_PHONE);
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
