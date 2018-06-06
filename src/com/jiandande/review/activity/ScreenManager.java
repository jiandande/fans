package com.jiandande.review.activity;

import java.lang.ref.WeakReference;

import com.jiandande.money.R;
import com.jiandande.review.MainApplication;
import com.jiandande.review.bean.UserInfo;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.Toast;

public class ScreenManager {

    private Context mContext;
    MainApplication app;
  //  private WeakReference<Activity> mActivityWref;

    public static ScreenManager gDefualt;

    public static ScreenManager getInstance(Context pContext) {
        if (gDefualt == null) {
            gDefualt = new ScreenManager(pContext.getApplicationContext());
        }
        return gDefualt;
    }
    private ScreenManager(Context pContext) {
        this.mContext = pContext;
    }
    public void setActivity(Activity pActivity) {
       	app=(MainApplication) this.mContext.getApplicationContext();
    
       	if (app.getWeakRef()==null)
    	{
    		app.setWeakRef(new WeakReference<Activity>(pActivity));
    	//	Toast.makeText(mContext, " setWeakRef", Toast.LENGTH_LONG).show();
    	}
    }

    public void startActivity() {
            LiveActivity.actionToLiveActivity(mContext);
    }

 /*   public void onBack(){  
    	  new Thread(){  
    	   public void run() {  
    	    try{  
    	     Instrumentation inst = new Instrumentation();  
    	     inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);  
    	    }  
    	    catch (Exception e) {  
    	              //   Log.e("Exception when onBack", e.toString());  
    	             }  
    	   }  
    	  }.start();  
    	 }  */
    public void finishActivity() {
        //结束掉LiveActivity
    	Toast.makeText(mContext, "start littleredhat", Toast.LENGTH_LONG).show();
       	app=(MainApplication) this.mContext.getApplicationContext();
    	WeakReference<Activity>  mActivityWref=app.getWeakRef();
        if (mActivityWref != null) {
            Activity activity = mActivityWref.get();
            if (activity != null) {
                activity.finish();
                app.setWeakRef(null);
          //  	Toast.makeText(mContext, " activity.finish()", Toast.LENGTH_LONG).show();
            }          
        }
    }
}
