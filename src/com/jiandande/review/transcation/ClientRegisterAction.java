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
import com.jiandande.review.util.SystemUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.ServerUrl;



public class ClientRegisterAction extends AbstractTransaction {

	class ClientRegisterSend {
		String imei;// 设备唯一号
		String delete="0";//忽略以前的帐号
		String ip;
		//666666
		String jddnumber;
		
	}

	public class ClientRegisterRespond {
		public String respCode;
		public String respDesc;
		public UserInfo userInfo;
		public ArrayList<MessageBean>messages;
		public ArrayList<HistoryMoney>historyMoney;
	}

	private static final String XML_TAG = "ClientRegisterAction";

	public ClientRegisterAction() {
		url = ServerUrl.clientRegister;
		sendData = new ClientRegisterSend();
		respondData = new ClientRegisterRespond();
	}

	private ClientRegisterSend sendData;

	public ClientRegisterRespond respondData;

	@Override
	protected ArrayList<NameValuePair> transPackage() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		super.initParamData(params);
		NameValuePair imeiPair = new BasicNameValuePair(ParamName.USERID,
				sendData.imei);
		params.add(imeiPair);
		NameValuePair deletePair = new BasicNameValuePair(ParamName.PASSWORD,
				"jiandande");
		params.add(deletePair);
		NameValuePair ipPair = new BasicNameValuePair(ParamName.UNAME,
				"AppUser");
		params.add(ipPair);
//666
		NameValuePair jddnumberPair = new BasicNameValuePair(ParamName.JDDNUMBER,
				sendData.jddnumber);
		params.add(jddnumberPair);
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
			//	respondData.respDesc = object.getString(ParamName.RESD_DESC);
				if(HttpCodeHelper.RESPONSE_SUCCESS.equals(respondData.respCode)){
					UserInfo userInfo=new UserInfo();
                    JSONObject objUser=object.getJSONObject("user");
                    userInfo.accountNo= objUser.getString("accountNo");	
                    userInfo.balance= objUser.getDouble("balance");
                    userInfo.settling= objUser.getDouble("money");
                    userInfo.money2= objUser.getDouble("money2");
                    //   userInfo.sumCount= objUser.getDouble("sumCount");
                    userInfo.sumCount= userInfo.balance+ userInfo.settling+userInfo.money2;
                    userInfo.email= objUser.getString("email");	
                    userInfo.checkCount= objUser.getDouble("checkscores")+objUser.getDouble("checkmoney")+objUser.getDouble("checkmoney2");
       
                    respondData.userInfo=userInfo;  
                    try {
						JSONArray objMessage=object.getJSONArray("messages");
						respondData.messages=new ArrayList<MessageBean>();
						for(int i=0;i<objMessage.length();i++){
							JSONObject objMsg=objMessage.getJSONObject(i);
							MessageBean mess=new MessageBean();
							mess.content= objMsg.getString("content");
							mess.msgid= objMsg.getInt("msgid");
							mess.sendUserName= objMsg.getString("sendUserName");
				//			mess.time= objMsg.getLong("time");
							respondData.messages.add(mess);
						}
                     } catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    try {
						JSONArray objMessage=object.getJSONArray("historyMoney");
						respondData.historyMoney=new ArrayList<HistoryMoney>();
						for(int i=0;i<objMessage.length();i++){
							JSONObject objMoney=objMessage.getJSONObject(i);
							HistoryMoney money=new HistoryMoney();
							money.amount= objMoney.getInt("amount");
							money.platType= objMoney.getString("platType");
							money.imei= objMoney.getString("imei");			
							respondData.historyMoney.add(money);
						}
                     } catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	public void setImei(String imei) {
		this.sendData.imei = imei;
	}
	public void setDelete(String delete) {
		this.sendData.delete = delete;
	}
	public void setIp(String ip) {
		sendData.ip = ip;
	}
	
	public void setJddNumber(String jddnumber) {
		sendData.jddnumber = jddnumber;
	}

}
