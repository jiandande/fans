package com.jiandande.review.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jiandande.money.R;
import com.jiandande.review.bean.UserInfo;





public class PayRecordActivity extends AbstractActivity implements OnClickListener{
	 public static final String ACTION_SERVICE_STATE_CHANGE = "ACTION_SERVICE_STATE_CHANGE";  
	TextView ordermessage;
	Button   btnClearRecordLog;
	@Override
	protected void addEventLister(){
		btnBack.setOnClickListener(this);
		btnClearRecordLog.setOnClickListener(this);
	}
	public class TimerTask implements Runnable {  
	    @Override  
	    public void run() {  
	        //	startAPP(QQ_PACKAGENAME);
	        //        Thread.sleep(28000);// 线程暂停10秒，单位毫秒       
	                Message message = new Message(); 
				       message.what = 0x000100000; 
				       handler.sendMessage(message); 
	    }  
	}  

	Handler handler = new Handler() { 
	    @Override 
	    public void handleMessage(Message msg) { 
	        // TODO Auto-generated method stub 
	  //  	ordermessage.setText("");
	        super.handleMessage(msg); 
	    }
	};
	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 if(v.getId()==R.id.back){
			 super.onBackPressed();
			 finish();
		 }
		 else if(v.getId()==R.id.clearrecordlog)
		 {
			//	app.setOrdermessageInfo("");
			 Toast.makeText(this, "清空按钮事情", Toast.LENGTH_SHORT).show();
				ordermessage.setText("");
				app.setOrdermessageInfo("");
				
				//new Thread(new TimerTask()).start();  
		 }
		 
	}
	
	
	 @Override  
	    protected void onResume() {  
	        super.onResume();  
	        IntentFilter filter = new IntentFilter();  
	        filter.addAction(ACTION_SERVICE_STATE_CHANGE);  
	        registerReceiver(new PayRecordActivity.ServiceStateReceiver(), filter);  
	 
		       
	    }  
	 
	  private class ServiceStateReceiver extends BroadcastReceiver {  
	        @Override  
	        public void onReceive(Context context, Intent intent) {  
	           // textView.setText(intent.getBooleanExtra("state", false) ? "服务已开启" : "服务已关闭");  
	        	Toast.makeText(context,  intent.getBooleanExtra("state", false) ? "服务已开启" : "服务已关闭", Toast.LENGTH_SHORT).show();	
	        }  
	    }  

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payrecordlog);
		UserInfo user=app.getUserInfo();
		initView();
		if (app.getOrdermessageInfo()!=null){user.paymessage=app.getOrdermessageInfo();}
		ordermessage = (TextView) findViewById(R.id.titlebar);
		btnClearRecordLog = (Button) findViewById(R.id.clearrecordlog);
		ordermessage.setText("text"+user.paymessage);
		//ordermessage.setText("");
		addEventLister();
	}
}


