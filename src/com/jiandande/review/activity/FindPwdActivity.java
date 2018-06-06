package com.jiandande.review.activity;

import com.jiandande.money.R;
import com.jiandande.review.transcation.FindBackPasswordAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.CheckViewUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FindPwdActivity extends AbstractActivity implements OnClickListener{

	
	FindBackPasswordAction findPasswd;
	
	Button btnConfirm;
	EditText txtAccountNo;
	EditText txtEmail;
	EditText txtReEmail;
	@Override
	public void onClick(View v) {
		 if(v.getId()==R.id.back){
			 super.onBackPressed();
			 finish();
		 }else if(v.getId()==R.id.confirm){
			 if(super.checkValue(true)){
				 if(CheckViewUtil.checkCardNotNull(txtAccountNo, "帐号", dialog)){
					 findPasswd.setAccountNo(txtAccountNo.getText().toString().trim());
				}else{
					 Toast.makeText(FindPwdActivity.this, "帐号不能为空", Toast.LENGTH_LONG).show();
					return ;
				}
				 String email;
				 if(CheckViewUtil.checkCardNotNull(txtEmail, "邮箱", dialog)){
					 email=txtEmail.getText().toString().trim()	;
				}else{
					 Toast.makeText(FindPwdActivity.this, "邮箱不能为空", Toast.LENGTH_LONG).show();
					return ;
				}
				 if(CheckViewUtil.checkCardNotNull(txtReEmail, "邮箱", dialog)){
					 String reemail=txtReEmail.getText().toString().trim();
					 if(reemail.equals(email)){
						findPasswd.setEmail(reemail); 
					 }else{
						 Toast.makeText(FindPwdActivity.this, "2次输入的邮箱不一致", Toast.LENGTH_LONG).show();
							return ;
					 }
				}else{
					 Toast.makeText(FindPwdActivity.this, "确认邮箱不能为空", Toast.LENGTH_LONG).show();
					return ;
				}
				 if (loadingDialog.isShowing()) {
						loadingDialog.setMessage("正在通讯中请稍候");
					} else {
						loadingDialog.show("正在通讯中请稍候");
					}				
				 findPasswd.setTransferPayResponse(feedResponse);
				 findPasswd.transStart();
			 }
		  }
		}
TransferPayResponse  feedResponse=new TransferPayResponse() {
		
		public void transComplete() {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			if (HttpCodeHelper.RESPONSE_SUCCESS
					.equals(findPasswd.respondData.respCode)) {
				// 保存
				Toast.makeText(FindPwdActivity.this,
						"新密码已经发送到你邮箱，请查看，注意保管密码", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(FindPwdActivity.this,
						findPasswd.respondData.respCode+"##"+findPasswd.respondData.respDesc, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			Toast.makeText(FindPwdActivity.this, errorInfo, Toast.LENGTH_LONG)
					.show();
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_password);
		initView();
		addEventLister();
		findPasswd=new FindBackPasswordAction();
	}
	protected void addEventLister(){
		btnConfirm.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	
	protected void initView(){
		super.initView();
		btnConfirm=(Button)findViewById(R.id.confirm);
		txtAccountNo=(EditText)findViewById(R.id.username);
		txtReEmail=(EditText)findViewById(R.id.reqqet);
		txtEmail=(EditText)findViewById(R.id.qqet);
	}
	
}
