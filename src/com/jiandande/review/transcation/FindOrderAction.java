package com.jiandande.review.transcation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import com.jiandande.review.activity.ExchangeListActivity;
import com.jiandande.review.bean.ExchangeBean;
import com.jiandande.review.bean.Pagination;
//import com.palmpay.realname.util.TestData;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.ServerUrl;



public class FindOrderAction extends AbstractTransaction {

	class FindOrderSend {
		public String status;
		public String startDate;
		public String endDate;
		public String type;
		public String myself;
		
		public int page;
		public String mid;
		public String buyid;
	}

	public class FindOrderRespond {
		public String respCode;
		public String respDesc;	
		public Pagination<ExchangeBean> pagination;
	}

	private static final String XML_TAG = "FindOrderAction";

	
	
	public FindOrderAction() {
		url = ServerUrl.findOrder;
		sendData = new FindOrderSend();
		respondData = new FindOrderRespond();
	}

	private FindOrderSend sendData;

	public FindOrderRespond respondData;

	@Override
	protected ArrayList<NameValuePair> transPackage() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		super.initParamData(params);
		NameValuePair statusPair = new BasicNameValuePair(ParamName.STATUS,
				sendData.status);
		params.add(statusPair);
		NameValuePair startDatePair = new BasicNameValuePair(ParamName.STATE_DATE,
				sendData.startDate);
		params.add(startDatePair);
		NameValuePair endDatePair = new BasicNameValuePair(ParamName.END_DATE,
				sendData.endDate);
		params.add(endDatePair);	
		NameValuePair typePair = new BasicNameValuePair(ParamName.TYPE,
				sendData.type);
		params.add(typePair);
		NameValuePair myselfPair = new BasicNameValuePair(ParamName.MYSELF,
				sendData.myself);
		params.add(myselfPair);
		NameValuePair pagePair = new BasicNameValuePair(ParamName.PAGE,
				sendData.page+"");
		params.add(pagePair);
		NameValuePair midPair = new BasicNameValuePair(ParamName.MID,
				sendData.mid+"");
		params.add(midPair);
		NameValuePair buyidPair = new BasicNameValuePair(ParamName.BUYID,
				sendData.buyid+"");
		params.add(buyidPair);
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
				//respondData.respDesc = object.getString(ParamName.RESD_DESC);
	
			//	LogUtil.i("Debug", respondData.respCode);
				if(HttpCodeHelper.RESPONSE_SUCCESS.equals(respondData.respCode)){
	
					respondData.pagination=new Pagination<ExchangeBean>();
		
					object=object.getJSONObject("pagination");
				
					respondData.pagination.countPage=object.getInt("countPage");
					respondData.pagination.datas=new ArrayList<ExchangeBean>();
			
					JSONArray arrays=object.getJSONArray("datas");
				
					for(int i=0; i<arrays.length();i++){
				
						ExchangeBean bean=new ExchangeBean();
						JSONObject temp=arrays.getJSONObject(i);
				
						bean.orderId=temp.getInt("orderId");
				
						bean.orderType=temp.getInt("orderType");
			
						bean.amount=temp.getDouble("amount");
						bean.status=temp.getInt("status");
						bean.pname=temp.getString("pname");
						bean.submitTime=temp.getLong("submitTime");
						bean.filesize=temp.getString("filesize");
						bean.producturl=temp.getString("producturl");
						bean.appdescription=temp.getString("appdescription");
						bean.packagename=temp.getString("packagename");
						bean.appiconurl=temp.getString("appiconurl");
			
					/*	bean.fee=temp.getDouble("fee");					
						
						bean.lastUpdateTime=temp.getLong("lastUpdateTime");
						bean.submiterId=temp.getInt("submiterId");//申请人
						bean.submiterAccountName=temp.getString("submiterAccountName");//申请人
						bean.phoneNo=temp.getString("phoneNo");
						bean.attribute=temp.getString("attribute");
						bean.qqNo=temp.getString("qqNo");
						bean.qqName=temp.getString("qqName");
						bean.alpayNo=temp.getString("alpayNo");
						bean.alpayName=temp.getString("alpayName"); 
					
						bean.remark=temp.getString("remark");//说明	
						bean.orderNo=temp.getString("orderNo");//	
						bean.setType=temp.getString("settleType");//
						bean.otherAmount=temp.getDouble("otherAmount");//	*/
						respondData.pagination.datas.add(bean);
				
						
					}
				//	LogUtil.i(XML_TAG, "88888888888888888888888888:" );
					respondData.pagination.page=1;//object.getInt("page");
					respondData.pagination.pageSize=object.getInt("pageSize");
					respondData.pagination.total=object.getInt("total");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void setStatus(String status) {
		this.sendData.status = status;
	}
	public void setStartDate(String startDate) {
		this.sendData.startDate = startDate;
	}
	public void setEndDate(String endDate) {
		this.sendData.endDate = endDate;
	}
	public void setType(String type) {
		this.sendData.type = type;
	}
	public void setMyself(String myself) {
		this.sendData.myself = myself;
	}
	
	public void setPage(int page) {
		this.sendData.page = page;
	}
	
	public void setMid(String mid) {
		this.sendData.mid = mid;
	}
	public void setBuyId(String buyid) {
		this.sendData.buyid = buyid;
	}
	
}
