package com.jiandande.review.activity;

import java.util.List;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.audiofx.BassBoost.Settings;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.Toast;











import com.jiandande.money.R;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.StringUtil;
import com.jiandande.review.util.SystemUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.PlatType;

public class MyMoreActivity extends AbstractActivity implements OnClickListener {
	Button BtnSetAccount;
	Button BtnSetMoreInfo;
	Button BtnExChargeList;
	Button BtnFeedback;
	Button BtnHelp;
	Button BtnUserGuide;
	Button BtnLoginOut;
	Button  Btncontactcustomer;
	Button BtnPayRecord;
	Button  BtnJoinQQGroup;
	Button BtnSysbalance;
	Button BtnBalanceChange;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		initView();
		addEventLister();
	}

	
	

	
	
	
	
	
	
	protected void initView() {
		BtnSetAccount = (Button) findViewById(R.id.setaccount);
		BtnSetMoreInfo = (Button) findViewById(R.id.settingpwd);
		BtnExChargeList = (Button) findViewById(R.id.exchangetask);
		BtnFeedback = (Button) findViewById(R.id.feedback);
		BtnHelp = (Button) findViewById(R.id.helpdes);
		BtnUserGuide = (Button) findViewById(R.id.newguide);
		BtnLoginOut = (Button) findViewById(R.id.contactus);
		BtnSysbalance= (Button) findViewById(R.id.ownbusinesstask);
		BtnBalanceChange = (Button) findViewById(R.id.balance_change);
		Btncontactcustomer = (Button) findViewById(R.id.custmonservice);
		BtnJoinQQGroup = (Button) findViewById(R.id.joinqqgroup);
		BtnPayRecord = (Button) findViewById(R.id.payrecord2);
		
		if(app.getUserInfo()!=null)
		{
	    if(app.getUserInfo().bindStatus==UserInfo.BINDED){
		BtnSetAccount.setVisibility(View.GONE);
		 }
		}
	}

	protected void addEventLister() {
		super.addEventLister();
		BtnSetAccount.setOnClickListener(this);
		BtnSetMoreInfo.setOnClickListener(this);
		BtnExChargeList.setOnClickListener(this);
		BtnFeedback.setOnClickListener(this);
		BtnHelp.setOnClickListener(this);
		BtnUserGuide.setOnClickListener(this);
		BtnLoginOut.setOnClickListener(this);
		BtnSysbalance.setOnClickListener(this);
		BtnBalanceChange.setOnClickListener(this);
		Btncontactcustomer.setOnClickListener(this);	
		BtnJoinQQGroup.setOnClickListener(this);	
		BtnPayRecord.setOnClickListener(this);
	}
	
	private boolean checkPackageInstalled(String packageName, String browserUrl) {
		try {
			// 检查是否有支付宝客户端
			getPackageManager().getPackageInfo(packageName, 0);
			return true;
		} catch (NameNotFoundException e) {
			// 没有安装支付宝，跳转到应用市场
			try {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=" + packageName));
				startActivity(intent);
			} catch (Exception ee) {// 连应用市场都没有，用浏览器去支付宝官网下载
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(browserUrl));
					startActivity(intent);
				} catch (Exception eee) {
				
				}
			}
		}
		return false;
	}
	
	  @SuppressLint("NewApi")
	public static boolean isStartAccessibilityService(Context context, String name){
	        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
	        List<AccessibilityServiceInfo> serviceInfos = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
	        for (AccessibilityServiceInfo info : serviceInfos) {
	            String id = info.getId();
	          //  LogUtil.d("all -->" + id);
	            if (id.contains(name)) {
	                return true;
	            }
	        }
	        return false;
	    }
	@Override
	public void onClick(View v) {
		Intent intent=new Intent();
		switch (v.getId()) {
		case R.id.settingpwd:
			intent.setClass(MyMoreActivity.this, SetPwdActivity.class);
			startActivity(intent);
			break;
		case R.id.setaccount:
		/*	if(app.getUserInfo().bindStatus==UserInfo.BINDED){
				intent.setClass(MyMoreActivity.this, ShowMoreInfoActivity.class);
				startActivity(intent);
				break;
			}else{*/
				intent.setClass(MyMoreActivity.this, BindMoreInfoActivity.class);
				startActivity(intent);
				break;
			//}
			
		case R.id.exchangetask:
			intent.setClass(MyMoreActivity.this, ExchangeListActivity.class);
			startActivity(intent);
			break;
		case R.id.feedback:
			intent.setClass(MyMoreActivity.this, FeedBackActivity.class);
			startActivity(intent);
			break;
		case R.id.newguide:
			intent.setClass(MyMoreActivity.this, NewGuideActivity.class);
			startActivity(intent);
			break;
		case R.id.helpdes:
			intent.setClass(MyMoreActivity.this, HelpActivity.class);
			startActivity(intent);
			break;
		case R.id.contactus:
	        intent.setAction("android.intent.action.VIEW");    
	        Uri content_url = Uri.parse("https://www.jiandande.com/"); 
	       // Uri content_url = Uri.parse("https://www.jiandande.com/html/xitongkaifa/benzhanyuanchuang/201508/02-7435.html"); 
		     
	        //    Uri content_url = Uri.parse("http://weibo.com/ttarticle/p/show?id=2309403960306106799718"); 
	        intent.setData(content_url);  
	        startActivity(intent);
			break;
		
		
		
		
		
		case R.id.custmonservice:
			if (!checkPackageInstalled("com.tencent.mobileqq",
					"https://www.qq.com")) { // 先要判断有没有安装QQ客户端，否则没有安装自己调用会报错的
				Toast.makeText(MyMoreActivity.this, "请安装QQ客户端", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			String url="mqqwpa://im/chat?chat_type=wpa&uin=710920465";
			startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));
			break;
		
		
		case R.id.joinqqgroup:
			if (!checkPackageInstalled("com.tencent.mobileqq",
					"https://www.qq.com")) { 
				Toast.makeText(MyMoreActivity.this, "请安装QQ客户端", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			String url22 = "mqqwpa://im/chat?chat_type=group&uin=205275168&version=1";
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url22)));
			break;

		
		case R.id.ownbusinesstask:
			intent.setClass(MyMoreActivity.this, OwnBusinessTaskListActivity.class);
			startActivity(intent);
			break;
		case R.id.balance_change:
			intent.setClass(MyMoreActivity.this, 
					BalanceChangeActivity.class);
			startActivity(intent);
			break;
		case R.id.payrecord2:
			intent.setClass(MyMoreActivity.this, 
					PayRecordActivity.class);
			startActivity(intent);
			
			if(!isStartAccessibilityService(this,"com.jiandande.luckymoney.GrabLuckyRedPacketService"))
			{
		    	try
			   {
				Intent intent2 = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
				startActivity(intent2);
				Toast.makeText(this, "找到“简单的支付订单记录”，然后开启服务即可", Toast.LENGTH_LONG).show();
			   } catch (Exception e)
			   {
				e.printStackTrace();
			   }
			}
			
			break;		
			
		default:
			break;
		}
	}
	
	   
	  void finishAddmoney(){
		   String imei=PreferenceUtil.getStrValue(getBaseContext(), ParamName.IMEI, "");
			if(StringUtil.checkIsNull(imei)){
				imei=new SystemUtil(this).getNewPhoneIMEI();
				PreferenceUtil.saveStrValue(getBaseContext(), ParamName.IMEI, imei);
			}
		   addmoneyAction.setImei(imei);
			if (loadingDialog.isShowing()) {
				loadingDialog.setMessage("正在通讯中请稍候");
			} else {
				loadingDialog.show("正在通讯中请稍候");
			}
		   addmoneyAction.transStart();
		   addmoneyAction.setTransferPayResponse(addmoneyResond);
	   }
	   int count=0;
	   private TransferPayResponse addmoneyResond=new TransferPayResponse() {
		
		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if(count<3){
				addmoneyAction.transStart();
				count++;
			}else{
				if (loadingDialog!=null) {
					loadingDialog.cancel();
				}
				Toast.makeText(MyMoreActivity.this,
						errorInfo, Toast.LENGTH_LONG)
						.show();
			}
		}	
		@Override
		public void transComplete() {
			if(HttpCodeHelper.RESPONSE_SUCCESS.equals(addmoneyAction.respondData.respCode)){
				UserInfo userInfo=app.getUserInfo();
				userInfo.balance=userInfo.balance+addmoneyAction.respondData.amount;
				userInfo.sumCount=userInfo.sumCount+addmoneyAction.respondData.amount;
			    app.setUserInfo(userInfo);
			    if(PlatType.BAIDU.equals(addmoneyAction.getPlatType())){
			    	/* int points=net.youmi.android.offers.PointsManager.getInstance(MyMoreActivity.this).queryPoints();
					  LogUtil.d("addmoneyAction", "points...."+points);
			    	 if(points>0){
						   addmoneyAction.setMoney(points);
						   addmoneyAction.setSource("APP_DWONLOAD");
						   addmoneyAction.setPlatType(PlatType.YOUMI);
						   finishAddmoney();
					   }else{
						 
					   }	   
					   count=0;*/
			    }else if(PlatType.YOUMI.equals(addmoneyAction.getPlatType())){
			    	/* int points=net.youmi.android.offers.PointsManager.getInstance(MyMoreActivity.this).queryPoints();
					  LogUtil.d("addmoneyAction", "points...."+points);
				  
					   count=0;
					   Toast.makeText(MyMoreActivity.this, "同步余额成功", Toast.LENGTH_LONG)
						.show();
			    }else{
			    	if (loadingDialog!=null) {
						loadingDialog.cancel();
					}*/
		
			    }
			}else{
				if(count<3){
					addmoneyAction.transStart();
					count++;
				}else{
					if (loadingDialog!=null) {
						loadingDialog.cancel();
					}
					Toast.makeText(MyMoreActivity.this,
							addmoneyAction.respondData.respCode+"##"+addmoneyAction.respondData.respDesc, Toast.LENGTH_LONG)
							.show();
				}
				
			}
		}
	  };

	
}
