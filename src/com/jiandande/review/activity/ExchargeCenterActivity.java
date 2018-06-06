package com.jiandande.review.activity;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiandande.money.R;
import com.jiandande.review.adater.ExChargeAdapter;
import com.jiandande.review.adater.FileUtil;
import com.jiandande.review.adater.LoadUserAvatar;
import com.jiandande.review.adater.LoadUserAvatar.ImageDownloadedCallBack;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.transcation.FindUserInfoAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.StringUtil;
import com.jiandande.review.util.Constants.ParamName;



public class ExchargeCenterActivity  extends AbstractActivity implements 
OnItemClickListener ,OnClickListener{

   private ListView listView;
	
	LinearLayout lyDownError;
	
	LinearLayout lyprogress;
	
	FindUserInfoAction findUserInfo;
	ExChargeAdapter adapter;
	
	TextView txtUserName;
	TextView txtTotal;
	TextView txtBanace;
	boolean needreflash=false;
	
	private ImageView iv_avatar;
	 private LoadUserAvatar avatarLoader;
	
		private ImageView iv_viplevel;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exchange_center);
		initView();
	
	   findUserInfo=new FindUserInfoAction();
		String imei=PreferenceUtil.getStrValue(this, ParamName.IMEI, "");
		findUserInfo.setImei(imei);
        startFinduser();
		addEventLister();
		 avatarLoader = new LoadUserAvatar(this, "/sdcard/jiandande/");
	}
	
	
	public boolean fileIsExists(String strPath){
        try{
                File f=new File(strPath);
                if(!f.exists()){
                        return false;
                }
                
        }catch (Exception e) {
                // TODO: handle exception
                return false;
        }
        return true;
}
	
	
	private void startFinduser() {
		UserInfo user=app.getUserInfo();

		
		findUserInfo.setTransferPayResponse(findUserRespond);
		
		findUserInfo.transStart();
		lyprogress.setVisibility(View.VISIBLE);
	
	}

	@Override
	protected void addEventLister() {
		super.addEventLister();
		listView.setOnItemClickListener(this);
		lyDownError.setOnClickListener(this);
	}
	protected void initView(){
		listView = (ListView) findViewById(R.id.lv);
		 adapter = new ExChargeAdapter(this);
		listView.setAdapter(adapter);
		txtBanace=(TextView)findViewById(R.id.pointcount);
		txtTotal=(TextView)findViewById(R.id.beancount);
		txtUserName=(TextView)findViewById(R.id.imei);
		lyDownError=(LinearLayout) findViewById(R.id.load_error);
		lyprogress=(LinearLayout) findViewById(R.id.progressbar_layout);
	     iv_avatar = (ImageView) this.findViewById(R.id.iv_avatar2);
		
		String avatar=PreferenceUtil.getStrValue(this,ParamName.USERIMAGE,"");
	      if (fileIsExists(avatar))
	      {
	 
	      showUserAvatar(iv_avatar, avatar);
	      }
	      iv_viplevel = (ImageView) this.findViewById(R.id.iv_vip2);
	      iv_viplevel.getDrawable().setLevel(2);
	}
	
	  public static final String URL_Avatar = "http://www.jiandande.com/jiandande/upload/";
	  private void showUserAvatar(ImageView iamgeView, String avatar) {
		        Bitmap bitmap ;
		   
		 
		            bitmap = BitmapFactory.decodeFile(avatar);
		            if (bitmap != null)
		                iamgeView.setImageBitmap(bitmap);

	  }
	
	
	//登录的回调函数
		private TransferPayResponse findUserRespond=new TransferPayResponse(){

			@Override
			public void transComplete() {
				 needreflash=false;
				if (loadingDialog!=null) {
					loadingDialog.cancel();
				} 
				if(HttpCodeHelper.RESPONSE_SUCCESS.equals(findUserInfo.respondData.respCode)){
					//保存
					app.setUserInfo(findUserInfo.respondData.userInfo);
					reflashView();			
				}else{
					lyprogress.setVisibility(View.GONE);
					lyDownError.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
					Toast.makeText(ExchargeCenterActivity.this, findUserInfo.respondData.respDesc, Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void transFailed(String httpCode, String errorInfo) {
				if (loadingDialog!=null) {
					loadingDialog.cancel();
				}
				listView.setVisibility(View.GONE);
				lyprogress.setVisibility(View.GONE);
				lyDownError.setVisibility(View.VISIBLE);
				Toast.makeText(ExchargeCenterActivity.this, errorInfo, Toast.LENGTH_LONG).show();	
			}
			
		};
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
		UserInfo user=app.getUserInfo();
		double fee=StringUtil.calculateFee(user.aplyCount);
		if(postion==0){
				Intent intent=new Intent(ExchargeCenterActivity.this,AlipayChargeActivity.class);
			    startActivity(intent);
				needreflash=true;
	
			
		}else if(postion==1){

				Intent intent=new Intent(ExchargeCenterActivity.this,PhoneChargeActivity.class);
			    startActivity(intent);
				needreflash=true;
	
		}else if(postion==2){
		    
		    	Intent intent=new Intent(ExchargeCenterActivity.this,QqChargeActivity.class);
			    startActivity(intent);
				needreflash=true;
			}
		}
	

	protected void reflashView() {
		UserInfo user=app.getUserInfo();
		String paymessage=user.paymessage;
		txtUserName.setText("会员ID:"+StringUtil.getUserName(user));
		txtTotal.setText("可兑："+StringUtil.moneyFormat((user.sumCount)/1000)+"元");
		txtBanace.setText("已兑："+StringUtil.moneyFormat(user.checkCount/1000)+"元");
		lyDownError.setVisibility(View.GONE);
		lyprogress.setVisibility(View.GONE);
		listView.setVisibility(View.VISIBLE);
		adapter.notifyDataSetChanged();
		if (iv_viplevel!=null){ iv_viplevel.getDrawable().setLevel(user.vip);}
	}

	@Override
	public void onClick(View v) {
		startFinduser() ;
	}
	
	@Override
		protected void onResume() {
			super.onResume();
			if(needreflash){
				startFinduser();
			}			
		}
}

