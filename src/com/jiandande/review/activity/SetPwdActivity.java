package com.jiandande.review.activity;

import com.jiandande.money.R;
import com.jiandande.review.transcation.PerfectInformAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.CheckViewUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetPwdActivity extends AbstractActivity implements OnClickListener{

	PerfectInformAction perfectInform;
	
	EditText edtEmail;
	EditText edtPasswd;
	EditText edtReEmail;
	EditText edtRePasswd;
	
	Button  btnConfirm;
	@Override
	public void onClick(View v) {
		 if(v.getId()==R.id.back){
			 super.onBackPressed();
			 finish();
		 }else if(v.getId()==R.id.confirm){
			 if(super.checkValue(true)){
				     String email=null;
					if(CheckViewUtil.checkCardNotNull(edtEmail, "邮箱", dialog)){
						 email=edtEmail.getText().toString();
						perfectInform.setEmail(email.trim());
					}else{
						Toast.makeText(SetPwdActivity.this, "邮箱不能为空", Toast.LENGTH_LONG).show();
					    return;
					}
					
					if(CheckViewUtil.checkCardNotNull(edtReEmail, "确认邮箱", dialog)){
	                    if(!email.equals(edtReEmail.getText().toString())){
	                    	Toast.makeText(SetPwdActivity.this, "2次输入的邮箱不相同", Toast.LENGTH_LONG).show();
	   					    return ;
						}
					}else{
						Toast.makeText(SetPwdActivity.this, "确认邮箱不能为空", Toast.LENGTH_LONG).show();
						 return ;
					}
					
					String password;
					if(CheckViewUtil.checkCardNotNull(edtPasswd, "密码", dialog)){
						password=edtPasswd.getText().toString();
						perfectInform.setPassword(password.trim());
					}else{
						Toast.makeText(SetPwdActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
						 return ;
					}
					if(CheckViewUtil.checkLengthIsOk(edtPasswd, "密码", 6, 16, dialog)){
						password=edtPasswd.getText().toString();
						perfectInform.setPassword(password.trim());
					}else{
						Toast.makeText(SetPwdActivity.this, "密码长度大于5小于16",
								Toast.LENGTH_LONG).show();
						 return ;
					}
					if(CheckViewUtil.checkCardNotNull(edtRePasswd, "确认密码", dialog)){
	                    if(!password.equals(edtRePasswd.getText().toString())){
	                    	Toast.makeText(SetPwdActivity.this, "2次输入的密码不相同", Toast.LENGTH_LONG).show();
	   					    return ;
						}
					}else{
						Toast.makeText(SetPwdActivity.this, "确认密码不能为空", Toast.LENGTH_LONG).show();
						 return ;
					}
					perfectInform.setTransferPayResponse(perfectInformResponse);
					if (loadingDialog.isShowing()) {
						loadingDialog.setMessage("正在开通中请稍候");
					} else {
						loadingDialog.show("正在开通中请稍候");
					}
					perfectInform.transStart();
				}
		 }
	}

	TransferPayResponse  perfectInformResponse=new TransferPayResponse() {
		
		public void transComplete() {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			if (HttpCodeHelper.RESPONSE_SUCCESS
					.equals(perfectInform.respondData.respCode)) {
				// 保存
				Toast.makeText(SetPwdActivity.this,
						"设置成功", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(SetPwdActivity.this,
						perfectInform.respondData.respCode+"##"+perfectInform.respondData.respDesc, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			Toast.makeText(SetPwdActivity.this, errorInfo, Toast.LENGTH_LONG)
					.show();
		}
		
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingpwd);
		perfectInform=new PerfectInformAction();
		initView();
		addEventLister();
	}
	protected void addEventLister(){
		btnConfirm.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	protected void initView(){
		super.initView();
		edtEmail=(EditText)findViewById(R.id.qqnum );
		edtPasswd=(EditText)findViewById(R.id.qqpwd);
		edtReEmail=(EditText)findViewById(R.id.reqqnum);
		edtRePasswd=(EditText)findViewById(R.id.reqqpwd);
		btnConfirm=(Button)findViewById(R.id.confirm);
	}
}
