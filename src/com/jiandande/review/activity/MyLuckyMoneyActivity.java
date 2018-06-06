package com.jiandande.review.activity;

import net.youmi.android.AdManager;
import net.youmi.android.normal.spot.SpotListener;
import net.youmi.android.normal.spot.SpotManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;






import com.jiandande.money.R;
//import com.jiandande.review.activity.LuckyPointsActivity.HelloWebViewClient;
import com.jiandande.review.transcation.ClientLuckyDayAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.ServerUrl;


public class MyLuckyMoneyActivity extends AbstractActivity implements OnClickListener {

	private Button btnGrabRedPacket;
	private Button btnreword;
	private Button btnreword2;
	private WebView webView1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
  	  AdManager.getInstance(this).init("434afaf01560ec82", "ce011c60e3c6c087", false , true );
		setContentView(R.layout.luckymoney);

		initView();
		addEventLister();
	}

	
	
	
	protected void initView() {
		btnGrabRedPacket=(Button)findViewById(R.id.login_GrabRedPackets_btn);
		btnreword=(Button)findViewById(R.id.login_Areward_btn);
		btnreword2=(Button)findViewById(R.id.login_Areward2_btn);
		
	}

	protected void addEventLister() {
		super.addEventLister();
		btnGrabRedPacket.setOnClickListener(this);
		btnreword.setOnClickListener(this);
		btnreword2.setOnClickListener(this);
	}
	

	 @Override
	    protected void onResume() {
	        super.onResume();

	       
	        
	        }

	    

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_GrabRedPackets_btn:
			open();
		

		//	startActivity(new Intent(MyLuckyMoneyActivity.this, SpotAdActivity.class));
			
			break;	
		case R.id.login_Areward_btn:

			Intent intent=new Intent(MyLuckyMoneyActivity.this,VipUserCenterActivity.class);
		    startActivity(intent);   
			break;	
		case R.id.login_Areward2_btn:
			Intent intent2=new Intent();
			intent2.setClass(MyLuckyMoneyActivity.this, LuckyMainActivity.class);
			startActivity(intent2);
		
			break;			
		default:
			break;
		}
	}
	
	private void open()
	{
		try
		{
			Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
			startActivity(intent);
			Toast.makeText(this, "找到“简单的抢红包辅助”，然后开启服务即可", Toast.LENGTH_LONG).show();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	   
	  void finishAddmoney(){
	
	   }
	   int count=0;
	   private TransferPayResponse addmoneyResond=new TransferPayResponse() {
		
		@Override
		public void transFailed(String httpCode, String errorInfo) {
			

		}	
		@Override
		public void transComplete() {
			
				
			    
		
		

		}
	  };




}
