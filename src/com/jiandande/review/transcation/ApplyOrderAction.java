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



public class ApplyOrderAction extends AbstractTransaction {

	class ApplyOrderSend {

         int type;
		 String amount;
		 String fee;
		 String qqNo;
		 String qqName;
		 String phoneNo;
		 String attributeName;
		 String aliPayno;
		 String aliPayName;
		 double otherAmount;
		 String settingType;
		 String imei;
		
	}

	public class ApplyOrderRespond {
		public String respCode;
		public String respDesc;
		
	}

	private static final String XML_TAG = "LoginAction";

	public ApplyOrderAction() {
		url = ServerUrl.applyOrder;
		sendData = new ApplyOrderSend();
		respondData = new ApplyOrderRespond();
	}

	private ApplyOrderSend sendData;

	public ApplyOrderRespond respondData;

	@Override
	protected ArrayList<NameValuePair> transPackage() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		super.initParamData(params);
		NameValuePair typePair = new BasicNameValuePair(ParamName.TYPE,
				sendData.type+"");
		params.add(typePair);
		NameValuePair amountPair = new BasicNameValuePair(ParamName.AMOUNT,
				sendData.amount);
		params.add(amountPair);
		NameValuePair feePair = new BasicNameValuePair(ParamName.FEE,
				sendData.fee);
		params.add(feePair);
		NameValuePair qqNoPair = new BasicNameValuePair(ParamName.QQNO,
				sendData.qqNo);
		params.add(qqNoPair);
		NameValuePair qqNamePair = new BasicNameValuePair(ParamName.QQ_NAME,
				sendData.qqName);
		params.add(qqNamePair);
		NameValuePair phoneNoPair = new BasicNameValuePair(ParamName.PHONE_NO,
				sendData.phoneNo);
		params.add(phoneNoPair);
		NameValuePair attributePair = new BasicNameValuePair(ParamName.ATTRBUTE,
				sendData.attributeName);
		params.add(attributePair);
		NameValuePair aliPaynoPair = new BasicNameValuePair(ParamName.ALIPAYNO,
				sendData.aliPayno);
		params.add(aliPaynoPair);
		NameValuePair aliPayNamePair = new BasicNameValuePair(ParamName.ALIPAYNAME,
				sendData.aliPayName);
		params.add(aliPayNamePair);
		
		NameValuePair settingTypePair = new BasicNameValuePair("settype",
				sendData.settingType);
		params.add(settingTypePair);
		
		NameValuePair otherAmountPair = new BasicNameValuePair("other_amount",
				sendData.otherAmount+"");
		params.add(otherAmountPair);
		
		NameValuePair imeiPair = new BasicNameValuePair(ParamName.IMEI,
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
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	public void setType(int type) {
		this.sendData.type = type;
	}
	public void setAmount(String amount) {
		this.sendData.amount = amount;
	}
	public void setFee(String fee) {
		this.sendData.fee = fee;
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
	public void setAttributeName(String attributeName) {
		this.sendData.attributeName = attributeName;
	}
	public void setAliPayno(String aliPayno) {
		this.sendData.aliPayno = aliPayno;
	}
	public void setAliPayName(String aliPayName) {
		this.sendData.aliPayName = aliPayName;
	}

	public void setOtherAmount(double otherAmount) {
		this.sendData.otherAmount = otherAmount;
	}

	public void setSettingType(String settingType) {
		this.sendData.settingType = settingType;
	}
	
	public void setImei(String imei) {
		sendData.imei = imei;
	}


}
