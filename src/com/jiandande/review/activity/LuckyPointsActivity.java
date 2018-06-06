package com.jiandande.review.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.jiandande.money.R;
import com.jiandande.review.transcation.ClientLuckyDayAction;
import com.jiandande.review.transcation.ClientSignInAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.ServerUrl;


public class LuckyPointsActivity extends AbstractActivity implements OnClickListener{
	private WebView webView1;
	Button btnBack;
	ClientLuckyDayAction LuckyPointAction;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void initView() {
		btnBack=(Button)findViewById(R.id.back);
		
		
		webView1 = (WebView) findViewById(R.id.webView1);
		webView1.requestFocusFromTouch();
		//支持javascript
		webView1.getSettings().setJavaScriptEnabled(true); 
		final Context myApp = this;  
		/* WebChromeClient must be set BEFORE calling loadUrl! */  
		webView1.setWebChromeClient(new WebChromeClient() {  
		    @Override  
		    public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)  
		    {     	 
		    	    long currTime = System.currentTimeMillis();
		           //打开系统第一次抽奖进行判断
		    	    String	luckypoint;
		    	    if (!message.equalsIgnoreCase("谢谢参与"))
			        {
		    	    	luckypoint=message.substring(0,message.indexOf("积分"));
			        }
		    	    else
		    	    {
		    	     	luckypoint="0";	
		    	    }	    	        
		    		String password=PreferenceUtil.getStrValue(LuckyPointsActivity.this,ParamName.PASSWORD,"");
					String email=PreferenceUtil.getStrValue(LuckyPointsActivity.this,ParamName.USERNAME,"");
					String imei=PreferenceUtil.getStrValue(LuckyPointsActivity.this, ParamName.IMEI, "");
					LuckyPointAction=new ClientLuckyDayAction();
					LuckyPointAction.setPassword(luckypoint);
					LuckyPointAction.setEmail(email);
					LuckyPointAction.setImei(imei);
			
					LuckyPointAction.setTransferPayResponse(GetLuckyPointsResPond);
					if (loadingDialog.isShowing()) {
						loadingDialog.setMessage(" 正在抽奖中，请稍候");
					} else {
						loadingDialog.show("正在抽奖中，请稍候");
					}
					LuckyPointAction.transStart();
			   
			   
	    	  
			      
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
		webView1.loadUrl(ServerUrl.clientluckyday);

	}	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 if(v.getId()==R.id.back){
			 super.onBackPressed();
			 finish();
		 }
	}
	
	
	protected void addEventLister(){

		btnBack.setOnClickListener(this);
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.luckypoints);
		initView();
		addEventLister();
	}
	
	//抽奖的回调函数
	private TransferPayResponse GetLuckyPointsResPond=new TransferPayResponse(){

		@Override
		public void transComplete() {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			} 
			
			if(HttpCodeHelper.RESPONSE_SUCCESS.equals(LuckyPointAction.respondData.respCode)){
				
				
				
				Toast.makeText(LuckyPointsActivity.this,LuckyPointAction.respondData.respDesc, Toast.LENGTH_LONG).show();
			}else if(HttpCodeHelper.PASSWORD_ERROR.equals(LuckyPointAction.respondData.respCode)){
				
			}else{
				Toast.makeText(LuckyPointsActivity.this,LuckyPointAction.respondData.respCode+"#"+LuckyPointAction.respondData.respDesc, Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			Toast.makeText(LuckyPointsActivity.this, errorInfo, Toast.LENGTH_LONG).show();	
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


	
	
	
}

