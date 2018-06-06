package com.jiandande.review.transcation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiandande.review.bean.BalanceChange;
//import com.palmpay.realname.util.TestData;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.ServerUrl;



public class FindBalanceChangeAction extends AbstractTransaction {


	public class FindBalanceRespond {
		public String respCode;
		public String respDesc;
		public ArrayList<BalanceChange> datas;
	}

	private static final String XML_TAG = "FindBalanceChange";

	public FindBalanceChangeAction() {
		url = ServerUrl.findOrder;
		
		respondData = new FindBalanceRespond();
	}

	
	public FindBalanceRespond respondData;

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
					  int count=object.getInt("count");
					  respondData.datas= new ArrayList<BalanceChange>();
					  if(count>0){				
						  JSONArray arrays=object.getJSONArray("datas");
							for(int i=0; i<arrays.length();i++){
								BalanceChange balanceChange=new BalanceChange();
								JSONObject temp=arrays.getJSONObject(i);
								balanceChange.amount=temp.getDouble("amount");
								balanceChange.bid=temp.getInt("bid");
							//	balanceChange.OrderId=temp.getDouble("remainder");OrderId
								balanceChange.OrderId=temp.getDouble("OrderId");
								
								balanceChange.remark=temp.getString("remark");
								balanceChange.submitTime=temp.getLong("submitTime");
								balanceChange.userId=temp.getInt("userId");
								respondData.datas.add(balanceChange);
							}
					  }
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	 
}
