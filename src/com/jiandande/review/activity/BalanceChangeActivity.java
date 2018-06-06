package com.jiandande.review.activity;


import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.jiandande.money.R;
import com.jiandande.review.adater.BalanceChangeAdapter;
import com.jiandande.review.adater.ExChangeOrderAdapter;
import com.jiandande.review.bean.BalanceChange;
import com.jiandande.review.bean.ExchangeBean;
import com.jiandande.review.bean.Pagination;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.transcation.FindBalanceChangeAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transcation.FindOrderAction;
import com.jiandande.review.transfer.HttpCodeHelper;

public class BalanceChangeActivity extends AbstractActivity implements OnClickListener{
    
	
	Pagination<ExchangeBean>pagination;
	FindOrderAction findbalanceAction;
//	BalanceChangeAdapter orderAdater;
	ExChangeOrderAdapter orderAdater;
	ListView lisOrder;
	int currentPage=1;
	String currentSelf="-1";
	ArrayList<BalanceChange>datas;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.balancelist);
		initView();
		addEventLister();
	}
	
	@Override
	protected void initView() {
		super.initView();
		
		
		findbalanceAction=new FindOrderAction();
		
		
		lisOrder=(ListView)findViewById(R.id.lv);
		currentPage=currentPage+1;
		beginSyscMonthOrder(currentSelf,currentPage);
	}
	@Override
	protected void addEventLister(){
		//orderAdater = new BalanceChangeAdapter(                                                
			//	BalanceChangeActivity.this, new ArrayList<BalanceChange>());   
		orderAdater = new ExChangeOrderAdapter(                                                
				BalanceChangeActivity.this, new ArrayList<ExchangeBean>());                                    

		lisOrder.setAdapter(orderAdater);                                               
		btnBack.setOnClickListener(this);
	}
	

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			super.onBackPressed();
			finish();
			break;
		}
	}
	

	
	private void beginSyscMonthOrder(String myself,int page) {		
		
		findbalanceAction.setEndDate("");
		findbalanceAction.setMyself(myself);
		findbalanceAction.setStartDate("");
		findbalanceAction.setStatus("");
		findbalanceAction.setType("");
		findbalanceAction.setPage(page);
		UserInfo user=app.getUserInfo();
		findbalanceAction.setMid(user.accountNo);
		findbalanceAction.setBuyId("addMoney");
	//	Toast.makeText(ExchangeListActivity.this, user.accountNo, Toast.LENGTH_LONG).show();
		findbalanceAction.setTransferPayResponse(findOrderRespond);
		if (loadingDialog.isShowing()) {
			loadingDialog.setMessage("正在查询订单，请稍候");
		} else {
			loadingDialog.show("正在查询订单，请稍候");
		}
		findbalanceAction.transStart();
	}
	
	TransferPayResponse findOrderRespond = new TransferPayResponse() {

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			 if(loadingDialog!=null){
				 loadingDialog.cancel();
			 }
			Toast.makeText(BalanceChangeActivity.this, httpCode + "#" + errorInfo,
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void transComplete() {
			 if(loadingDialog!=null){
				 loadingDialog.cancel();
			 }
			if (HttpCodeHelper.RESPONSE_SUCCESS
					.equals(findbalanceAction.respondData.respCode)) {
				 pagination=findbalanceAction.respondData.pagination;
				 if(currentPage>1){
					 orderAdater.setDatas(pagination.datas, true);
				 }else{
					 orderAdater.setDatas(pagination.datas, false);
				 }
				 orderAdater.notifyDataSetChanged();
			/*	 if(pagination.page<pagination.countPage){
					 footerView.setVisibility(View.VISIBLE);
					 lyLoading.setVisibility(View.GONE);
				 }else{
					 footerView.setVisibility(View.GONE);
				 }*/
				 
			} else {
				Toast.makeText(BalanceChangeActivity.this,
						findbalanceAction.respondData.respDesc, Toast.LENGTH_LONG)
						.show();
			}
		}

	};
}

