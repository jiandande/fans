package com.jiandande.review.activity;

import net.youmi.android.normal.video.VideoAdManager;

import com.jiandande.money.R;
import com.jiandande.review.bean.HistoryMoney;
import com.jiandande.review.transcation.ClientLoginAction;
import com.jiandande.review.transcation.ClientRegisterAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.CheckViewUtil;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.StringUtil;
import com.jiandande.review.util.SystemUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.PlatType;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AbstractActivity implements OnClickListener{

	private Button btnLogin;
	private Button btnFindPwd;
	private Button btnLoginCheck;
	private Button btnRegister;
	private Button btnAutoLogin;

	
	private TextView txtName;
	
	private TextView txtPasswd;
	
	private String email;//email
	private String password;//密码
	private ClientLoginAction loginAction;
	private ClientRegisterAction clientRegister;
	private boolean isCheck=true;
	private boolean autoIsCheck=false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initView();
		addEventLister();
		

	}
	@Override
	protected void addEventLister() {
		super.addEventLister();
		btnLogin.setOnClickListener(this);
		btnFindPwd.setOnClickListener(this);
		btnLoginCheck.setOnClickListener(this);	
		btnRegister.setOnClickListener(this);
		btnAutoLogin.setOnClickListener(this);

	}
	@Override
	protected void initView() {
		super.initView();
		btnRegister=(Button)findViewById(R.id.login_rigister_btn);
		btnAutoLogin=(Button)findViewById(R.id.login_auto_login);
		loginAction=new ClientLoginAction();
		txtName=(TextView)findViewById(R.id.login_username);
		txtPasswd=(TextView)findViewById(R.id.login_password);
		String value=PreferenceUtil.getStrValue(this,ParamName.USERNAME,"");
		if(StringUtil.checkNotNull(value)){
			txtName.setText(value);
		}
		String autoLogin=PreferenceUtil.getStrValue(this,ParamName.AUTO_LOGIN,"");
		if("1".equals(autoLogin)){
			 btnAutoLogin.setBackgroundResource(R.drawable.login_check);
		}
		btnLogin = (Button) findViewById(R.id.login_login_btn);
		btnFindPwd = (Button) findViewById(R.id.login_findpassword_btn);
		btnLoginCheck = (Button) findViewById(R.id.login_check_btn);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_login_btn:
                if(super.checkValue(true)){     
                	String loginName=getString(R.string.login_name);
                	if(CheckViewUtil.checkCardNotNull(txtName,loginName , dialog)){
                		email=txtName.getText().toString(); 
                	}else{
                		Toast.makeText(LoginActivity.this, "请输入Email  ", Toast.LENGTH_LONG).show();         
                		return ;
                	}
                	if(CheckViewUtil.checkCardNotNull(txtPasswd,loginName , dialog)){
                		password=txtPasswd.getText().toString(); 
                	}else{
                		Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();             
                		return ;
                	}
                	if(isCheck){
                		PreferenceUtil.saveStrValue(this,ParamName.USERNAME,email);
                	}
                	PreferenceUtil.saveStrValue(this,ParamName.PASSWORD,password);
                	loginAction.setEmail(email);
                	loginAction.setPassword(password);   
                	String imei=PreferenceUtil.getStrValue(LoginActivity.this, ParamName.IMEI, "");
            		if(StringUtil.checkIsNull(imei)){
            			imei=new SystemUtil(this).getNewPhoneIMEI();
            			PreferenceUtil.saveStrValue(LoginActivity.this, ParamName.IMEI, imei);
            		}
            		loginAction.setIp(SystemUtil.getIpAddr(this));
            		loginAction.setImei(imei);
                	loginAction.setTransferPayResponse(loginResponse);
                	if (loadingDialog.isShowing()) {
        				loadingDialog.setMessage("正在登录请稍候");
        			} else {
        				loadingDialog.show("正在登录请稍候");
        			}
                	loginAction.transStart();
                	LogUtil.i("DEBUG", "login_login_btn transStart() imei"+imei);  
                //	LogUtil.i("DEBUG", loginAction.respondData.messages.toString());  
                 //	LogUtil.i("DEBUG", loginAction.respondData.userInfo.imei);  
                //	Intent intent=new Intent(this,IndexActivity.class);
        		//    startActivity(intent);	
                	//loadDialog.show();
                }
			break;
		case R.id.login_check_btn:
               if(isCheck){
            	   isCheck=false;
            	   PreferenceUtil.saveStrValue(this, ParamName.USERNAME, "");
            	   btnLoginCheck.setBackgroundResource(R.drawable.login_notcheck);
               }else{
            	   isCheck=true;
            	   PreferenceUtil.saveStrValue(this, ParamName.USERNAME, email);
            	   btnLoginCheck.setBackgroundResource(R.drawable.login_check);
               }
			break;
		case R.id.login_auto_login:
            if(autoIsCheck){
            	autoIsCheck=false;
         	   PreferenceUtil.saveStrValue(this, ParamName.AUTO_LOGIN, "0");
         	   btnAutoLogin.setBackgroundResource(R.drawable.login_notcheck);
            }else{
               autoIsCheck=true;
         	   PreferenceUtil.saveStrValue(this, ParamName.AUTO_LOGIN,"1" );
         	   btnAutoLogin.setBackgroundResource(R.drawable.login_check);
            }
			break;
		case R.id.login_findpassword_btn:
			Intent intent=new Intent(this,FindPwdActivity.class);
		    startActivity(intent);		  
			break;
		case R.id.login_rigister_btn:
		  {
			 /*  clientRegister=new ClientRegisterAction();
			   String imei=PreferenceUtil.getStrValue(LoginActivity.this, ParamName.IMEI, "");
				if(StringUtil.checkIsNull(imei)){
					imei=new SystemUtil(this).getNewPhoneIMEI();
					PreferenceUtil.saveStrValue(LoginActivity.this, ParamName.IMEI, imei);
				}
				clientRegister.setIp(SystemUtil.getIpAddr(this));
				clientRegister.setImei(imei);
				clientRegister.setDelete("1");
				clientRegister.setTransferPayResponse(createUserResponse);
				if (loadingDialog.isShowing()) {
    				loadingDialog.setMessage("正在创建用户，请稍候");
    			} else {
    				loadingDialog.show("正在创建用户请稍候");
    			}
				clientRegister.transStart();*/
		  }
			break;
	
		default:
			break;
		}
	}
   
    
	
	//登录的回调函数
	private TransferPayResponse loginResponse=new TransferPayResponse(){
		@Override
		public void transComplete() {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			} 
		 
			if(loginAction.respondData.historyMoney!=null){
				app.setHistoryMoneys(loginAction.respondData.historyMoney);		
				for(HistoryMoney money:loginAction.respondData.historyMoney){
					if(PlatType.BAIDU.equals(money.platType)){
						 int points=com.baidu.mobads.appoffers.OffersManager.getPoints(LoginActivity.this);
						if(money.amount!=points){
							if(money.amount-points>0){
								 com.baidu.mobads.appoffers.OffersManager.addPoints(LoginActivity.this, money.amount-points);
							 }
//							else{
//								 com.baidu.mobads.appoffers.OffersManager.subPoints(LoginActivity.this, points-money.amount);
//							 }
						}
					}else if(PlatType.YOUMI.equals(money.platType)){
					
						
					}
				}
			}
			LogUtil.i("DEBUG", HttpCodeHelper.RESPONSE_SUCCESS+loginAction.respondData.respCode);  
			if(HttpCodeHelper.RESPONSE_SUCCESS.equals(loginAction.respondData.respCode)){
				//保存
				Toast.makeText(LoginActivity.this, "保存转到主界面...", Toast.LENGTH_LONG).show();
				app.setMessages(loginAction.respondData.messages);
				app.setUserInfo(loginAction.respondData.userInfo); 
				 toMainActivity();
				 
			}else{
				Toast.makeText(LoginActivity.this, loginAction.respondData.respDesc, Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			Toast.makeText(LoginActivity.this, errorInfo, Toast.LENGTH_LONG).show();
		
		}

	};
	public  void toMainActivity(){
		Intent intent=new Intent(LoginActivity.this,IndexActivity.class);
	    startActivity(intent);
	    finish();
	}
	//创建用户的回调函数
		private TransferPayResponse createUserResponse=new TransferPayResponse(){

			@Override
			public void transComplete() {
				if (loadingDialog!=null) {
					loadingDialog.cancel();
				} 
				if(clientRegister.respondData.historyMoney!=null){
					app.setHistoryMoneys(clientRegister.respondData.historyMoney);		
					for(HistoryMoney money:clientRegister.respondData.historyMoney){
						if(PlatType.BAIDU.equals(money.platType)){
							 int points=com.baidu.mobads.appoffers.OffersManager.getPoints(LoginActivity.this);
							if(money.amount!=points){
								if(money.amount-points>0){
									 com.baidu.mobads.appoffers.OffersManager.addPoints(LoginActivity.this, money.amount-points);
								 }
//								else{
//									 com.baidu.mobads.appoffers.OffersManager.subPoints(LoginActivity.this, points-money.amount);
//								 }
							}
						}else if(PlatType.YOUMI.equals(money.platType)){
							//int points=net.youmi.android.offers.PointsManager.getInstance(LoginActivity.this).queryPoints();
						//	if(money.amount-points>0){
							//	net.youmi.android.offers.PointsManager.getInstance(LoginActivity.this).awardPoints(money.amount-points);
							// }
							
						}
					}
				}
				if(HttpCodeHelper.RESPONSE_SUCCESS.equals(clientRegister.respondData.respCode)){
					//保存	
					app.setMessages(clientRegister.respondData.messages);
					app.setUserInfo(clientRegister.respondData.userInfo);
					toMainActivity();
				}else if(HttpCodeHelper.NEED_LOGIN.equals(clientRegister.respondData.respCode)){
			
					Intent intent=new Intent(LoginActivity.this,LoginActivity.class);
				    startActivity(intent);
				    finish();
				}else {			
					Toast.makeText(LoginActivity.this,clientRegister.respondData.respCode+"#"+clientRegister.respondData.respDesc, Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void transFailed(String httpCode, String errorInfo) {
				if (loadingDialog!=null) {
					loadingDialog.cancel();
				}
				Toast.makeText(LoginActivity.this, errorInfo, Toast.LENGTH_LONG).show();	
			}
		};
		
	
}