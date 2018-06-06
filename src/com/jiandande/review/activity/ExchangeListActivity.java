package com.jiandande.review.activity;


import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.jiandande.money.R;
import com.jiandande.review.adater.ExChangeOrderAdapter;
import com.jiandande.review.bean.ExchangeBean;
import com.jiandande.review.bean.Pagination;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.transcation.FindOrderAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.StringUtil;
import com.jiandande.review.util.Constants.ParamName;
public class ExchangeListActivity extends AbstractActivity implements OnClickListener{
    
	
	
	FindOrderAction findOrderAction;
	ExChangeOrderAdapter orderAdater;
	ListView lisOrder;

	Pagination<ExchangeBean>pagination;
	
	Button  btnMyself,btnLoadMore;
	LinearLayout lyLoading;
	View footerView;
	int currentPage=1;
	String currentSelf="-1";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myexchangelist);
		initView();
		addEventLister();
	}
	@Override
	protected void initView() {
		super.initView();
		findOrderAction=new FindOrderAction();
		lisOrder=(ListView)findViewById(R.id.lv);
		btnMyself=(Button)findViewById(R.id.myself);
		LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerView = layoutInflater.inflate(R.layout.list_footer, null);
		lisOrder.addFooterView(footerView);
		footerView.setVisibility(View.GONE);
		btnLoadMore=(Button)footerView.findViewById(R.id.list_bottom_btn);
		lyLoading=(LinearLayout)footerView.findViewById(R.id.loading);
		beginSyscMonthOrder(currentSelf,1);
	}
	@Override
	protected void addEventLister(){
		orderAdater = new ExChangeOrderAdapter(                                                
				ExchangeListActivity.this, new ArrayList<ExchangeBean>());                                    
		lisOrder.setAdapter(orderAdater);                                             
		lisOrder.setOnItemClickListener(itemClickListener);   
		btnMyself.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnLoadMore.setOnClickListener(this);
	}
	
	OnItemClickListener itemClickListener =new  OnItemClickListener() {                           
        
        
		@Override                                                                                 
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,                     
				long arg3) {                                                                      
		/*	ExchangeBean temporder =(ExchangeBean) orderAdater.getItem(position);                                         
			Intent intent = new Intent();                                                         
			intent.setClass(ExchangeListActivity.this, ExChangeOrderActivity.class);           
			intent.putExtra(ParamName.ORDER, temporder);                                              
			startActivity(intent);          */                                                      
		}                                                                                         
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			super.onBackPressed();
			finish();
			break;
		case R.id.myself:	
			currentSelf="1";
			beginSyscMonthOrder("1",1);
			break;
		case R.id.list_bottom_btn:	
			lyLoading.setVisibility(View.VISIBLE);
			footerView.setVisibility(View.GONE);
			currentPage=currentPage+1;
			beginSyscMonthOrder(currentSelf,currentPage);
			break;
		}
	}
	

	
	private void beginSyscMonthOrder(String myself,int page) {		
		findOrderAction.setEndDate("");
		findOrderAction.setMyself(myself);
		findOrderAction.setStartDate("");
		findOrderAction.setStatus("");
		findOrderAction.setType("");
		findOrderAction.setPage(page);
		UserInfo user=app.getUserInfo();
		findOrderAction.setMid(user.accountNo);
		findOrderAction.setBuyId("GiftToMoney");
	//	Toast.makeText(ExchangeListActivity.this, user.accountNo, Toast.LENGTH_LONG).show();
		findOrderAction.setTransferPayResponse(findOrderRespond);
		if (loadingDialog.isShowing()) {
			loadingDialog.setMessage("正在查询订单，请稍候");
		} else {
			loadingDialog.show("正在查询订单，请稍候");
		}
		findOrderAction.transStart();
	}
	
	TransferPayResponse findOrderRespond = new TransferPayResponse() {

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			 if(loadingDialog!=null){
				 loadingDialog.cancel();
			 }
			Toast.makeText(ExchangeListActivity.this, httpCode + "#" + errorInfo,
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void transComplete() {
			 if(loadingDialog!=null){
				 loadingDialog.cancel();
			 }
			if (HttpCodeHelper.RESPONSE_SUCCESS
					.equals(findOrderAction.respondData.respCode)) {
				 pagination=findOrderAction.respondData.pagination;
				 if(currentPage>1){
					 orderAdater.setDatas(pagination.datas, true);
				 }else{
					 orderAdater.setDatas(pagination.datas, false);
				 }
				 orderAdater.notifyDataSetChanged();
				 if(pagination.page<pagination.countPage){
					 footerView.setVisibility(View.VISIBLE);
					 lyLoading.setVisibility(View.GONE);
				 }else{
					 footerView.setVisibility(View.GONE);
				 }
				 
			} else {
				Toast.makeText(ExchangeListActivity.this,
						findOrderAction.respondData.respDesc, Toast.LENGTH_LONG)
						.show();
			}
		}

	};
}
