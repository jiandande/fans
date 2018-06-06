package com.jiandande.review.activity;


import java.util.ArrayList;




import com.jiandande.money.R;
import com.jiandande.review.MainApplication;
import com.jiandande.review.bean.HistoryMoney;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.dialog.DialogFactory;
import com.jiandande.review.dialog.LoadingDialog;
import com.jiandande.review.transcation.AddMoneyAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.transfer.NetworkUtil;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.StringUtil;
import com.jiandande.review.util.SystemUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.PlatType;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public abstract class AbstractActivity extends Activity  {

	protected static final int MSG_UPDATE_TEXT = 1;
	protected static final int MSG_UPDATE_VERSION = 2;
	protected DialogFactory dialog;
	
	protected LoadingDialog loadingDialog;
	protected MainApplication app;
	
	protected Button btnBack;
	protected TextView activityTitle;
	
	protected String TAG;
	AddMoneyAction addmoneyAction;
	int count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app=(MainApplication)getApplicationContext();
		dialog=DialogFactory.getInstance(this);
		loadingDialog=new LoadingDialog(this);
		TAG=this.getClass().getName();
        app.addActivity(this);
        addmoneyAction = new AddMoneyAction();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!(this instanceof WelComeActivity)){

		}
	}

	protected void addEventLister() {

	
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		app.removeActivity(this);
	}
	
	protected boolean checkValue(boolean haveCheckNet) {
		
		if (haveCheckNet && !checkNet()) {
			Toast.makeText(AbstractActivity.this, "请打开网路", Toast.LENGTH_LONG).show();         
			return false;
		}
		return true;
	}
	
	
	protected boolean checkNet() {
		if (NetworkUtil.getNetworkType(this) == NetworkUtil.NO_NET_CONNECT) {
			return false;
		} else {
			LogUtil.i("checkNet", "当前网络可用");
			return true;
		}
	}




	protected void initView() {
		btnBack=(Button)findViewById(R.id.back);
		activityTitle=(TextView)findViewById(R.id.activity_title);
	}	
	




	
	void finishAddmoney() {
		String imei = PreferenceUtil.getStrValue(getBaseContext(),
				ParamName.IMEI, "");
		if (StringUtil.checkIsNull(imei)) {
			imei = new SystemUtil(this).getNewPhoneIMEI();
			PreferenceUtil.saveStrValue(getBaseContext(), ParamName.IMEI, imei);
		}
		addmoneyAction.setImei(imei);
		addmoneyAction.setTransferPayResponse(addmoneyResond);
		addmoneyAction.transStart();
		
	}
	
	private TransferPayResponse addmoneyResond = new TransferPayResponse() {

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if (count < 3) {
				addmoneyAction.transStart();
				count++;
			}
		}

		@Override
		public void transComplete() {
			if (HttpCodeHelper.RESPONSE_SUCCESS
					.equals(addmoneyAction.respondData.respCode)) {
				UserInfo userInfo = app.getUserInfo();
				userInfo.balance = userInfo.balance
						+ addmoneyAction.respondData.amount;
				userInfo.sumCount = userInfo.sumCount
						+ addmoneyAction.respondData.amount;
				app.setUserInfo(userInfo);
			} else {
				if (count < 3) {
					addmoneyAction.transStart();
					count++;
				}
			}
		}
	};
}
