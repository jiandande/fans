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



public class CanDownLoadAction extends AbstractTransaction {

	public class CanDownLoadSend {

		String ip;
		String imei;
		public String index;
	}

	public class CanDownLoadRespond {
		public String respCode;
		public String respDesc;
		public boolean can;
	}

	private static final String XML_TAG = "CanDownLoad";

	public CanDownLoadAction() {
		url = ServerUrl.can_download;
		sendData = new CanDownLoadSend();
		respondData = new CanDownLoadRespond();
	}

	public CanDownLoadSend sendData;

	public CanDownLoadRespond respondData;

	@Override
	protected ArrayList<NameValuePair> transPackage() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		super.initParamData(params);
		NameValuePair ipPair = new BasicNameValuePair("ip",
				sendData.ip);
		params.add(ipPair);
		NameValuePair imeiPair = new BasicNameValuePair(ParamName.IMEI,
				sendData.imei);
		params.add(imeiPair);
		NameValuePair indexPair = new BasicNameValuePair("index",
				sendData.index);
		params.add(indexPair);
		
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
					respondData.can = object.getBoolean("can");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void setIp(String ip) {
		sendData.ip = ip;
	}
	
	public void setImei(String imei) {
		this.sendData.imei = imei;
	}
	public void setIndex(String index) {
		this.sendData.index = index;
	}
}
