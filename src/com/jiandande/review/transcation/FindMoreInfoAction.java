package com.jiandande.review.transcation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiandande.review.bean.UserMoreInfo;
//import com.palmpay.realname.util.TestData;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.ServerUrl;

/**
 * @author shimingzheng
 * @version 创建时间：2013-12-18 下午4:49:26 说明：
 */

public class FindMoreInfoAction extends AbstractTransaction {

	class FindMoreInfoSend {
		
	}

	public class FindMoreInfoRespond {
		public String respCode;
		public String respDesc;	
		public UserMoreInfo userMoreInfo;
		  
	}

	private static final String XML_TAG = "FindMoreInfoAction";

	public FindMoreInfoAction() {
		url = ServerUrl.findBindInfo;
		respondData = new FindMoreInfoRespond();
	}


	public FindMoreInfoRespond respondData;

	@Override
	protected ArrayList<NameValuePair> transPackage() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		super.initParamData(params);
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
					object=object.getJSONObject("userMoreInfo");
					UserMoreInfo userMoreInfo=new UserMoreInfo();
					userMoreInfo.phoneNo= object.getString("phoneNo");		 
					userMoreInfo.attribute= object.getString("attribute");	  
					userMoreInfo.qqNo= object.getString("qqNo");	  
					userMoreInfo.qqName= object.getString("qqName");	  
					userMoreInfo.alipayNo= object.getString("alipayNo");
					userMoreInfo.alipayName= object.getString("alipayName");	  
					respondData.userMoreInfo=userMoreInfo;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}



}
