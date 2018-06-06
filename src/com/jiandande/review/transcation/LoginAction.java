package com.jiandande.review.transcation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

//import com.palmpay.realname.util.TestData;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.ServerUrl;

/**
 * @author shimingzheng
 * @version 创建时间：2013-12-18 下午4:49:26 说明：
 */

public class LoginAction extends AbstractTransaction {

	class LoginActionSend {
		String password;// 密码
		String loginName;// 登录名或者手机号码
		String ip;
	}

	public class LoginActionRespond {
		public String respCode;
		public String respDesc;
		public String custId;
		public String realName;
		public String phoneNum;
		public String hxMerchantId;
		public String currentPsam;
		public String sessionId;// 会话id
		public String status;// 状态
		public String accountStatus;
		public String authStatus;
		public String settleAccountType;
	}

	private static final String XML_TAG = "LoginAction";

	public LoginAction() {
		url = ServerUrl.clientLogin;
		sendData = new LoginActionSend();
		respondData = new LoginActionRespond();
	}

	private LoginActionSend sendData;

	public LoginActionRespond respondData;

	@Override
	protected ArrayList<NameValuePair> transPackage() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		super.initParamData(params);
		NameValuePair namePair = new BasicNameValuePair(ParamName.USERNAME,
				sendData.loginName);
		params.add(namePair);
		NameValuePair passwordPair = new BasicNameValuePair(ParamName.PASSWORD,
				sendData.password);
		params.add(passwordPair);
		NameValuePair ipPair = new BasicNameValuePair("ip",
				sendData.ip);
		params.add(ipPair);
		
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
				respondData.respCode = object.getString(ParamName.RESD_CODE);
				respondData.respDesc = object.getString(ParamName.RESD_DESC);
				if(HttpCodeHelper.RESPONSE_SUCCESS.equals(respondData.respCode)){
				respondData.custId = object.getString("custId");
				respondData.phoneNum = object.getString("phoneNum");
				respondData.realName = object.getString("realName");
				respondData.sessionId = object.getString("sessionId");
				respondData.status = object.getString("status");
				respondData.accountStatus = object.getString("accountStatus");
				respondData.authStatus = object.getString("authStatus");
				respondData.currentPsam = object.getString("currentPsam");
				respondData.hxMerchantId = object.getString("hxMerchantId");
				respondData.settleAccountType = object.getString("settleAccountType");
				
				
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void setUserName(String userName) {
		sendData.loginName = userName;
	}

	public void setPassword(String password) {
		sendData.password = password;
	}

	public void setIp(String ip) {
		sendData.ip = ip;
	}

}
