package com.jiandande.review.activity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;












import c.b.BP;
import c.b.PListener;

import com.jiandande.money.R;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.transcation.OwnBusinessTaskAction;
//import com.jiandande.review.transcation.ClientLuckyMainAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.StringUtil;
import com.jiandande.review.util.Constants.ParamName;

public class VipUserCenterActivity extends AbstractActivity implements OnClickListener{
	Button btnBack;
	String APPID = "54bb7ee8a8acd477bf5f19393e7d9c05";
	// 此为支付插件的官方最新版本号,请在更新时留意更新说明
//	 ClientLuckyMainAction LuckyAword;
	EditText name, price, content, order;
	Button openvipmemberBtn;
	RadioGroup type;
	TextView tv;
	
	ProgressDialog dialog;
	OwnBusinessTaskAction VipTaskAction;
	@Override
	public void initView() {
		
		btnBack=(Button)findViewById(R.id.back);		
		name = (EditText) findViewById(R.id.vipname);
		
		price = (EditText) findViewById(R.id.vipprice);
		content = (EditText) findViewById(R.id.vipcontent);
		
		order = (EditText) findViewById(R.id.viporder);
		openvipmemberBtn = (Button) findViewById(R.id.openvipmember);
		type = (RadioGroup) findViewById(R.id.viptype);

	}
	 private static final int REQUESTPERMISSION = 101;

	    private void installApk(String s) {
	    	
	    	if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
	            //申请权限
	            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTPERMISSION);
	        } else {
	            installBmobPayPlugin(s);
	        }
	    }
	    @SuppressLint({ "NewApi", "Override" })
		public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
	        if (requestCode == REQUESTPERMISSION) {
	            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
	                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
	                    installBmobPayPlugin("bp.db");
	                } else {
	                    //提示没有权限，安装不了
	                    Toast.makeText(VipUserCenterActivity.this,"您拒绝了权限，这样无法安装支付插件",Toast.LENGTH_LONG).show();
	                }
	            }
	        }
	    }	
	String getName() {
		 UserInfo user=app.getUserInfo();
		 if (user !=null) {  
				return this.name.getText().toString()+""+ user.accountNo;
		 }
		 else{
		return this.name.getText().toString();
		 }
	}

	String getName2() {
		return this.name.getText().toString();
	}

	// 商品详情(可不填)
	String getBody() {
		return this.content.getText().toString();
	}

	// 支付订单号(查询时必填)
	String getOrder() {
		return this.order.getText().toString();
	}
		double getPrice() {
			double price = 10;
			try {
				price = Double.parseDouble(this.price.getText().toString());
			} catch (NumberFormatException e) {
			}
			return price;
		}
		
		String getPrice2() {
			String price = "10";
			price = this.price.getText().toString();
		
			return price;
		}
		/**
		 * 检查某包名应用是否已经安装
		 * 
		 * @param packageName
		 *            包名
		 * @param browserUrl
		 *            如果没有应用市场，去官网下载
		 * @return
		 */
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
						Toast.makeText(VipUserCenterActivity.this,
								"您的手机上没有没有应用市场也没有浏览器，我也是醉了，你去想办法安装支付宝/微信吧",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
			return false;
		}

		/**
		 * 调用支付
		 * 
		 * @param alipayOrWechatPay
		 *            支付类型，true为支付宝支付,false为微信支付
		 */
		void pay(final boolean alipayOrWechatPay) {
			if (alipayOrWechatPay) {
				if (!checkPackageInstalled("com.eg.android.AlipayGphone",
						"https://www.alipay.com")) { // 支付宝支付要求用户已经安装支付宝客户端
					Toast.makeText(VipUserCenterActivity.this, "请安装支付宝客户端", Toast.LENGTH_SHORT)
							.show();
					return;
				}
			} else {
	            if (checkPackageInstalled("com.tencent.mm", "http://weixin.qq.com")) {// 需要用微信支付时，要安装微信客户端，然后需要插件
	                // 有微信客户端，看看有无微信支付插件，170602更新了插件，这里可检查可不检查
	                if (!BP.isAppUpToDate(this, "cn.bmob.knowledge", 8)){
	                    Toast.makeText(
	                    		VipUserCenterActivity.this, 
	                            "监测到本机的支付插件不是最新版,最好进行更新,请先更新插件(无流量消耗)",
	                            Toast.LENGTH_SHORT).show();
	                    installApk("bp.db");
	                    return;
	                }
	            } else {// 没有安装微信
	                Toast.makeText(VipUserCenterActivity.this, "请安装微信客户端", Toast.LENGTH_SHORT).show();
	                return;
	            }
			}

	
			showDialog("正在获取订单...\nSDK版本号:" + BP.getPaySdkVersion());
			  

			final String name = getName();
			try {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				ComponentName cn = new ComponentName("com.bmob.app.sport",
						"com.bmob.app.sport.wxapi.BmobActivity");
				intent.setComponent(cn);
				this.startActivity(intent);
		} catch (Throwable e) {
				e.printStackTrace();
			}

			BP.pay(name, getBody(), getPrice(), alipayOrWechatPay, new PListener() {

				// 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
				@Override
				public void unknow() {
					Toast.makeText(VipUserCenterActivity.this, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT).show();
					hideDialog();
				}

				// 支付成功,如果金额较大请手动查询确认
				@Override
				public void succeed() {
					Toast.makeText(VipUserCenterActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();
					hideDialog();
					
					 String	taskpoint=StringUtil.moneyFormat2(getPrice());   //debug 模式 1毛*100相当于10 开通V1会员 	        
			         String password=PreferenceUtil.getStrValue(VipUserCenterActivity.this,ParamName.PASSWORD,"");
					 String email=PreferenceUtil.getStrValue(VipUserCenterActivity.this,ParamName.USERNAME,"");
					 String imei=PreferenceUtil.getStrValue(VipUserCenterActivity.this, ParamName.IMEI, "");
						
						
					
					VipTaskAction=new OwnBusinessTaskAction();
					VipTaskAction.setPassword(taskpoint);//getPrice()
					VipTaskAction.setEmail(email);
					VipTaskAction.setImei(imei);
				//	OwnBusinessTaskAction.setOwnbusiness("666666");
				
					VipTaskAction.setOwnbusiness("222888");
					VipTaskAction.setPackagename("");
				//	VipTaskAction.setImei(imei);
					VipTaskAction.setPname(name);
					VipTaskAction.setTransferPayResponse(GetVipbusinessResPond);
					VipTaskAction.transStart();
					
				}

				// 无论成功与否,返回订单号
				@Override
				public void orderId(String orderId) {
					// 此处应该保存订单号,比如保存进数据库等,以便以后查询
					order.setText(orderId);
					showDialog("获取订单成功!请等待跳转到支付页面~");
				}

				// 支付失败,原因可能是用户中断支付操作,也可能是网络原因
				@Override
				public void fail(int code, String reason) {

					// 当code为-2,意味着用户中断了操作
					// code为-3意味着没有安装BmobPlugin插件
					if (code == -3) {
						Toast.makeText(
								VipUserCenterActivity.this,
								"监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
								0).show();
//						installBmobPayPlugin("bp.db");
						installApk("bp.db");

						
					} else {
						Toast.makeText(VipUserCenterActivity.this, "支付可能出错,"+reason, Toast.LENGTH_SHORT)
								.show();
					}
			
					hideDialog();
				}
			});
		}
		
		
		//自营任务的回调函数
		private TransferPayResponse GetVipbusinessResPond=new TransferPayResponse(){

			@Override
			public void transComplete() {
				if (loadingDialog!=null) {
					loadingDialog.cancel();
				} 
				
				if(HttpCodeHelper.RESPONSE_SUCCESS.equals(VipTaskAction.respondData.respCode)){
					Toast.makeText(VipUserCenterActivity.this,VipTaskAction.respondData.respDesc, Toast.LENGTH_LONG).show();
				}else if(HttpCodeHelper.PASSWORD_ERROR.equals(VipTaskAction.respondData.respCode)){
					
				}else{
					Toast.makeText(VipUserCenterActivity.this,VipTaskAction.respondData.respCode+"#"+VipTaskAction.respondData.respDesc, Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void transFailed(String httpCode, String errorInfo) {
				if (loadingDialog!=null) {
					loadingDialog.cancel();
				}
				Toast.makeText(VipUserCenterActivity.this, errorInfo, Toast.LENGTH_LONG).show();	
			}
			
		};		
			
	void showDialog(String message) {
		try {
			if (dialog == null) {
				dialog = new ProgressDialog(this);
				dialog.setCancelable(true);
			}
			dialog.setMessage(message);
			dialog.show();
			//Toast.makeText(VipUserCenterActivity.this, "showDialog.show!", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// 在其他线程调用dialog会报错
		}
	}

	void hideDialog() {
	
		if (dialog != null && dialog.isShowing())
			try {
				dialog.dismiss();
			} catch (Exception e) {
			}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 if(v.getId()==R.id.back){
			 super.onBackPressed();
			 finish();
		 }else 	 if(v.getId()==R.id.openvipmember){
		//	 Toast.makeText(VipActivity.this, "222  reword", Toast.LENGTH_LONG).show();  
		 if (type.getCheckedRadioButtonId() == R.id.alipay2) // 当选择的是支付宝支付时
		 {   
	    	pay(true);
		 }
			else if (type.getCheckedRadioButtonId() == R.id.wxpay) // 调用插件用微信支付
			{
				if(getPrice2().equals("")){
   
            		Toast.makeText(VipUserCenterActivity.this, "请输入充值金额  ", Toast.LENGTH_LONG).show();         
            		return ;
            	}
				if(getName2().equals("")){
					   
            	//	Toast.makeText(VipActivity.this, "请输入昵称 ", Toast.LENGTH_LONG).show();         
            	//	return ;
            	}
				
		pay(false);
			}
		 }
			
	}
	
	
	protected void addEventLister(){

		btnBack.setOnClickListener(this);
	
		
		openvipmemberBtn.setOnClickListener(this) ;	
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vip);
		initView();	
		BP.init(APPID);
		addEventLister();
	}
	
	@Override
	 protected void onSaveInstanceState(Bundle outState) {

	/*  outState.putString(user, Application.LOGINNAME);
	  outState.putInt(classId, Application.classId);
	  outState.putSerializable(classinfos, 
	    (ArrayList<classinfo>)Application.getInstance().getClassInfos());*/
	  super.onSaveInstanceState(outState);
	 }
	/*
	//抽奖的回调函数
	private TransferPayResponse GetLuckyPointsResPond=new TransferPayResponse(){

		@Override
		public void transComplete() {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			} 
			
			if(HttpCodeHelper.RESPONSE_SUCCESS.equals(LuckyAword.respondData.respCode)){
				Toast.makeText(VipUserCenterActivity.this,LuckyAword.respondData.respDesc, Toast.LENGTH_LONG).show();
				
				
				Toast.makeText(VipUserCenterActivity.this,LuckyAword.respondData.respDesc, Toast.LENGTH_LONG).show();
			}else if(HttpCodeHelper.PASSWORD_ERROR.equals(LuckyAword.respondData.respCode)){
				
			}else{
				Toast.makeText(VipUserCenterActivity.this,LuckyAword.respondData.respCode+"#"+LuckyAword.respondData.respDesc, Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			Toast.makeText(VipUserCenterActivity.this, errorInfo, Toast.LENGTH_LONG).show();	
		}
		
	};
	
	*/

@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	 super.onBackPressed();
	 finish();
}


void installBmobPayPlugin(String fileName) {
	try {
		InputStream is = getAssets().open(fileName);
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + fileName + ".apk");
		if (file.exists())
		{
			return ;
			//file.delete();
		}
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		byte[] temp = new byte[1024];
		int i = 0;
		while ((i = is.read(temp)) > 0) {
			fos.write(temp, 0, i);
		}
		fos.close();
		is.close();

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

	
	
}

