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

public class FindBackPasswordAction extends AbstractTransaction {

	class FindBackPasswordSend {
		
		String email;
		String accountNo;
				
	}

	public class FindBackPasswordRespond {
		public String respCode;
		public String respDesc;
		
	}

	private static final String XML_TAG = "LoginAction";

	public FindBackPasswordAction() {
		url = ServerUrl.findBackPassword;
		sendData = new FindBackPasswordSend();
		respondData = new FindBackPasswordRespond();
	}

	private FindBackPasswordSend sendData;

	public FindBackPasswordRespond respondData;

	@Override
	protected ArrayList<NameValuePair> transPackage() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		super.initParamData(params);
		NameValuePair emailPair = new BasicNameValuePair(ParamName.EMAIL,
				sendData.email);
		params.add(emailPair);
		NameValuePair accountPair = new BasicNameValuePair("account_no",
				sendData.accountNo);
		params.add(accountPair);
//		NameValuePair aliPayNoPair = new BasicNameValuePair(ParamName.ALIPAYNO,
//				sendData.aliPayNo);
//		params.add(aliPayNoPair);
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
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void setEmail(String email) {
		this.sendData.email = email;
	}
	public void setAccountNo(String accountNo) {
		this.sendData.accountNo = accountNo;
	}
	 
	 
}
