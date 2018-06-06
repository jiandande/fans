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



public class AddMoneyAction extends AbstractTransaction {

	class AddMoneySend {
		int money;// 
		String source;//
		String platType;//
		String imei;
	}

	public class AddMoneyRespond {
		public String respCode;
		public String respDesc;
		public double amount;
		
	}

	private static final String XML_TAG = "AddMoneyAction";

	public AddMoneyAction() {
		url = ServerUrl.addMoney;
		sendData = new AddMoneySend();
		respondData = new AddMoneyRespond();
	}

	private AddMoneySend sendData;

	public AddMoneyRespond respondData;

	@Override
	protected ArrayList<NameValuePair> transPackage() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		super.initParamData(params);
		NameValuePair moneyPair = new BasicNameValuePair(ParamName.AMOUNT,
				sendData.money+"");
		params.add(moneyPair);
		NameValuePair sourcePair = new BasicNameValuePair(ParamName.SOURCE,
				sendData.platType+"##"+sendData.source);
		params.add(sourcePair);
		
		NameValuePair platPair = new BasicNameValuePair("platType",
				sendData.platType);
		params.add(platPair);
		
		NameValuePair imeiPair = new BasicNameValuePair(ParamName.USERID,
				sendData.imei);
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
				respondData.respCode = object.getString(ParamName.RESD_CODE);
				respondData.respDesc = object.getString(ParamName.RESD_DESC);
				if(HttpCodeHelper.RESPONSE_SUCCESS.equals(respondData.respCode)){
				//	respondData.amount=object.getDouble("amount");
				
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setMoney(int money) {
		this.sendData.money = money;
	}
	
	public void setSource(String source) {
		this.sendData.source = source;
	}
	
	public void setPlatType(String platType) {
		this.sendData.platType = platType;
	}
	public String getPlatType() {
		return this.sendData.platType;
	}

	
	public void setImei(String imei) {
		
		this.sendData.imei = imei;
	}
	
	

}
