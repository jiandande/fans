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

public class QqChargeActivity extends AbstractActivity implements OnClickListener{

	
	ApplyOrderAction applyOrder;
	EditText edtQqNo;
	EditText edtReQqNo;
	EditText edtQqName;
	TextView txtFee;
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
		 edtQqNo=(EditText)findViewById(R.id.qqet);
		 edtReQqNo=(EditText)findViewById(R.id.reqqet);
		 edtQqName=(EditText)findViewById(R.id.qqnum);
	//	 txtFee=(TextView)findViewById(R.id.fee_txt);
		 selectPosition=0;
		 spAmountList=(Spinner)findViewById(R.id.amount_list);
			 //将可选内容与ArrayAdapter连接起来  
			String [] data=new String[]{"2Q币","20Q币","58Q币"};
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
				/* edtQqNo.setText(moreinfo.qqNo);
				 edtReQqNo.setText(moreinfo.qqNo);
				 edtQqName.setText(moreinfo.qqName); 
				 edtQqNo.setEnabled(false);
				 edtReQqNo.setEnabled(false);
				 edtQqName.setEnabled(false);*/
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
				
					Toast.makeText(QqChargeActivity.this, "绑定手机后才能兑换", Toast.LENGTH_LONG).show();	
					Intent intent=new Intent(QqChargeActivity.this,BindMoreInfoActivity.class);
				    startActivity(intent);
				    return;
				}
			 if(super.checkValue(true)){
				     String qqNo=null;
					if(CheckViewUtil.checkCardNotNull(edtQqNo, "QQ号", dialog)){
						qqNo=edtQqNo.getText().toString();
						applyOrder.setQqNo(qqNo.trim());
					}else{
						Toast.makeText(QqChargeActivity.this, "QQ号不能为空", Toast.LENGTH_LONG).show();
					    return;
					}
					
					if(CheckViewUtil.checkCardNotNull(edtReQqNo, "确认QQ号", dialog)){
	                    if(!qqNo.equals(edtReQqNo.getText().toString())){
	                    	Toast.makeText(QqChargeActivity.this, "2次输入的QQ号不相同", Toast.LENGTH_LONG).show();
	   					    return ;
						}
					}else{
						Toast.makeText(QqChargeActivity.this, "确认QQ号不能为空", Toast.LENGTH_LONG).show();
						 return ;
					}
					
					if(CheckViewUtil.checkCardNotNull(edtQqName, "QQ昵称", dialog)){
						 applyOrder.setQqName(edtQqName.getText().toString().trim());
					}else{
						 Toast.makeText(QqChargeActivity.this, "QQ 昵称不能为空", Toast.LENGTH_LONG).show();
						return ;
					}
					
					applyOrder.setTransferPayResponse(applyOrderResponse);			
					if(selectPosition==0){
						applyOrder.setAmount(2+"");
					}else if(selectPosition==1){
						applyOrder.setAmount(20+"");
					}else if(selectPosition==2){
						applyOrder.setAmount(58+"");
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
				Toast.makeText(QqChargeActivity.this,
						applyOrder.respondData.respDesc, Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(QqChargeActivity.this,
						applyOrder.respondData.respCode+"##"+applyOrder.respondData.respDesc, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			Toast.makeText(QqChargeActivity.this, errorInfo, Toast.LENGTH_LONG)
					.show();
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qq_recharge);
		initView();
		addEventLister();
		applyOrder=new ApplyOrderAction();
		applyOrder.setType(Constants.OrderType.TYPE_QQ);
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
