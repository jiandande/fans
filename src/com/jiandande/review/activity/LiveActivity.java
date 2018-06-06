package com.jiandande.review.activity;

import com.jiandande.money.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class LiveActivity extends Activity  {
    public static final String TAG = LiveActivity.class.getSimpleName();
    public static void actionToLiveActivity(Context pContext) {
        Intent intent = new Intent(pContext, LiveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (pContext!=null){
        pContext.startActivity(intent);
        }
    }

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_live);
        Window window = getWindow();
        //放在左上角
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams attributes = window.getAttributes();
        //宽高设计为1个像素
        attributes.width = 1;
        attributes.height = 2;
        //起始坐标
        attributes.x = 0;
        attributes.y = 0;
        window.setAttributes(attributes);
        ScreenManager.getInstance(this).setActivity(this);
     /*   this.app = (MainApplication) super.getApplication();
        ScreenManager.getInstance(this).setActivity(this);
        
        final ScreenManager screenManager = ScreenManager.getInstance(LiveActivity.this);
        
        ScreenBroadcastListener listener = new ScreenBroadcastListener(this);
         listener.registerListener(new ScreenBroadcastListener.ScreenStateListener() {
           @Override
            public void onScreenOn() {
        	
             screenManager.finishActivity();
    
         //   	UserInfo user=app.getUserInfo();
	//			user.paymessage=user.paymessage+"onScreenOn-screenManager.finishActivity()";
            }

            @Override
            public void onScreenOff() {
                screenManager.startActivity();
            //	UserInfo user=app.getUserInfo();
        	//	user.paymessage=user.paymessage+"onScreenOff-screenManager.startActivity()";
            	
            }
        });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
