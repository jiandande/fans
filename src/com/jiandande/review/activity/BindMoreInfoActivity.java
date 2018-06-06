package com.jiandande.review.activity;

import java.sql.Date;
import java.text.SimpleDateFormat;




import java.util.HashMap;













import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;





import com.jiandande.money.R;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.transcation.BindMoreInfoAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.CheckViewUtil;
import com.jiandande.review.util.CountDownTimerUtils;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.StringUtil;
import com.jiandande.review.util.SystemUtil;
import com.jiandande.review.util.Constants.ParamName;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class BindMoreInfoActivity extends AbstractActivity implements OnClickListener{
	
	
	
	

	
	BindMoreInfoAction bindInform;

	


	EditText edtPhoneNo;
	EditText edtAtrribute;
	EditText edtInvitecode;
	

	Button  btnConfirm;
	Button  btnRequestCode;
	RadioGroup radSetSex;
	

	protected void addEventLister(){
		btnConfirm.setOnClickListener(this);
		btnRequestCode.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	
	
	protected void initView(){
		super.initView();
		btnConfirm=(Button)findViewById(R.id.confirm);
		btnRequestCode=(Button)findViewById(R.id.request_code_btn);
	

		radSetSex=(RadioGroup)findViewById(R.id.setting_sex);
		
		 edtPhoneNo=(EditText)findViewById(R.id.tel);	
		 
		 edtAtrribute=(EditText)findViewById(R.id.attribute);	
		 edtInvitecode=(EditText)findViewById(R.id.invitecode);	
			
		 BmobSMS.initialize(this,"54bb7ee8a8acd477bf5f19393e7d9c05");
		
		 radSetSex.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					if(checkedId==R.id.radiobutton1){
						bindInform.setSex("1");
					}else{
						bindInform.setSex("0");	
					}
				}
			});
	}
	
	
	@Override
	public void onClick(View v) {
		 if(v.getId()==R.id.back){
			 super.onBackPressed();
			 finish();
		 }else if(v.getId()==R.id.request_code_btn){
			 String phoneNo=edtPhoneNo.getText().toString();
				if(CheckViewUtil.checkCardNotNull(edtPhoneNo, "手机号码", dialog)){
				
					bindInform.setPhoneNo(phoneNo.trim());
				}else{
					Toast.makeText(BindMoreInfoActivity.this, "手机号码不能为空", Toast.LENGTH_LONG).show();
				    return;
				}
			 BmobSMS.requestSMSCode(this, phoneNo, "完善资料",new RequestSMSCodeListener() {

				 @Override
				    public void done(Integer smsId,BmobException ex) {
				        // TODO Auto-generated method stub
				        if(ex==null){//
				        	 Toast.makeText(BindMoreInfoActivity.this," 验证码已发送成功..." ,Toast.LENGTH_LONG).show();
				    
				        }else{
				        	 Toast.makeText(BindMoreInfoActivity.this,"errorCode = "+ex.getErrorCode()+",errorMsg = "+ex.getLocalizedMessage(),Toast.LENGTH_LONG).show();

				        }
				    }
				});
		
		
		
			CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(btnRequestCode, 60000, 1000);

			 mCountDownTimerUtils.start();
				    }
	     else if(v.getId()==R.id.confirm){
			 if(super.checkValue(true)){
		
					String phoneNo=edtPhoneNo.getText().toString();
					if(CheckViewUtil.checkCardNotNull(edtPhoneNo, "手机号码", dialog)){
					
						bindInform.setPhoneNo(phoneNo.trim());
					}else{
						Toast.makeText(BindMoreInfoActivity.this, "手机号码不能为空", Toast.LENGTH_LONG).show();
					    return;
					}
					
					if(CheckViewUtil.checkCardNotNull(edtAtrribute, "验证码", dialog)){
						bindInform.setAttrbute(edtAtrribute.getText().toString().trim());
					}else{
						 Toast.makeText(BindMoreInfoActivity.this, "验证码不能为空", Toast.LENGTH_LONG).show();
						return ;
					}
					if(CheckViewUtil.checkCardNotNull(edtInvitecode, "邀请码", dialog)){
						bindInform.setinvitecode(edtInvitecode.getText().toString().trim());
					}else{
					//	 Toast.makeText(BindMoreInfoActivity.this, "输入密友邀请码,各得2元积分奖励!", Toast.LENGTH_LONG).show();
					
					}
					
					String imei=PreferenceUtil.getStrValue(BindMoreInfoActivity.this, ParamName.IMEI, "");

					if(StringUtil.checkIsNull(imei)){
						imei=new SystemUtil(this).getNewPhoneIMEI();
						PreferenceUtil.saveStrValue(BindMoreInfoActivity.this, ParamName.IMEI, imei);
					}
					bindInform.setImei(imei);
					bindInform.setPhoneNo(phoneNo);
					
					
				  
				    BmobSMS.verifySmsCode(this,phoneNo,edtAtrribute.getText().toString().trim(), new VerifySMSCodeListener() {

					    @Override
					    public void done(BmobException ex) {
					        // TODO Auto-generated method stub
					        if(ex==null){//短信验证码已验证成功
					           // Log.i("bmob", "验证通过");
					    
					  
					        //	Toast.makeText(BindMoreInfoActivity.this,"验证通过,正在通讯中请稍候" , Toast.LENGTH_LONG).show();
					        	bindInform.setTransferPayResponse(setAccountResponse);			
								if (loadingDialog.isShowing()) {
									loadingDialog.setMessage("正在通讯中请稍候");
								} else {
									loadingDialog.show("正在通讯中请稍候");
								}			
								bindInform.transStart();	
					        }else{
					        	Toast.makeText(BindMoreInfoActivity.this, "验证码输入有误：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
					   
					        	return ;
					          //  Log.i("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
					        }
					    }
					});
				try {
					Thread.sleep(888);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		

				}
		 }
	}

	TransferPayResponse  setAccountResponse=new TransferPayResponse() {
		
		public void transComplete() {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}

			if (HttpCodeHelper.RESPONSE_SUCCESS
					.equals(bindInform.respondData.respCode)) {
				// 保存
				UserInfo user=app.getUserInfo();
				user.bindStatus=UserInfo.BINDED;
				Toast.makeText(BindMoreInfoActivity.this,
						bindInform.respondData.respDesc, Toast.LENGTH_LONG)
						.show();
				finish();
				
			} else {
				Toast.makeText(BindMoreInfoActivity.this,
						bindInform.respondData.respCode+"##"+bindInform.respondData.respDesc, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			Toast.makeText(BindMoreInfoActivity.this, errorInfo, Toast.LENGTH_LONG)
					.show();
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_setting);
		initView();
		addEventLister();
		bindInform=new BindMoreInfoAction();
		
	}
	
}
