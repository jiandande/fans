
package com.jiandande.review.transcation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiandande.review.bean.HistoryMoney;
import com.jiandande.review.bean.MessageBean;
import com.jiandande.review.bean.UserInfo;
//import com.palmpay.realname.util.TestData;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.ServerUrl;


public class OwnBusinessTaskAction extends AbstractTransaction {

	class ClientSignInSend {
		String luckyscore;// 密码
		String email;// youxiang
		String imei;
		String packagename;
		String ownbusiness;//自营业务
		String pname;
	}

	public class ClientSignInRespond {
		public String respCode;
		public String respDesc;
		public UserInfo userInfo;
		public ArrayList<MessageBean>messages;
		public ArrayList<HistoryMoney>historyMoney;
	}

	private static final String XML_TAG = "ClientLuckyDayAction";

	public OwnBusinessTaskAction() {
		url = ServerUrl.ownbusinesstask;//自营任务积分
		sendData = new ClientSignInSend();
		respondData = new ClientSignInRespond();
	}

	private ClientSignInSend sendData;

	public ClientSignInRespond respondData;

	@Override
	protected ArrayList<NameValuePair> transPackage() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		super.initParamData(params);
		NameValuePair namePair = new BasicNameValuePair(ParamName.EMAIL,
				sendData.email);
		params.add(namePair);
		NameValuePair passwordPair = new BasicNameValuePair(ParamName.LUCKYPOINT,
				sendData.luckyscore);
		params.add(passwordPair);
		NameValuePair imeiPair = new BasicNameValuePair(ParamName.USERID,
				sendData.imei);
		params.add(imeiPair);
		
		NameValuePair packagenamePair = new BasicNameValuePair(ParamName.PACKAGENAME,
				sendData.packagename);
		params.add(packagenamePair);  
		
		
		
		NameValuePair pnamePair = new BasicNameValuePair(ParamName.PNAME,
				sendData.pname);
		params.add(pnamePair);
		
		NameValuePair ownbusinessPair = new BasicNameValuePair(ParamName.OWNBUSINESS,
				sendData.ownbusiness);
		params.add(ownbusinessPair);
		
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
		LogUtil.i("DEBUG", "resloveLoginResult_response"+object.toString()); 
		if (object == null) {
			respondData.respCode = HttpCodeHelper.HTTP_REQUEST_ERROR; 
		} else {
	
			
				respondData.respCode = object.getString(ParamName.RESD_CODE);
				respondData.respDesc = object.getString(ParamName.RESD_DESC);
		

		
			
		}
	}

	public void setEmail(String email) {
		sendData.email = email;
	}

	public void setPassword(String password) {
		sendData.luckyscore = password;
	}

	public void setImei(String imei) {
		sendData.imei = imei;
	}
	
	public void setOwnbusiness(String ownbusiness) {
		sendData.ownbusiness = ownbusiness;
	}

	public void setPackagename(String packagename) {
		sendData.packagename = packagename;
	}
	public void setPname(String pname) {
		sendData.pname = pname;
	}

}
