package com.jiandande.review.util;
import java.util.List;

import com.jiandande.review.MainApplication;
import com.jiandande.review.activity.OwnBusinessTaskListActivity;
import com.jiandande.review.bean.ExchangeBean;
import com.jiandande.review.transcation.OwnBusinessTaskAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.Constants.ParamName;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;  
import android.os.CountDownTimer;  
import android.text.Spannable;  
import android.text.SpannableString;  
import android.text.style.ForegroundColorSpan;  
import android.widget.Button;
import android.widget.TextView;  
import android.widget.Toast;
  
public class CountVisitWebsiteTimerUtils extends CountDownTimer {  
    private ExchangeBean mExchangeBean;  
    private Context mContext;  
	OwnBusinessTaskAction OwnBusinessTaskAction;
  
 
    public CountVisitWebsiteTimerUtils(Context contex,ExchangeBean ExchangeBean, long millisInFuture, long countDownInterval) {  
        super(millisInFuture, countDownInterval);  
        this.mExchangeBean = ExchangeBean;  
        this.mContext = contex;  
    }  
  
    
    private boolean isAppOnForeground(Context contex) {
    	ActivityManager activityManager =(ActivityManager) contex.getApplicationContext().getSystemService(
    	   Context.ACTIVITY_SERVICE);
    	String packageName2 =contex.getApplicationContext().getPackageName();
    	ComponentName cn=activityManager.getRunningTasks(1).get(0).topActivity;
   // 	Toast.makeText(CountVisitWebsiteTimerUtils.this, "访问网址成功111", Toast.LENGTH_LONG).show();
       	Toast.makeText(contex, "每天浏览网页2分钟即可得22积分~~", Toast.LENGTH_LONG).show();	  
    	List<RunningAppProcessInfo>appProcesses = activityManager.getRunningAppProcesses();
    	if (appProcesses == null)
    	return false;
     String	packageName="com.android.browser";
     //	Toast.makeText(contex, "访问网址成功666", Toast.LENGTH_LONG).show();	
  
     	
    	for (RunningAppProcessInfo appProcess : appProcesses) {
    	if (appProcess.processName.equals(packageName)
    	       && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
   
    	   return true;
    	}
    	}
    	return false;
    	}
    @Override  
    public void onTick(long millisUntilFinished) {  
        
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);  
        isAppOnForeground(mContext);
      //  mTextView.setText(spannableString);  
    }  

  	
    @Override  
    public void onFinish() {  
    	Toast.makeText(mContext, "恭喜你,任务已达成,可以返回!", Toast.LENGTH_LONG).show();	
    	
        
     //   mTextView.setText("重新获取验证码");  
     //   mTextView.setClickable(true);//重新获得点击  
    //    mTextView.setBackgroundResource(R.drawable.bg_identify_code_normal);  //还原背景色  
    }  
}  
