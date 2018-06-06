package com.jiandande.review.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiandande.money.R;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.bean.UserMoreInfo;
import com.jiandande.review.transcation.FindMoreInfoAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;



public class ShowMoreInfoActivity extends AbstractActivity implements OnClickListener{

	FindMoreInfoAction bindInform;
	TextView edtAlipay;
	TextView edtAlipayName;
	
	TextView edtQqNo;
	TextView edtQqName;

	TextView edtPhoneNo;
	TextView edtAtrribute;
	
	LinearLayout lyContent;
	LinearLayout lyloadError;
	
	Button  btnUpdate;
	UserMoreInfo userMoreInfo;
	
	protected void addEventLister(){
		btnUpdate.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		lyloadError.setOnClickListener(this);
	}
	
	
	protected void initView(){
		super.initView();
		btnUpdate=(Button)findViewById(R.id.update);
		edtAlipay=(TextView)findViewById(R.id.zhifu);
		edtAlipayName=(TextView)findViewById(R.id.zhifuname);	
		
		 edtQqNo=(TextView)findViewById(R.id.qqnum);	
		 edtQqName=(TextView)findViewById(R.id.qqpwd);	

		 edtPhoneNo=(TextView)findViewById(R.id.tel);	
		 edtAtrribute=(TextView)findViewById(R.id.attribute);	
		 lyContent=(LinearLayout)findViewById(R.id.lv);	
		 lyloadError=(LinearLayout)findViewById(R.id.load_error);
		
	}
	
	@Override
	public void onClick(View v) {
		 if(v.getId()==R.id.back){
			 super.onBackPressed();
			 finish();
		 }else if(v.getId()==R.id.update){
			 //
			 Intent intent=new Intent(this,UpdateMoreInfoActivity.class);
			 startActivity(intent);
		 }else if(v.getId()==R.id.load_error){
			 finishFindInfo();
		 }
	}
TransferPayResponse  findAccountResponse=new TransferPayResponse() {
		
		public void transComplete() {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			if (HttpCodeHelper.RESPONSE_SUCCESS
					.equals(bindInform.respondData.respCode)) {
				// 保存
//				Toast.makeText(ShowMoreInfoActivity.this,
//						"绑定帐号信息成功", Toast.LENGTH_LONG)
//						.show();
				 userMoreInfo=bindInform.respondData.userMoreInfo;
				 UserInfo user=app.getUserInfo();
				 user.userMoreInfo=userMoreInfo;
				 app.setUserInfo(user);
				 edtAlipay.setText(userMoreInfo.alipayNo);
				 edtAlipayName.setText(userMoreInfo.alipayName);
				 edtQqNo.setText(userMoreInfo.qqNo);
				 edtQqName.setText(userMoreInfo.qqName);
				 edtPhoneNo.setText(userMoreInfo.phoneNo);
				 edtAtrribute.setText(userMoreInfo.attribute);
				 lyContent.setVisibility(View.VISIBLE);
				 lyloadError.setVisibility(View.GONE);
			} else {
				Toast.makeText(ShowMoreInfoActivity.this,
						bindInform.respondData.respCode+"##"+bindInform.respondData.respDesc, Toast.LENGTH_LONG)
						.show();
				lyContent.setVisibility(View.GONE);
			}
			
		}

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			Toast.makeText(ShowMoreInfoActivity.this, errorInfo, Toast.LENGTH_LONG)
					.show();
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_account_setting);
		initView();
		addEventLister();
		bindInform=new FindMoreInfoAction();	
		finishFindInfo();
	}
	
	void finishFindInfo(){
		bindInform.setTransferPayResponse(findAccountResponse);			
		if (loadingDialog.isShowing()) {
			loadingDialog.setMessage("正在查询中 ，请稍候");
		} else {
			loadingDialog.show("正在查询中，请稍候");
		}			
		bindInform.transStart();
	}
}
