package com.jiandande.review.activity;



import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.youmi.android.AdManager;

import com.baidu.android.pushservice.BasicPushNotificationBuilder;
import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.push.example.Utils;
import com.jiandande.money.R;
import com.jiandande.review.bean.HistoryMoney;
import com.jiandande.review.dialog.CheckVersionThread;
import com.jiandande.review.transcation.ClientLoginAction;
import com.jiandande.review.transcation.ClientRegisterAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.StringUtil;
import com.jiandande.review.util.SystemUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.PlatType;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.widget.Toast;
public class WelComeActivity extends AbstractActivity {
	
	 ClientRegisterAction clientRegister;
	 ClientLoginAction clientLogin;
	 CheckVersionThread checkVersionThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

    	setContentView(R.layout.welcome);
        // 初始化接口，应用启动的时候调用
        // 参数：appId, appSecret, false:不开启调试模式
    
    	
    	
    	
    	
    	
    	
    	String value=PreferenceUtil.getStrValue(this,ParamName.CHECK_VERSION,"");
		String checkValue=createCheckValue();
    
		initView();
	  //  Toast.makeText(WelComeActivity.this, "initView();", Toast.LENGTH_LONG).show();	
		if(!value.equals(checkValue)){
		//	Toast.makeText(WelComeActivity.this, " checkFinishVersion()2;", Toast.LENGTH_LONG).show();
			PreferenceUtil.saveStrValue(this,ParamName.CHECK_VERSION,checkValue);
			checkVersionThread = new CheckVersionThread(this, dialog);
			checkVersionThread.setShowLoadDialog(false);
			checkVersionThread.run();
		//2	Toast.makeText(WelComeActivity.this, " checkFinishVersion()3;", Toast.LENGTH_LONG).show();
		}else{
			//Toast.makeText(WelComeActivity.this, " checkFinishVersion();;", Toast.LENGTH_LONG).show();
			 checkFinishVersion();
		}
	
    	  //面
	
    }
    
    private String createCheckValue() {
		
		return StringUtil.getCurDate();
	}
    @Override
    protected void initView() {
    	super.initView();
    }
    

    

	public void checkFinishVersion() {
		String password=PreferenceUtil.getStrValue(this,ParamName.PASSWORD,"");
		String email=PreferenceUtil.getStrValue(this,ParamName.USERNAME,"");
		String imei=PreferenceUtil.getStrValue(WelComeActivity.this, ParamName.IMEI, "");

		if(StringUtil.checkIsNull(imei)){
			imei=new SystemUtil(this).getNewPhoneIMEI();
		
			PreferenceUtil.saveStrValue(WelComeActivity.this, ParamName.IMEI, imei);
		}
		if(StringUtil.checkNotNull(password)&&StringUtil.checkNotNull(email)){
			String autoLogin=PreferenceUtil.getStrValue(this,ParamName.AUTO_LOGIN,"1");
			if(autoLogin.equals("1")){
			//	Toast.makeText(WelComeActivity.this, "autoLogin", Toast.LENGTH_LONG).show();	
				clientLogin=new ClientLoginAction();
				clientLogin.setPassword(password);
				clientLogin.setEmail(email);
				clientLogin.setImei(imei);
				clientLogin.setIp(SystemUtil.getIpAddr(this));
				clientLogin.setTransferPayResponse(loginResPond);
				if (loadingDialog.isShowing()) {
					loadingDialog.setMessage("正在登录，请稍候");
				} else {
					loadingDialog.show("正在登录，请稍候");
				}
				clientLogin.transStart();
			}else{
			//	Toast.makeText(WelComeActivity.this, "startActivity", Toast.LENGTH_LONG).show();
				Intent intent=new Intent(WelComeActivity.this,LoginActivity.class);
			    startActivity(intent);
			//	Intent intent2=new Intent(WelComeActivity.this,TwopxActivity.class);
			//    startActivity(intent2);
			    finish();
			}
			
		}else{
			//Toast.makeText(WelComeActivity.this, "checkNotNull(email)_password,imei="+imei, Toast.LENGTH_LONG).show();
		
			clientRegister=new ClientRegisterAction();
			clientRegister.setImei(imei);
			clientRegister.setIp(SystemUtil.getIpAddr(this));
			String jddnumber=SystemUtil.getPhoneIMEI2(this);
			if(jddnumber.equals("")){jddnumber=SystemUtil.getPhoneIMEI2(this);}
			clientRegister.setJddNumber(jddnumber);
			clientRegister.setTransferPayResponse(createUserResponse);
			if (loadingDialog.isShowing()) {
				loadingDialog.setMessage("正在创建用户，请稍候");
			} else {
				loadingDialog.show("正在创建用户请稍候");
			}
			clientRegister.transStart();
			/*
			Intent intent=new Intent(WelComeActivity.this,LoginActivity.class);
		    startActivity(intent);
		    finish();*/
		}	
		

	}
	//登录的回调函数
		private TransferPayResponse loginResPond=new TransferPayResponse(){

			@Override
			public void transComplete() {
				if (loadingDialog!=null) {
					loadingDialog.cancel();
				} 
				if(clientLogin.respondData.historyMoney!=null){
					app.setHistoryMoneys(clientLogin.respondData.historyMoney);
						for(HistoryMoney money:clientLogin.respondData.historyMoney){
							if(PlatType.BAIDU.equals(money.platType)){
								 int points=com.baidu.mobads.appoffers.OffersManager.getPoints(WelComeActivity.this);
								if(money.amount!=points){				
									 if(money.amount-points>0){
										 com.baidu.mobads.appoffers.OffersManager.addPoints(WelComeActivity.this, money.amount-points);
									 }
									 //else{
//										 com.baidu.mobads.appoffers.OffersManager.subPoints(WelComeActivity.this, points-money.amount);
//									 }
								}
							}else if(PlatType.YOUMI.equals(money.platType)){
								/*int points=net.youmi.android.offers.PointsManager.getInstance(WelComeActivity.this).queryPoints();
								if(money.amount-points>0){
									net.youmi.android.offers.PointsManager.getInstance(WelComeActivity.this).awardPoints(money.amount-points);
								 }*/
								
							}
						}
					}
	
				if(HttpCodeHelper.RESPONSE_SUCCESS.equals(clientLogin.respondData.respCode)){
					//保存
					app.setMessages(clientLogin.respondData.messages);
					app.setUserInfo(clientLogin.respondData.userInfo);
					toMainActivity();
				}else if(HttpCodeHelper.PASSWORD_ERROR.equals(clientLogin.respondData.respCode)){
				
					Intent intent=new Intent(WelComeActivity.this,LoginActivity.class);
				    startActivity(intent);
				    finish();
				}else{
					Toast.makeText(WelComeActivity.this,clientLogin.respondData.respCode+"#"+clientLogin.respondData.respDesc, Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void transFailed(String httpCode, String errorInfo) {
				if (loadingDialog!=null) {
					loadingDialog.cancel();
				}
				Toast.makeText(WelComeActivity.this, errorInfo, Toast.LENGTH_LONG).show();	
			}
			
		};
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
						 int points=com.baidu.mobads.appoffers.OffersManager.getPoints(WelComeActivity.this);
						if(money.amount!=points){
							if(money.amount-points>0){
								 com.baidu.mobads.appoffers.OffersManager.addPoints(WelComeActivity.this, money.amount-points);
							 }
//							else{
//								 com.baidu.mobads.appoffers.OffersManager.subPoints(WelComeActivity.this, points-money.amount);
//							 }
						}
					}else if(PlatType.YOUMI.equals(money.platType)){
					/*	int points=net.youmi.android.offers.PointsManager.getInstance(WelComeActivity.this).queryPoints();
						if(money.amount!=points){
							if(money.amount-points>0){
								net.youmi.android.offers.PointsManager.getInstance(WelComeActivity.this).awardPoints(money.amount-points);
							 }
						}*/
						
					}
				}
			}
			if(HttpCodeHelper.RESPONSE_SUCCESS.equals(clientRegister.respondData.respCode)){
				//保存			
			//	Toast.makeText(WelComeActivity.this, "createUserResponse__RESPONSE_SUCCESS", Toast.LENGTH_LONG).show();	
				PreferenceUtil.saveStrValue(WelComeActivity.this, ParamName.USERNAME, "666");
			//	PreferenceUtil.saveStrValue(WelComeActivity.this, ParamName.USERNAME, imei);
				PreferenceUtil.saveStrValue(WelComeActivity.this, ParamName.PASSWORD, "jiandande");
				app.setUserInfo(clientRegister.respondData.userInfo);
				app.setMessages(clientRegister.respondData.messages);
				toMainActivity();
			}else if(HttpCodeHelper.NEED_LOGIN.equals(clientRegister.respondData.respCode)){
				PreferenceUtil.saveStrValue(WelComeActivity.this, ParamName.USERNAME, "666");
				PreferenceUtil.saveStrValue(WelComeActivity.this, ParamName.PASSWORD, "jiandande");
				Intent intent=new Intent(WelComeActivity.this,LoginActivity.class);
			    startActivity(intent);
			    finish();
			}else {			
				Toast.makeText(WelComeActivity.this,clientRegister.respondData.respCode+"#"+clientRegister.respondData.respDesc, Toast.LENGTH_LONG).show();
			}
		
		}

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			Toast.makeText(WelComeActivity.this, errorInfo, Toast.LENGTH_LONG).show();	
		}
	};
	
	public  void toMainActivity(){
		Intent intent=new Intent(WelComeActivity.this,IndexActivity.class);
	    startActivity(intent);
	    finish();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
