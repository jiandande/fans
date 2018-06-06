package com.jiandande.review.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;




//import WebviewAnalytic.MyWebviewClient;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
//import c.b.BP;


//import c.b.PListener;









import c.b.BP;
import c.b.PListener;






//import com.inmobi.ads.InMobiBanner;
//import com.inmobi.sdk.InMobiSdk;
import com.jiandande.money.R;
import com.jiandande.review.transcation.ClientLuckyMainAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.Constants.ServerUrl;


public class LuckyMainActivity extends AbstractActivity implements OnClickListener{
	private WebView webView1;
	Button btnBack;
	String APPID = "54bb7ee8a8acd477bf5f19393e7d9c05";
	// 此为支付插件的官方最新版本号,请在更新时留意更新说明
	int PLUGINVERSION = 7;
	 ClientLuckyMainAction LuckyAword;
	
	
	
	EditText name, price, body, order;
	Button rewordBtn;
	RadioGroup type;
	TextView tv;

	ProgressDialog dialog;
	
	@Override
	public void initView() {
		btnBack=(Button)findViewById(R.id.back);

		// 初始化BmobPay对象,可以在支付时再初始化
		name = (EditText) findViewById(R.id.name2);
		price = (EditText) findViewById(R.id.price);
		body = (EditText) findViewById(R.id.body);
		order = (EditText) findViewById(R.id.order);
		rewordBtn = (Button) findViewById(R.id.reword);
		type = (RadioGroup) findViewById(R.id.type);
		
		
		
	
		
		webView1 = (WebView) findViewById(R.id.webView2);
		/*
	        webView1.getSettings().setJavaScriptEnabled(true);
	        webView1.loadUrl("https://www.jiandande.com");
	   */
		webView1.requestFocusFromTouch();
	
		webView1.getSettings().setJavaScriptEnabled(true); 
		final Context myApp = this;  
		
		webView1.setWebChromeClient(new WebChromeClient() {  
		    @Override  
		    public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)  
		    {     	 
		    	    long currTime = System.currentTimeMillis();
		           //打开系统第一次抽奖进行判断
		    
		    	    if (!message.equalsIgnoreCase("谢谢参与"))
			        {
	    	     
		    	    String	luckypoint=message.substring(0,message.indexOf("积分"));
	    	        
		    		
			        }
	    	  
			      
		    	    new AlertDialog.Builder(myApp)  
		            .setTitle("抽奖结果")  
		            .setMessage(message)  
		            .setPositiveButton(android.R.string.ok,  
		                    new AlertDialog.OnClickListener()  
		                    {  
		                        public void onClick(DialogInterface dialog, int which)  
		                        {  
		                        
		                            result.confirm();  
		                        }  
		                    })  
		            .setCancelable(false)  
		            .create()  
		            .show();  
		   
		        return true;  
		    };  
		});  	
		// 设置可以支持缩放 
		webView1.getSettings().setSupportZoom(true); 
		// 设置出现缩放工具 
		//webView1.getSettings().setBuiltInZoomControls(true);
		//扩大比例的缩放
		webView1.getSettings().setUseWideViewPort(true);
		//自适应屏幕
		webView1.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		//webView1.getSettings().setLoadWithOverviewMode(true);
		webView1.getSettings().setDefaultTextEncodingName("UTF-8");  
		webView1.setWebViewClient(new HelloWebViewClient ()); 
	
			webView1.loadUrl(ServerUrl.clientawardranking);
		//webView1.loadUrl("https://www.jiandande.com/ad/app.html");

	}	
	
	
	
	
	String getName() {
		return this.name.getText().toString();
	}

	String getName2() {
		return this.name.getText().toString();
	}

	// 商品详情(可不填)
	String getBody() {
		return this.body.getText().toString();
	}

	// 支付订单号(查询时必填)
	String getOrder() {
		return this.order.getText().toString();
	}
	// 默认为0.02
		double getPrice() {
			double price = 0.8;
			try {
				price = Double.parseDouble(this.price.getText().toString());
			} catch (NumberFormatException e) {
			}
			return price;
		}
		
		String getPrice2() {
			String price = "0.8";
			price = this.price.getText().toString();
		
			return price;
		}
	
	

		
		
		
		
		
		
	void pay(final boolean alipayOrWechatPay) {
		showDialog("正在获取订单...");
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
				Toast.makeText(LuckyMainActivity.this, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT)
						.show();
	
				hideDialog();
			}

			// 支付成功,如果金额较大请手动查询确认
			@Override
			public void succeed() {
				Toast.makeText(LuckyMainActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();
	
				
				LuckyAword=new ClientLuckyMainAction();
				LuckyAword.setPassword(getPrice2());
				LuckyAword.setEmail(getName2());
				LuckyAword.setImei(getBody());
		
				LuckyAword.setTransferPayResponse(GetLuckyPointsResPond);
				LuckyAword.transStart();
		   
				hideDialog();
		
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
							LuckyMainActivity.this,
							"监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
							0).show();
					installBmobPayPlugin("bp.db");
				} else {
					Toast.makeText(LuckyMainActivity.this, "支付中断!", Toast.LENGTH_SHORT)
							.show();
				}
		
				hideDialog();
			}
		});
	}

	void showDialog(String message) {
		try {
			if (dialog == null) {
				dialog = new ProgressDialog(this);
				dialog.setCancelable(true);
			}
			dialog.setMessage(message);
			dialog.show();
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
		 }else 	 if(v.getId()==R.id.reword){
		/* if (type.getCheckedRadioButtonId() == R.id.alipay) // 当选择的是支付宝支付时
				pay(true);
			else if (type.getCheckedRadioButtonId() == R.id.wxpay) // 调用插件用微信支付
			{
				if(getPrice2().equals("")){
   
            		Toast.makeText(LuckyMainActivity.this, "请输入红包金额  ", Toast.LENGTH_LONG).show();         
            		return ;
            	}
				if(getName2().equals("")){
					   
            		Toast.makeText(LuckyMainActivity.this, "请输入昵称 ", Toast.LENGTH_LONG).show();         
            		return ;
            	}
				*/
	//			InMobiBanner banner = (InMobiBanner)findViewById(R.id.banner);
	//			banner.load();
			   Toast.makeText(LuckyMainActivity.this, "您也可以通过微信号或者支付宝账号18758020322进行赞助  ", Toast.LENGTH_LONG).show();    
				if (!checkPackageInstalled("com.tencent.mobileqq",
						"https://www.qq.com")) { // 先要判断有没有安装QQ客户端，否则没有安装自己调用会报错的
					Toast.makeText(LuckyMainActivity.this, "请安装QQ客户端", Toast.LENGTH_SHORT)
							.show();
					return;
				}
			 String url="mqqwpa://im/chat?chat_type=wpa&uin=710920465";
			 startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));
		//		pay(false);
			}
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
	
	
	protected void addEventLister(){

		btnBack.setOnClickListener(this);
	
		rewordBtn.setOnClickListener(this) ;

			

	
		
		
		
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lucky_main);
		initView();
	//	BP.init(this, APPID);
		BP.init(APPID);
	//	InMobiSdk.init(this, "8231887a51cb4dd8ac9c4888e5ae8592"); //'this' is used specify context
		addEventLister();
		/*int pluginVersion = BP.getPluginVersion();
		if (pluginVersion < PLUGINVERSION) {// 为0说明未安装支付插件, 否则就是支付插件的版本低于官方最新版
			Toast.makeText(
					LuckyMainActivity.this,
					pluginVersion == 0 ? "监测到本机尚未安装支付插件,无法进行支付,请先安装插件(无流量消耗)"
							: "监测到本机的支付插件不是最新版,最好进行更新,请先更新插件(无流量消耗)", 0).show();
			installBmobPayPlugin("bp.db");
		}*/
	}
	
	//抽奖的回调函数
	private TransferPayResponse GetLuckyPointsResPond=new TransferPayResponse(){

		@Override
		public void transComplete() {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			} 
			
			if(HttpCodeHelper.RESPONSE_SUCCESS.equals(LuckyAword.respondData.respCode)){
				Toast.makeText(LuckyMainActivity.this,LuckyAword.respondData.respDesc, Toast.LENGTH_LONG).show();
				
				
				Toast.makeText(LuckyMainActivity.this,LuckyAword.respondData.respDesc, Toast.LENGTH_LONG).show();
			}else if(HttpCodeHelper.PASSWORD_ERROR.equals(LuckyAword.respondData.respCode)){
				
			}else{
				Toast.makeText(LuckyMainActivity.this,LuckyAword.respondData.respCode+"#"+LuckyAword.respondData.respDesc, Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			Toast.makeText(LuckyMainActivity.this, errorInfo, Toast.LENGTH_LONG).show();	
		}
		
	};
	
	
	

private class HelloWebViewClient extends WebViewClient { 

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) { 
        view.loadUrl(url); 
        return true; 
    } 
    
    
    @Override
    public void onReceivedError(WebView view, int errorCode,
                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        
           //这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
                      view.loadData("<html><body><h1>Page Not Found</h1></body></html>", "text/html", "UTF-8");
                    
             
    } 
} 


@Override    public boolean onKeyDown(int keyCode, KeyEvent event) {        // TODO Auto-generated method stub
    if(keyCode==KeyEvent.KEYCODE_BACK)
    {
        if(webView1.canGoBack())
        {
        	webView1.goBack();//返回上一页面
            return true;
        }           
        else
        { }   
       
    }
    return super.onKeyDown(keyCode, event);
    }
void installBmobPayPlugin(String fileName) {
	try {
		InputStream is = getAssets().open(fileName);
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + fileName + ".apk");
		if (file.exists())
			file.delete();
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

