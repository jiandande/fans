package com.jiandande.review.activity;



import com.baidu.android.pushservice.BasicPushNotificationBuilder;
import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.push.example.Utils;
import com.jiandande.luckymoney.GrabLuckyRedPacketService;
import com.jiandande.money.R;
import com.jiandande.review.MainApplication;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.util.LogUtil;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.provider.MediaStore.Audio;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.Toast;

public class IndexActivity extends AbstractMyActivityGroup {
	  MainApplication app = null;
	//加载的Activity的名字，LocalActivityManager就是通过这些名字来查找对应的Activity的。
	 private static final String CONTENT_ACTIVITY_NAME_0 = "contentActivity0";
    private static final String CONTENT_ACTIVITY_NAME_1 = "contentActivity1";
    private static final String CONTENT_ACTIVITY_NAME_2 = "contentActivity2";
    private static final String CONTENT_ACTIVITY_NAME_3 = "contentActivity3";
    private static final String CONTENT_ACTIVITY_NAME_luckymoney = "contentActivityluckymoney"; 
    
    Button btn_my; 
    Button btn_luckymoney;
    Button btn_taskcenter;
    Button btn_exchargegift ;
    Button btn_love ;
    
    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = getPackageName()+"/com.jiandande.luckymoney.GrabLuckyRedPacketService";
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
 
        } catch (SettingNotFoundException e) {
   
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
 
          String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();

                    if (accessabilityService.equalsIgnoreCase(service)) {
   
                        return true;
                    }
                }
            }
        } else {

        }

        return accessibilityFound;      
    }
    
    /**
	 * 连续按两次返回键就退出
	 */
	private long firstTime;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (System.currentTimeMillis() - firstTime < 3000) {
			//mApplication.showNotification();
		
			finish();
		} else {
			firstTime = System.currentTimeMillis();
		//	if (mSpUtil.getMsgNotify())
				
		//	Toast.makeText(this, R.string.press_again_backrun, Toast.LENGTH_LONG).show();	
		//	else
				Toast.makeText(this, R.string.press_again_exit, Toast.LENGTH_LONG).show();	
		
		}
	}

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	this.app = (MainApplication) super.getApplication();
     	this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.main);
        super.onCreate(savedInstanceState);
        setContainerView(CONTENT_ACTIVITY_NAME_0, UserCenterActivity.class);

        
      /*  final ScreenManager screenManager = ScreenManager.getInstance(IndexActivity.this);
       
        ScreenBroadcastListener listener = new ScreenBroadcastListener(this);
         listener.registerListener(new ScreenBroadcastListener.ScreenStateListener() {
           @Override
            public void onScreenOn() {
             screenManager.finishActivity();
          // 	UserInfo user=app.getUserInfo();
		//		user.paymessage=user.paymessage+"onScreenOn-screenManager.finishActivity()";
            }

            @Override
            public void onScreenOff() {
                screenManager.startActivity();
            //	UserInfo user=app.getUserInfo();
        	//	user.paymessage=user.paymessage+"onScreenOff-screenManager.startActivity()";
            	
            }
        });*/
     //   this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);  
     //   setContentView(R.layout.welcome); //构造锁屏显示界面
    }
    
	@Override
	protected void initRadioBtns() {
		// TODO Auto-generated method stub
		initRadioBtn(R.id.radio_button0);
        initRadioBtn(R.id.radio_button1);
        initRadioBtn(R.id.radio_button2);
        initRadioBtn(R.id.radio_button3);
        initRadioBtn(R.id.radio_button_luckymoney);
	     btn_my = (Button) findViewById(R.id.radio_button0);
         btn_taskcenter = (Button) findViewById(R.id.radio_button1);
         btn_exchargegift = (Button) findViewById(R.id.radio_button2);
         btn_love = (Button) findViewById(R.id.radio_button3);
         btn_luckymoney= (Button) findViewById(R.id.radio_button_luckymoney);
	}

	@Override
	protected ViewGroup getContainer() {
		// TODO Auto-generated method stub
		 return (ViewGroup) findViewById(R.id.container);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
        
        case R.id.radio_button0:
            setContainerView(CONTENT_ACTIVITY_NAME_0, UserCenterActivity.class);
            btn_my.setSelected(true);
            btn_taskcenter.setSelected(false);
            btn_exchargegift.setSelected(false);
            btn_exchargegift.setSelected(false);
            btn_luckymoney.setSelected(false);

       
            
            break;
            
        case R.id.radio_button1:
            setContainerView(CONTENT_ACTIVITY_NAME_1, ChargeCenterActivity.class);
            btn_my.setSelected(false);
            btn_taskcenter.setSelected(true);
            btn_exchargegift.setSelected(false);
            btn_love.setSelected(false);
            btn_luckymoney.setSelected(false);

            
            break;
            
        case R.id.radio_button2:
            setContainerView(CONTENT_ACTIVITY_NAME_2, ExchargeCenterActivity.class);
            btn_my.setSelected(false);
            btn_taskcenter.setSelected(false);
            btn_exchargegift.setSelected(true);
            btn_love.setSelected(false);
            btn_luckymoney.setSelected(false);
            break;
            
        case R.id.radio_button3:
            setContainerView(CONTENT_ACTIVITY_NAME_3, MyMoreActivity.class);
           
            btn_my.setSelected(false);
            btn_taskcenter.setSelected(false);
            btn_exchargegift.setSelected(false);
            btn_love.setSelected(true);
            btn_luckymoney.setSelected(false);
            break;
            
        case R.id.radio_button_luckymoney:
            setContainerView(CONTENT_ACTIVITY_NAME_luckymoney, MyLuckyMoneyActivity.class);
     
            btn_my.setSelected(false);
            btn_taskcenter.setSelected(false);
            btn_exchargegift.setSelected(false);
            btn_love.setSelected(false);
            btn_luckymoney.setSelected(true);
           /* if (isAccessibilitySettingsOn(this)) {
  	          //  this.asSwitch.setImageResource(R.mipmap.open);
  	        } else {
  	        //	Toast.makeText(this, "抢红包服务未开启,请找到“简单的抢红包辅助”，然后开启服务即可", Toast.LENGTH_LONG).show();
  	        	// startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
  	            }
*/
  	         
               
            break;    
            
   
               
       
        default:
            break;
        }
	}

}
