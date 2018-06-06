package com.jiandande.review.activity;

import com.jiandande.money.R;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.bean.UserMoreInfo;
import com.jiandande.review.transcation.UpdateMoreInfoAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.CheckViewUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateMoreInfoActivity extends AbstractActivity implements OnClickListener{

	
	UpdateMoreInfoAction updateInform;
	EditText edtAlipay;
	EditText edtAlipayName;
	
	EditText edtQqNo;
	EditText edtQqName;

	EditText edtPhoneNo;
	EditText edtAtrribute;
	
	EditText edtPassword;
	Button  btnConfirm;
	
	
	
	protected void addEventLister(){
		btnConfirm.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	
	
	protected void initView(){
		super.initView();
		btnConfirm=(Button)findViewById(R.id.confirm);
		edtAlipay=(EditText)findViewById(R.id.zhifu);
		edtAlipayName=(EditText)findViewById(R.id.zhifuname);	
		
		 edtQqNo=(EditText)findViewById(R.id.qqnum);	
		 edtQqName=(EditText)findViewById(R.id.qqpwd);	

		 edtPhoneNo=(EditText)findViewById(R.id.tel);	
		 edtAtrribute=(EditText)findViewById(R.id.attribute);	
		
		 edtPassword=(EditText)findViewById(R.id.paypass);		
		
		 UserMoreInfo moreInfo=app.getUserInfo().userMoreInfo;
		  edtAlipay.setText(moreInfo.alipayNo);
		  edtAlipayName.setText(moreInfo.alipayName);			
		  edtQqNo.setText(moreInfo.qqNo);
	      edtQqName.setText(moreInfo.qqName);
		  edtPhoneNo.setText(moreInfo.phoneNo);
		  edtAtrribute.setText(moreInfo.attribute);
	}
	
	@Override
	public void onClick(View v) {
		 if(v.getId()==R.id.back){
			 super.onBackPressed();
			 finish();
		 }else if(v.getId()==R.id.confirm){
			 if(super.checkValue(true)){
				    
					if(CheckViewUtil.checkCardNotNull(edtAlipay, "支付宝帐号", dialog)){
						String aliPayNo=edtAlipay.getText().toString();
						updateInform.setAliPayNo(aliPayNo.trim());
					}else{
						Toast.makeText(UpdateMoreInfoActivity.this, "支付宝帐号不能为空", Toast.LENGTH_LONG).show();
					    return;
					}
					
					if(CheckViewUtil.checkCardNotNull(edtAlipayName, "支付宝账户名", dialog)){
						updateInform.setAlipayName(edtAlipayName.getText().toString().trim());
					}else{
						 Toast.makeText(UpdateMoreInfoActivity.this, "支付宝账户名不能为空", Toast.LENGTH_LONG).show();
						return ;
					}
					
					if(CheckViewUtil.checkCardNotNull(edtPhoneNo, "手机号码", dialog)){
						String phoneNo=edtPhoneNo.getText().toString();
						updateInform.setPhoneNo(phoneNo.trim());
					}else{
						Toast.makeText(UpdateMoreInfoActivity.this, "手机号码不能为空", Toast.LENGTH_LONG).show();
					    return;
					}
					
					if(CheckViewUtil.checkCardNotNull(edtAtrribute, "归属地", dialog)){
						updateInform.setAttrbute(edtAtrribute.getText().toString().trim());
					}else{
						 Toast.makeText(UpdateMoreInfoActivity.this, "归属地不能为空", Toast.LENGTH_LONG).show();
						return ;
					}
					
					if(CheckViewUtil.checkCardNotNull(edtQqNo, "QQ号", dialog)){
						String aliPayNo=edtQqNo.getText().toString();
						updateInform.setQqNo(aliPayNo.trim());
					}else{
						Toast.makeText(UpdateMoreInfoActivity.this, "QQ号不能为空", Toast.LENGTH_LONG).show();
					    return;
					}
					
					if(CheckViewUtil.checkCardNotNull(edtQqName, "QQ昵称", dialog)){
						updateInform.setQqName(edtQqName.getText().toString().trim());
					}else{
						 Toast.makeText(UpdateMoreInfoActivity.this, "QQ昵称不能为空", Toast.LENGTH_LONG).show();
						return ;
					}
					
					 String password=null;
						if(CheckViewUtil.checkCardNotNull(edtPassword, "绑定密码", dialog)){
							password=edtPassword.getText().toString();
							updateInform.setPassword(password.trim());
						}else{
							Toast.makeText(UpdateMoreInfoActivity.this, "绑定密码不能为空", Toast.LENGTH_LONG).show();
						    return;
						}
						
					
						updateInform.setTransferPayResponse(setAccountResponse);			
					if (loadingDialog.isShowing()) {
						loadingDialog.setMessage("正在通讯中请稍候");
					} else {
						loadingDialog.show("正在通讯中请稍候");
					}			
					updateInform.transStart();				
				}
		 }
	}

	TransferPayResponse  setAccountResponse=new TransferPayResponse() {
		
		public void transComplete() {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			if (HttpCodeHelper.RESPONSE_SUCCESS
					.equals(updateInform.respondData.respCode)) {
				// 保存
				UserInfo user=app.getUserInfo();
				user.bindStatus=UserInfo.BINDED;
				Toast.makeText(UpdateMoreInfoActivity.this,
						"绑定帐号信息成功", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(UpdateMoreInfoActivity.this,
						updateInform.respondData.respCode+"##"+updateInform.respondData.respDesc, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			Toast.makeText(UpdateMoreInfoActivity.this, errorInfo, Toast.LENGTH_LONG)
					.show();
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_account_setting);
		initView();
		addEventLister();
		updateInform=new UpdateMoreInfoAction();
		
	}
	
}
