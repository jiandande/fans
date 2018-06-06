package com.jiandande.review.transcation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.widget.Toast;

import com.jiandande.review.activity.ExchargeCenterActivity;
import com.jiandande.review.activity.WelComeActivity;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.bean.UserMoreInfo;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.StringUtil;
import com.jiandande.review.util.SystemUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.ServerUrl;


public class FindUserInfoAction extends AbstractTransaction {
	
	 String imei;// 设备唯一号

	public class FindUserInfoRespond {
		public String respCode;
		public String respDesc;
		public UserInfo userInfo;
	
		
	}

	private static final String XML_TAG = "LoginAction";

	public FindUserInfoAction() {
		url = ServerUrl.findUserInfo;
		
		respondData = new FindUserInfoRespond();
	}

	

	public FindUserInfoRespond respondData;

	@Override
	protected ArrayList<NameValuePair> transPackage() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		super.initParamData(params);
	//	LogUtil.i("DEBUG", "PreferenceUtil.getStrValue(null, ParamName.IMEI strart");  
		NameValuePair imeiPair = new BasicNameValuePair(ParamName.USERID,
				imei);
	//	LogUtil.i("DEBUG", "PreferenceUtil.getStrValue(null, ParamName.IMEI end imei="+imei);  
		params.add(imeiPair);
		return params;
	}

	@Override
	protected boolean transParse(InputStream inputStream) {
		LogUtil.i(XML_TAG, "开始解析inputStream");
		/**
		 * 采用pull解析方式：XmlPullParser采用驱动解析，占用内存少，无需一次性加载数据
		 */
		String json = null;
		if (inputStream == null) {
			//json = TestData.creatLoginData();
			 respondData.respCode = HttpCodeHelper.ERROR;
			 return false;
		} else {
			json = readInputStream(inputStream);
		}

		LogUtil.i(XML_TAG, "json:" + json);
		try {
			resloveLoginResult(json);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	void resloveLoginResult(String response) throws JSONException, IOException {
		JSONObject object = super.processResult(response);
		if (object == null) {
			respondData.respCode = HttpCodeHelper.HTTP_REQUEST_ERROR;
		} else {
			try {
				LogUtil.i("DEBUG", "resloveLoginResult FindUserInfoRespond");  
				respondData.respCode = object.getString(ParamName.RESD_CODE);
			//	respondData.respDesc = object.getString(ParamName.RESD_DESC);
				if(HttpCodeHelper.RESPONSE_SUCCESS.equals(respondData.respCode)){
					 UserInfo userInfo=new UserInfo();
					 /* UserMoreInfo moreinfo=new UserMoreInfo();
                     try {
						JSONObject moreInfoObject=object.getJSONObject("moreinfo");
						 moreinfo.alipayName= moreInfoObject.getString("alipayName");	
						 moreinfo.alipayNo= moreInfoObject.getString("alipayNo");
						 moreinfo.qqName= moreInfoObject.getString("qqName");	
						 moreinfo.qqNo= moreInfoObject.getString("qqNo");	
						 moreinfo.phoneNo= moreInfoObject.getString("phoneNo");
						 moreinfo.attribute= moreInfoObject.getString("attribute");	
						 userInfo.userMoreInfo=moreinfo;
					} catch (Exception e) {				
						
					} 
                     */
					// JSONObject userObject=object.getJSONObject("userInfo");
					/* JSONObject userObject=object.getJSONObject("user");
                   //  userInfo.uid= userObject.getInt("uid");	
                   // 
                   //  userInfo.imei= userObject.getString("imei");	
                     userInfo.accountNo= userObject.getString("accountNo");	
                     userInfo.balance= userObject.getDouble("balance");
                 //    userInfo.settled= userObject.getDouble("settled");	
                 //    userInfo.settling= userObject.getDouble("settling");	
                   //  userInfo.aplyCount= userObject.getInt("aplyCount");	
                     userInfo.sumCount= userObject.getDouble("sumCount");	
                     userInfo.email= userObject.getString("email");	
                   //  userInfo.haveFilter= userObject.getBoolean("haveFilter");*/
					 
			
                     JSONObject objUser=object.getJSONObject("user");
                 	LogUtil.i("DEBUG", "object.getJSONObject user"); 
                     userInfo.accountNo= objUser.getString("accountNo");	
                     userInfo.balance= objUser.getDouble("balance");
                     userInfo.settling= objUser.getDouble("money");
                     //   userInfo.sumCount= objUser.getDouble("sumCount");
                     userInfo.money2= objUser.getDouble("money2");
                     userInfo.sumCount= userInfo.balance+ userInfo.settling+userInfo.money2;
                     userInfo.email= objUser.getString("email");	
                     userInfo.checkCount= objUser.getDouble("checkscores")+objUser.getDouble("checkmoney")+objUser.getDouble("checkmoney2");
                     userInfo.bindStatus= objUser.getInt("bindStatus");
                     userInfo.vip= objUser.getInt("vip");
                
                     respondData.userInfo=userInfo;  
                 	LogUtil.i("DEBUG", "resloveLoginResult FindUserInfoRespond"+respondData.respCode);  
                    
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	public void setImei(String imei) {
		this.imei = imei;
	}


}
