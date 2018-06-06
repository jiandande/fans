package com.jiandande.review.transcation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import com.jiandande.review.bean.VersionModel;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.Constants;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.ServerUrl;

public class CheckVersionAction   extends AbstractTransaction{
	public static final int NO_NEW_VERSION = 0;
	public static final int SELECT_VERSION_UPDATE = 1;
	public static final int MAST_UPDATE_VERSION = 2;

	public class CheckVersionRespond {
		public String respCode;
		public String respDesc;
		public VersionModel model;
		
	}

	private static final String XML_TAG = "CheckVersionAction";

	public CheckVersionAction() {
		url = ServerUrl.checkVersion;
		respondData = new CheckVersionRespond();
	}


	public CheckVersionRespond respondData;

	@Override
	protected ArrayList<NameValuePair> transPackage() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		super.initParamData(params);
		NameValuePair appNamePair = new BasicNameValuePair(ParamName.APPNAME,
				Constants.APPNAME);
		params.add(appNamePair);
		NameValuePair typePair = new BasicNameValuePair(ParamName.TYPE,"1");
		params.add(typePair);

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
		LogUtil.i(XML_TAG, "resloveLoginResult:" + response);
		JSONObject object = super.processResult(response);
	
		if (object == null) {
			respondData.respCode = HttpCodeHelper.HTTP_REQUEST_ERROR;
		} else {
			try {
			
				respondData.respCode = object.getString(ParamName.RESD_CODE);
			//	respondData.respDesc = object.getString(ParamName.RESD_DESC);	
				LogUtil.i(XML_TAG, "respondData.respCode :" + respondData.respCode );
			   if(HttpCodeHelper.RESPONSE_SUCCESS.equals(respondData.respCode)){
					LogUtil.i(XML_TAG, "object:" );
				   object=object.getJSONObject("model");
					LogUtil.i(XML_TAG, "model:" );
				   VersionModel versionModel=new VersionModel();
			
				   versionModel.type =object.getInt("type");//1biaoshi android
				   versionModel.appName=object.getString("appName");;//		 	
				   versionModel.versionCode=object.getInt("versionCode");;		 	
				//   versionModel.unUpdateCode=object.getString("unUpdateCode");
				   versionModel.needUpdate=object.getInt("needUpdate");	
				//   versionModel.versionName=object.getString("versionName");
				   versionModel.path=object.getString("path");;		 	
				   versionModel.msg=object.getString("msg");
					LogUtil.i(XML_TAG, "versionModel:" );
				   respondData.model=versionModel;
				   LogUtil.i(XML_TAG, " respondData.model=versionModel;:" );
			   }
			   
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
