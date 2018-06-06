package com.jiandande.review;

import java.util.ArrayList;
import java.lang.ref.WeakReference;




import net.youmi.android.AdManager;
//import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsChangeNotify;









import com.baidu.mobads.appoffers.PointsChangeListener;
import com.jiandande.review.bean.HistoryMoney;
import com.jiandande.review.bean.MessageBean;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.transcation.AddMoneyAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.RsaDesHelper;
import com.jiandande.review.util.StringUtil;
import com.jiandande.review.util.SystemUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.PlatType;

import android.app.Activity;
import android.app.Application;

public class MainApplication extends Application implements PointsChangeNotify
{
	String ordermessage="";
	int messagestatus=7;
	String retStrFormatNowDate;
	WeakReference<Activity> mActivityWref;
	UserInfo  userInfo;
	
	ArrayList<MessageBean>messages;
	
	public ArrayList<MessageBean> getMessages() {
		return messages;
	}
	public void setMessages(ArrayList<MessageBean> messages) {
		this.messages = messages;
	}

	RsaDesHelper rsaDeHelper;
	
	public static MainApplication app ;
	AddMoneyAction addmoneyAction;
	private ArrayList<Activity>activityList;
	
	private ArrayList<HistoryMoney>historyMoneys;
	

	
      @Override
    public void onCreate() {
    	
    	  
    	  super.onCreate();
    	  /*boolean  breturn=  cn.bmob.statistics.AppStat.i("54bb7ee8a8acd477bf5f19393e7d9c05", "");
    		if (breturn){
			//	Toast.makeText(this, "开启bmob统计SDK成功", Toast.LENGTH_LONG).show();
			}
			else
			{
				//Toast.makeText(this, "开启bmob统计SDK失败", Toast.LENGTH_LONG).show();
			}*/
  // 	boolean  breturn=  cn.bmob.statistics.AppStat.i("54bb7ee8a8acd477bf5f19393e7d9c05", "");
    
    	  //面霸的
    	  //AdManager.getInstance(this).init("0151ff3b0401a197",
   			//	"361823815e9b4cac", false);
    	
    	  //有米
    	// userid 不能为空 或者 空串,否则设置无效, 字符串长度必须要小于50
    	// OffersManager.getInstance(this).setCustomUserId(String userid);

    	  // 有米Android SDK v4.10之后的sdk还需要配置下面代码，以告诉sdk使用了服务器回调
    	//  OffersManager.getInstance(this).setUsingServerCallBack(true);
    	  AdManager.getInstance(this).init("434afaf01560ec82", "ce011c60e3c6c087", false , true );
   		//// 如果使用积分广告，请务必调用积分广告的初始化接口:
   		//  OffersManager.getInstance(this).onAppLaunch();
   	// (可选)注册积分监听-随时随地获得积分的变动情况
  		//PointsManager.getInstance(this).registerNotify(this);
  		
    	  activityList=new ArrayList<Activity>();
    	  rsaDeHelper=RsaDesHelper.getInstance(getBaseContext());
    	  app=this;
    //	  PointsManager.getInstance(this).registerNotify(this);
    	  addmoneyAction=new AddMoneyAction();
    	 
    	  com.baidu.mobads.appoffers.OffersManager.setPointsChangeListener(new PointsChangeListener(){
    	  
    	  			@Override
    	  			public void onPointsChanged(int pointsBalance) {
//    	  				Log.d("onPointsChanged", "total points is: "+arg0);
//    	  				tv.setText(""+arg0);
    	  			   addmoneyAction.setMoney(pointsBalance);
    	  			   addmoneyAction.setSource("APP_DWONLOAD");  	  			   
    	  			   addmoneyAction.setPlatType(PlatType.BAIDU);
    	  			   finishAddmoney();
    	  			   //String count=PreferenceUtil.getStrValue(getBaseContext(),ParamName.BAIDU_DOWNLOAD, "0");
    	  			   //int downCount=Integer.parseInt(count)+1;
    	  			  //PreferenceUtil.saveStrValue(getBaseContext(),ParamName.BAIDU_DOWNLOAD,downCount+"");
    	  			}
    	  			
    	  		});
    
    }
	
     
     
	public RsaDesHelper getRsaDeHelper() {
		return rsaDeHelper;
	}
	public WeakReference<Activity> getWeakRef() {
		return mActivityWref;
	}
	public void setWeakRef(WeakReference<Activity> mActivityWref) {
		this.mActivityWref = mActivityWref;
	}
	
	public UserInfo getUserInfo() {
		return userInfo;
	}



	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}



	public ArrayList<HistoryMoney> getHistoryMoneys() {
		return historyMoneys;
	}
	public void setHistoryMoneys(ArrayList<HistoryMoney> historyMoneys) {
		this.historyMoneys = historyMoneys;
	}
	public String getretStrFormatNowDate() {
		return retStrFormatNowDate;
	}
	public void setretStrFormatNowDate(String retStrFormatNowDate) {
		this.retStrFormatNowDate = retStrFormatNowDate;
	}
	
	
	public String getOrdermessageInfo() {
		return ordermessage;
	}
	
	public void setOrdermessageInfo(String ordermessage) {
		this.ordermessage = ordermessage;
	}
	public int getmessagestatus() {
		return messagestatus;
	}
	public void setmessagestatus(int messagestatus) {
		this.messagestatus = messagestatus;
	}
	/**
	 * 退出程序
	 */
	public void exitAppaction()
	{
		OffersManager.getInstance(this).onAppExit();
		for(Activity activity : activityList)
		{
			if(activity!=null && (! activity.isFinishing()))
			{
				activity.finish();
			}
		}
		onTerminate();
	//	PointsManager.getInstance(this).unRegisterNotify(this);
		// 释放资源，原finalize()方法名修改为close()

	}
	
	
   public void addActivity(Activity activity){
	    activityList.add(activity);
   }
   
   public void removeActivity(Activity activity){
		  activityList.remove(activity);
	   }



  // @Override
   public void onPointBalanceChange(int pointsBalance) {
	   addmoneyAction.setMoney(pointsBalance);
	   addmoneyAction.setSource("APP_DWONLOAD");
	   addmoneyAction.setPlatType(PlatType.YOUMI);
	   finishAddmoney();
	  // String count=PreferenceUtil.getStrValue(getBaseContext(),ParamName.YOUMI_DOWNLOAD, "0");
       //int downCount=Integer.parseInt(count)+1;
	  // PreferenceUtil.saveStrValue(getBaseContext(),ParamName.YOUMI_DOWNLOAD,downCount+"");
   } 
   
  void finishAddmoney(){
	   String imei=PreferenceUtil.getStrValue(getBaseContext(), ParamName.IMEI, "");
		if(StringUtil.checkIsNull(imei)){
			imei=new SystemUtil(this).getNewPhoneIMEI();
			PreferenceUtil.saveStrValue(getBaseContext(), ParamName.IMEI, imei);
		}
	   addmoneyAction.setImei(imei);
	   addmoneyAction.transStart();
	   addmoneyAction.setTransferPayResponse(addmoneyResond);
   }
   int count=0;
   private TransferPayResponse addmoneyResond=new TransferPayResponse() {
	
	@Override
	public void transFailed(String httpCode, String errorInfo) {
		if(count<3){
			addmoneyAction.transStart();
			count++;
		}
	}	
	@Override
	public void transComplete() {
		if(HttpCodeHelper.RESPONSE_SUCCESS.equals(addmoneyAction.respondData.respCode)){
			userInfo.balance=userInfo.balance+addmoneyAction.respondData.amount;
			userInfo.sumCount=userInfo.sumCount+addmoneyAction.respondData.amount;
		}else{
			if(count<3){
				addmoneyAction.transStart();
				count++;
			}
		}
	}
  };

@Override
public void onPointBalanceChange(float arg0) {
	// TODO Auto-generated method stub
	
}
}
