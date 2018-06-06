package com.jiandande.review.transcation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.ServerUrl;



public class FeedBackAction  extends AbstractTransaction {

	class FeedBackSend {
		String content;// 密码		
	}

	public class FeedBackRespond {
		public String respCode;
		public String respDesc;
	}

	private static final String XML_TAG = "LoginAction";

	public FeedBackAction() {
		url = ServerUrl.feedBack;
		sendData = new FeedBackSend();
		respondData = new FeedBackRespond();
	}

	private FeedBackSend sendData;

	public FeedBackRespond respondData;

	@Override
	protected ArrayList<NameValuePair> transPackage() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		super.initParamData(params);
		NameValuePair namePair = new BasicNameValuePair(ParamName.FEED_CONTENT,
				sendData.content);
		params.add(namePair);
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
					  int count=object.getInt("count");
					   if(count>0){
						  
					   }
					}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	public void setContent(String content) {
		this.sendData.content = content;
	}
	
}
