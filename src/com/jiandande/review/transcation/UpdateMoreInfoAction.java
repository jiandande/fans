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

public class UpdateMoreInfoAction extends AbstractTransaction {

	class UpdateMoreInfoSend {
		 String qqNo;
		 String qqName;
		 String phoneNo;
		 String attrbute;
		 String aliPayNo;
		 String password;
		 String alipayName; 
	}

	public class UpdateMoreInfoRespond {
		public String respCode;
		public String respDesc;
		
	}

	private static final String XML_TAG = "UpdateMoreInfoAction";

	public UpdateMoreInfoAction() {
		url = ServerUrl.updateMoreInform;
		sendData = new UpdateMoreInfoSend();
		respondData = new UpdateMoreInfoRespond();
	}

	private UpdateMoreInfoSend sendData;

	public UpdateMoreInfoRespond respondData;

	@Override
	protected ArrayList<NameValuePair> transPackage() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		super.initParamData(params);
		NameValuePair qqNoPair = new BasicNameValuePair(ParamName.QQNO,
				sendData.qqNo);
		params.add(qqNoPair);
		NameValuePair qqNamePair = new BasicNameValuePair(ParamName.QQ_NAME,
				sendData.qqName);
		params.add(qqNamePair);
		NameValuePair phoneNoPair = new BasicNameValuePair(ParamName.PHONE_NO,
				sendData.phoneNo);
		params.add(phoneNoPair);
		NameValuePair attrbutePair = new BasicNameValuePair(ParamName.ATTRBUTE,
				sendData.attrbute);
		params.add(attrbutePair);
		NameValuePair aliPayNoPair = new BasicNameValuePair(ParamName.ALIPAYNO,
				sendData.aliPayNo);
		params.add(aliPayNoPair);
		NameValuePair alipayNamePair = new BasicNameValuePair(ParamName.ALIPAYNAME,
				sendData.alipayName);
		params.add(alipayNamePair);
		NameValuePair passwordPair = new BasicNameValuePair(ParamName.PASSWORD,
				sendData.password);
		params.add(passwordPair);
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
				
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void setQqNo(String qqNo) {
		this.sendData.qqNo = qqNo;
	}
	public void setQqName(String qqName) {
		this.sendData.qqName = qqName;
	}
	public void setPhoneNo(String phoneNo) {
		this.sendData.phoneNo = phoneNo;
	}
	public void setAttrbute(String attrbute) {
		this.sendData.attrbute = attrbute;
	}
	public void setAliPayNo(String aliPayNo) {
		this.sendData.aliPayNo = aliPayNo;
	}
	public void setPassword(String password) {
		this.sendData.password = password;
	}
	public void setAlipayName(String alipayName) {
		this.sendData.alipayName = alipayName;
	}
	
}
