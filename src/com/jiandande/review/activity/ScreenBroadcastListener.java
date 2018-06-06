package com.jiandande.review.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

public class ScreenBroadcastListener {

    private Context mContext;

    private ScreenBroadcastReceiver mScreenReceiver;

    private ScreenStateListener mListener;

    public ScreenBroadcastListener(Context context) {
        mContext = context.getApplicationContext();
        mScreenReceiver = new ScreenBroadcastReceiver();
    }

    public interface ScreenStateListener {

        void onScreenOn();

        void onScreenOff();
    }

    /**
     * screen状态广播接收者
     */
    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
                mListener.onScreenOn();
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
                mListener.onScreenOff();
            }
        	Toast.makeText(context,  "服务已开启2" , Toast.LENGTH_SHORT).show();	
         //  	Toast.makeText(context,  intent.getBooleanExtra("state", false) ? "服务已开启2" : "服务已关闭2", Toast.LENGTH_SHORT).show();	
   	     
        }
    }
    
    /** 
     * @author 编写人： xiaox 
     * @date 创建时间： 2016/8/20 
     * @Description 功能描述： 该类用来实现该app开机自动运行 
     */  
    public class BootBroadcastReceiver extends BroadcastReceiver {  
      
        /** 
         * demo2: 可以实现开机自动打开软件并运行。 
         */  
        @Override  
        public void onReceive(Context context, Intent intent) {  
        	Toast.makeText(context,  "服务已开启2" , Toast.LENGTH_SHORT).show();	
          //  Log.d("XRGPS", "BootReceiver.onReceive: " + intent.getAction());  
           /* System.out.println("自启动程序即将执行");  
        //MainActivity就是开机显示的界面  
            Intent mBootIntent = new Intent(context, LoginActivity.class);  
        //下面这句话必须加上才能开机自动运行app的界面  
            mBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
            context.startActivity(mBootIntent);  */
      
        }  
    }  
    
    

    public void registerListener(ScreenStateListener listener) {
        mListener = listener;
        registerListener();
    }
    /**
     * 停止screen状态监听
     */
    public void unregisterListener() {
        mContext.unregisterReceiver(mScreenReceiver);
    }    
   

    private void registerListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mContext.registerReceiver(mScreenReceiver, filter);
    }
    
}