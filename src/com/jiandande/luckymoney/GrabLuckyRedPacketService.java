package com.jiandande.luckymoney;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.jiandande.review.MainApplication;
import com.jiandande.review.activity.IndexActivity;
import com.jiandande.review.activity.LoginActivity;
import com.jiandande.review.activity.PayRecordActivity;
import com.jiandande.review.activity.ScreenBroadcastListener;
import com.jiandande.review.activity.ScreenManager;
import com.jiandande.review.bean.HistoryMoney;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.transcation.ClientLoginAction;
import com.jiandande.review.transcation.ClientRegisterAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.transfer.UploadUtil;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.FileUtil;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.StringUtil;
import com.jiandande.review.util.SystemUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.PlatType;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import com.jiandande.review.transfer.UploadUtil;


@SuppressLint("NewApi")
public class GrabLuckyRedPacketService extends AccessibilityService
{
    //Intent构造器里面的变量是一个字符串，通常在要更新UI的文件中进行定义  
    private Intent intent = new Intent(PayRecordActivity.ACTION_SERVICE_STATE_CHANGE);  
	
//当接收到多个群消息不能自动 拆开红包,考虑震动语音温馨提醒.
	static final String TAG = "GrabLuckyMoney";
	ScreenBroadcastListener listener;
	  private static GrabLuckyRedPacketService GRPservice = null;
	  MainApplication app = null;
	  ScreenManager screenManager;

	  /** 微信的包名 */
	static final String WECHAT_PACKAGENAME = "com.tencent.mm";

	
	

		
	 /**
	  * 
	  *  
	  *  QQ的包名*/
    public static final String QQ_PACKAGENAME = "com.tencent.mobileqq";
	/** 红包消息的关键字 */
	static final String WX_HONGBAO_TEXT_KEY = "[微信红包]";
	static final String QQ_HONGBAO_TEXT_KEY = "[QQ红包]";

	 /** 不能再使用文字匹配的最小版本号 */
    public static final int WECHAT_VERSION_MIN = 700;// 6.3.8 对应code为680,6.3.9对应code为700

    public static final String CLASS_NAME_BUTTON = "android.widget.Button";
    public static final String CLASS_NAME_IMAGEVIEW = "android.widget.ImageView";
   // public static final String CLASS_NAME_TEXTVIEW = "android.widget.TextView";
    public static final String CLASS_NAME_VIEW = "android.widget.TextView";//CLASS_NAME_VIEW = "android.view.View";
    public static final String CLASS_NAME_LISTVIEW = "android.widget.ListView";
    public static final String CLASS_NAME_FRAMEVIEW = "android.widget.FrameLayout";
    public static final String CLASS_NAME_LinearLayout= "android.widget.LinearLayout";
    public static final String CLASS_NAME_VIEW2 = "android.view.accessibility.AccessibilityNodeInfo";
	
//	public String[] QQparentrecord={"","","","","","","",""};
//	public String[] WXparentrecord={"","","","","","","",""};
//	public int j=1;
	public boolean isReceivingHongbao;
	public boolean isReceivingMessage;
	
	 private void sendAction(boolean state) {  
	        intent.putExtra("state", state);  
	        sendBroadcast(intent);  
	    }  
	
	
	private void delay(int ms){  
        try {  
            Thread.currentThread();  
            Thread.sleep(ms);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }   
     }    
	private void producerecordchatcontent(){  
           Thread.currentThread();  
          //  app.setOrdermessageInfo(app.getOrdermessageInfo()+strcontent);
            try {
				Thread.sleep(777);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
     // 	  Toast.makeText(this, "test123456", Toast.LENGTH_LONG).show();	 
          	  AccessibilityNodeInfo RootNode  = getRootInActiveWindow();
          	  if (RootNode==null){
      			//	  Toast.makeText(this, "test", Toast.LENGTH_LONG).show();	 
      					LogUtil.i("Debug", "TYPE_WINDOW_STATE_CHANGED return");
      				  return ;}
          	List<AccessibilityNodeInfo> tbzbroomchatList = RootNode.findAccessibilityNodeInfosByViewId("com.taobao.taobao:id/taolive_chat_item_content");
        //	List<AccessibilityNodeInfo> tbzbmsgstubinflated = RootNode.findAccessibilityNodeInfosByViewId("com.taobao.taobao:id/taolive_msg_stub_inflated");
        	if ((tbzbroomchatList!=null) && (tbzbroomchatList.size() != 0))
			{
        		//if (tbzbmsgstubinflated==null)
    		 //	{return;}
      		//	  Toast.makeText(this, "test", Toast.LENGTH_LONG).show();
      				int childCount = RootNode.getChildCount();
      			//	int nstatus =app.getmessagestatus();
      		   // 	app.setmessagestatus(2);
      				 for (int i = 0; i < childCount; i++) {
      					     AccessibilityNodeInfo node = RootNode.getChild(i);
      			            if ( node !=null) {
      			            		 
      				        	int childCount2 = node.getChildCount();
      				        	{
      				        		 for (int j = 0; j < childCount2; j++) {
      				      			   
      							            AccessibilityNodeInfo node2 = node.getChild(j);
      							            if(node2!=null){
      											}
      							            //直播登录购买消息    app.setOrdermessageInfo(app.getOrdermessageInfo()+""+node2.getText());//+node2.getContentDescription());//.getClassName()); 	
      						            		
      							            	int childCount3 = node2.getChildCount();
      								        	{
      								        		
      								        		    for (int k = 0; k < childCount3; k++) {
      								      			   
      											           AccessibilityNodeInfo node3 = node2.getChild(k);
      											            if(node3!=null){
      											            	if ((node3.getText()!=null)&&(node3.getText()=="null"))
      											            	{
      											          	    if (!app.getOrdermessageInfo().contains(node3.getText()))
																    {
      											          	    	if (app.getOrdermessageInfo()==null) {app.setOrdermessageInfo("");}
      											            	       app.setOrdermessageInfo(app.getOrdermessageInfo()+"\r\n"+node3.getText());//+node3.getContentDescription()); 
																    }
      											            	}
      											            	int childCount4 = node3.getChildCount();
      												        	{
      												        		
      												        		    for (int o = 0; o < childCount4; o++) {
      												      			   
      															           AccessibilityNodeInfo node4 = node3.getChild(o);
      															            if(node4!=null){
      															            	if (node4.getText()!=null)
      															            	{
      															            	 	   String NewMsg=node4.getText().toString();
      															         		    if (!app.getOrdermessageInfo().contains(NewMsg)&&(!NewMsg.contains("null")))
      																			    {
      															         		   	if (app.getOrdermessageInfo()==null) {app.setOrdermessageInfo("");}
      															                     	app.setOrdermessageInfo(app.getOrdermessageInfo()+"\r\n"+NewMsg); 	
      															         		
      																			    }
      									        
      															         
      															            	String ServiceNotification=node4.getText().toString();
      															     
      															            	int childCount5 = node4.getChildCount();
      																        	{
      																        		
      																        		    for (int g = 0; g < childCount5; g++) {
      																      			   
      																			           AccessibilityNodeInfo node5 = node4.getChild(o);
      																			            if(node5!=null){
      																			            	if (node5.getText()!=null)
      																			            	{
      																			            	app.setOrdermessageInfo(app.getOrdermessageInfo()+"  gosubsubsubnode.getClassName()"+node5.getText()+node5.describeContents()); 	
      																			            	}
      																			            }
      																        		    }
      																        		    }
      																        	}
      															            }
      															            
      												        		    }
      												        	}
      											            }
      								        		    }
      								        	}
      				        		 }}
      			            }}}									  
      													
      												  
      											 
      	  	
      			 
	}
      			     /*       	 	
      			            	 	if ((tbzbroomchatList!=null) && (tbzbroomchatList.size() != 0))
      	
       */
			      	 	
			     /*       	 	
			            	 	if ((tbzbroomchatList!=null) && (tbzbroomchatList.size() != 0))
			{
				String strMsgInfo="";
				for (AccessibilityNodeInfo nroomchat : tbzbroomchatList)
				{		
				AccessibilityNodeInfo chat_node;	
				AccessibilityNodeInfo pchat_node;	
				chat_node=nroomchat;
				   if (chat_node!=null)
				   {
					  pchat_node=chat_node.getParent().getParent();
					  if (pchat_node!=null)
					  {
						 String strtmpchat =""+pchat_node.getChild(1).getText();
						 String Ordermessage=app.getOrdermessageInfo();
						  //Toast.makeText(this, Ordermessage, Toast.LENGTH_LONG).show();	 
			
					    if (!Ordermessage.contains(strtmpchat))
					    {
						//	  Toast.makeText(this, strtmpchat, Toast.LENGTH_LONG).show();	 
					     strMsgInfo=strMsgInfo+  strtmpchat;//pchat_node.getChild(1).getText();
					    } 
					  }
			        }
				}
		//		  if ((app.getmessagestatus()==2)&&
				//	if (!strMsgInfo.contains("nullnul"))
				 // {
				     //   Toast.makeText(this, "test6", Toast.LENGTH_LONG).show();	 
						app.setOrdermessageInfo(app.getOrdermessageInfo()+strMsgInfo);
				// }
			}
			            }
				 }  
     }    */
	
	public class threadrecordchatcontent implements Runnable {  
	    @Override  
	    public void run() {  
	        // TODO Auto-generated method stub  
	          while  (true) {  
	            producerecordchatcontent();
                try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 线程暂停10秒，单位毫秒 
				Message message = new Message();  
				message.what = 2;  
				handler2.sendMessage(message);// 发送消息    
	        }  
	    }  
	}  
	Handler handler2 = new Handler() {  
	    public void handleMessage(Message msg) {  
	        // 要做的事情 
	        super.handleMessage(msg);  
	    }  
	
	};
	
	
	public class MyThread implements Runnable {  
	    @Override  
	    public void run() {  
	        // TODO Auto-generated method stub  
	        while (isReceivingHongbao) {  
	            try {  
	            //	startAPP(QQ_PACKAGENAME);
	                Thread.sleep(28000);// 线程暂停10秒，单位毫秒 
	    	
	                Message message = new Message();  
	                message.what = 1;  
	                handler.sendMessage(message);// 发送消息  
	            } catch (InterruptedException e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	            }  
	        }  
	    }  
	}  

	
	Handler handler = new Handler() {  
	    public void handleMessage(Message msg) {  
	        // 要做的事情 
	    	
	    	isReceivingHongbao=false;
			isReceivingMessage=false;
	        super.handleMessage(msg);  
	    }  
	};
	private int Map;  
    
    
	public void recycle(AccessibilityNodeInfo info) {
		if (info.getChildCount() == 0) {

		} else {
			for (int i = 0; i < info.getChildCount(); i++) {
				if(info.getChild(i)!=null){
					recycle(info.getChild(i));
				}
			}
		}
	}
	
	  public void recycle2(AccessibilityNodeInfo rootInActiveWindow) {
	        //只有一个子元素
	        if (rootInActiveWindow.getChildCount() == 0) {
	            Log.d(TAG,"the rootInActiveWindow count is : 0");
	        } else {
	            // 遍历所有子元素
	            for (int i = 0; i < rootInActiveWindow.getChildCount(); i++) {
	                AccessibilityNodeInfo nodeInfo = rootInActiveWindow.getChild(i);
	                Log.d(TAG,"the child "+i+"is :"+nodeInfo+"\n");
	            }
	        }
	    }
	  /*
	 * 启动一个app
	 */
	public void startAPP(String appPackageName){
	    try{
	        Intent intent = this.getPackageManager().getLaunchIntentForPackage(appPackageName);
	        startActivity(intent);
	    }catch(Exception e){
	        Toast.makeText(this, "没有安装", Toast.LENGTH_LONG).show();
	    }
	}
	 /* 上传文件至Server，uploadUrl：接收文件的处理页面 */  
	/*  private void uploadFile(String uploadUrl,String srcPath)  
	  {  
	    String end = "\r\n";  
	    String twoHyphens = "--";  
	    String boundary = "******";  
	    Toast.makeText(this, " uploadFile start"+srcPath, Toast.LENGTH_SHORT).show();
	    try  
	    {  
	      URL url = new URL(uploadUrl);  
	      HttpURLConnection httpURLConnection = (HttpURLConnection) url  
	          .openConnection();  
	      Toast.makeText(this, " httpURLConnection start"+uploadUrl, Toast.LENGTH_SHORT).show();
	      // 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃  
	      // 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。  
	      httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K  
	      // 允许输入输出流  
	      httpURLConnection.setDoInput(true);  
	      httpURLConnection.setDoOutput(true);  
	      httpURLConnection.setUseCaches(false);  
	      // 使用POST方法 

	      httpURLConnection.setRequestMethod("POST");  
	      httpURLConnection.setRequestProperty("Connection", "Keep-Alive");  
	      httpURLConnection.setRequestProperty("Charset", "UTF-8");  
	      httpURLConnection.setRequestProperty("Content-Type",  
	          "multipart/form-data;boundary=" + boundary);  
	  
	      DataOutputStream dos = new DataOutputStream(  
	          httpURLConnection.getOutputStream());  
	      dos.writeBytes(twoHyphens + boundary + end);  
	      dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""  //  /upload_tmp 
	          + srcPath.substring(srcPath.lastIndexOf("/") + 1)  
	          + "\""  
	          + end);  
	       Toast.makeText(this,uploadUrl+ "name=\"uploadedfile\"; filename=\"" + srcPath.substring(srcPath.lastIndexOf("/") + 1)  , Toast.LENGTH_LONG).show();
	      dos.writeBytes(end);  
	  
	      FileInputStream fis = new FileInputStream(srcPath);  
	      byte[] buffer = new byte[8192]; // 8k  
	      int count = 0;  
	      // 读取文件  
	      while ((count = fis.read(buffer)) != -1)  
	      {  
	        dos.write(buffer, 0, count);  
	      }  
	      fis.close();  
	  
	      dos.writeBytes(end);  
	      dos.writeBytes(twoHyphens + boundary + twoHyphens + end);  
	      dos.flush();  
	  
	      InputStream is = httpURLConnection.getInputStream();  
	      InputStreamReader isr = new InputStreamReader(is, "utf-8");  
	      BufferedReader br = new BufferedReader(isr);  
	      String result = br.readLine();  
	  
	      Toast.makeText(this, result, Toast.LENGTH_LONG).show();  
	      dos.close();  
	      is.close();  
	  
	    } catch (Exception e)  
	    {  
	      e.printStackTrace();  
	    //  setTitle(e.getMessage());  
	    }  
	  }  

	*/
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event)
	{
		final int eventType = event.getEventType(); // ClassName:
													// com.tencent.mm.ui.LauncherUI
	    CharSequence packageName = event.getPackageName();
	
		//   if ((!WX_HONGBAO_TEXT_KEY.equals(packageName)&&!QQ_PACKAGENAME.equals(packageName))){return;}
		// 通知栏事件
		/*	 if (eventType == 0x000100000)
			{
					app.setOrdermessageInfo("");
			}
			 else */ if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED)
		{
	
			  String tip = event.getText().toString();
		
		//		LogUtil.i("Debug", "getEventType:"+Integer.toString(eventType)+"tip:"+tip);
			// Toast.makeText(this, "000"+tip+event.getContentDescription(), Toast.LENGTH_SHORT).show();
				//	isReceivingHongbao=true;//接收到红包
			//if ((!tip.contains(WX_HONGBAO_TEXT_KEY))&&(!tip.contains(QQ_HONGBAO_TEXT_KEY))) return ;
		//	LogUtil.i("Debug", "getEventType:"+Integer.toString(eventType)+" 通知栏事件");
			  String messagetip="";
			List<CharSequence> texts = event.getText();	
			if (!texts.isEmpty())
			{
				for (CharSequence t : texts)
				{	
					String text = String.valueOf(t);
					messagetip = messagetip+text;
			//		LogUtil.i("Debug", "getEventType:TYPE_NOTIFICATION_STATE_CHANGED"+Integer.toString(eventType)+"text:"+text);
				//	if (text.contains(WX_HONGBAO_TEXT_KEY) || text.contains(QQ_HONGBAO_TEXT_KEY))
					//{
						//  Toast.makeText(this, "isReceivingHongbao=true"+tip, Toast.LENGTH_SHORT).show();
					//	isReceivingHongbao=true;//接收到红包
					//	isReceivingMessage=false;
				//		new Thread(new MyThread()).start();  
				//	}
					
			
				//		LogUtil.i("Debug", "openNotify:"+text);
				//	new Thread(new MyThread()).start();  
			      //  	openNotify(event);
					//	break;
				//	}
				}
				FileUtil.writeContentTofile(messagetip)	;	
		//		UserInfo user=app.getUserInfo();
		//		user.paymessage=user.paymessage+messagetip;
				app.setOrdermessageInfo(app.getOrdermessageInfo()+messagetip);
				//Toast.makeText(this,user.paymessage , Toast.LENGTH_SHORT).show();
				if(messagetip.contains("收款到账通知")) // if(messagetip.contains("收款到账通知"))//微信商户订单通知Service Notification 
	            {
	            	isReceivingHongbao=true;
	            	new Thread(new MyThread()).start();  
	            	openNotify(event);
	            	app.setmessagestatus(7);
	           // 	uploadFile("http://www.jiandande.com/test_tmp.php","/mnt/sdcard/mylog/realname/javalog_" + System.currentTimeMillis() + ".txt");
	            }            
				
			}
		} else if (eventType == AccessibilityEvent.CONTENT_CHANGE_TYPE_SUBTREE)
		{
			  String tip = event.getText().toString()+event.getContentDescription();
			//  UserInfo user=app.getUserInfo();
			 // user.paymessage=user.paymessage+tip;
			//  app.setOrdermessageInfo(app.getOrdermessageInfo()+tip);
			  if (tip.contains("收款金额"))
			  {
			   if (app!=null)
			   {
					UserInfo user=app.getUserInfo();
					StringUtil.getUserName(user);
					tip.replaceAll(" ","");
					FileUtil.writeContentTofile(tip)	;
					UploadUtil UploadUtil2;
					UploadUtil2=UploadUtil.getInstance();
			
					UploadUtil2.uploadFile("/mnt/sdcard/mylog/realname/javalog_" + StringUtil.getUserName(user)+"id"+app.getretStrFormatNowDate() + ".txt","uploadedfile","http://www.jiandande.com/test_tmp.php", 	null);
		        //  uploadFile("http://www.jiandande.com/test_tmp.php","/mnt/sdcard/mylog/realname/javalog_" + app.getretStrFormatNowDate() + ".txt");
					app.setOrdermessageInfo(app.getOrdermessageInfo()+tip);
					app.setOrdermessageInfo(app.getOrdermessageInfo()+"uploadFile(http://www.jiandande.com/test_tmp.php"+ app.getretStrFormatNowDate() + ".txt");
		      //    Toast.makeText(this, " uploadFile(http://www.jiandande.com/test_tmp.php"+ app.getretStrFormatNowDate() + ".txt", Toast.LENGTH_SHORT).show();
			   }
			  }   
			//Toast.makeText(this, "eventType--CONTENT_CHANGE_TYPE_SUBTREE"+tip, Toast.LENGTH_SHORT).show();
			
			// 从微信主界面进入聊天界面
		//	LogUtil.i("Debug", " CONTENT_CHANGE_TYPE_SUBTREE从微信主界面进入聊天界面:openHongBao(event);"+Integer.toString(eventType));
		//	isReceivingHongbao=true;
		//2	if (isReceivingHongbao)
	//		{
				delay(666);
				//openHongBao(event);
				AccessibilityNodeInfo rootNode  = getRootInActiveWindow();
		//		 int childCount = rootNode.getChildCount();
				if (rootNode!=null)
				{
			//	app.setOrdermessageInfo(app.getOrdermessageInfo()+"点击文字事情");//rootNode); 
			//	  Toast.makeText(this, tip, Toast.LENGTH_LONG).show();
			/*	
			   List<AccessibilityNodeInfo> list3 = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ga");			            	
		//		   Toast.makeText(this, "ListView:idcom.tencent.mm:id/ga", Toast.LENGTH_LONG).show();
						
				if (list3 != null && list3.size() != 0)
				{
					
				for (AccessibilityNodeInfo n3 : list3)
				{
				Toast.makeText(this, "++ListView:id/a_d-wechat:id/ga team 打开页面后第二个页面"+n3.getClassName(), Toast.LENGTH_LONG).show();
				AccessibilityNodeInfo parent_node2;		
				parent_node2=n3.getParent();
				if (parent_node2!=null)
				{
					Toast.makeText(this, "+ Welcome to parent_node2 "+parent_node2.getClassName(), Toast.LENGTH_LONG).show();
					  String strMsgInfo3=""+n3.getText();
				    if (strMsgInfo3.contains("微信支付凭证"))
					  {
							parent_node2.performAction(AccessibilityNodeInfo.ACTION_CLICK); 
							return ;
					  }
				  }   
				}
				}
				
				*/
				}
				
				
				//isReceivingHongbao=false;
		//	};
			
		} else if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
		{	
		   //  openHongBao(event);	
			Boolean bQRcode=false;
			String tip = event.getText().toString()+event.getContentDescription();
			//  tip=tip+event.getSource() ;
			  app.setOrdermessageInfo(app.getOrdermessageInfo()+tip);
			//	isReceivingHongbao=true;
			//	Toast.makeText(this, "222 eventType--TYPE_WINDOW_STATE_CHANGED"+tip, Toast.LENGTH_SHORT).show();
			// 处理微信聊天界面
			LogUtil.i("Debug", "TYPE_WINDOW_STATE_CHANGED处理微信聊天界面openHongBao");
		//	Toast.makeText(this, "eventType--TYPE_WINDOW_STATE_CHANGED", Toast.LENGTH_SHORT).show();	
			  AccessibilityNodeInfo RootNode  = getRootInActiveWindow();
			  if (RootNode==null){
					LogUtil.i("Debug", "TYPE_WINDOW_STATE_CHANGED return");
				  return ;}
			//  Toast.makeText(this, "TYPE_WINDOW_STATE_CHANGED—test"+RootNode.getClassName(), Toast.LENGTH_LONG).show();
				
			
		
				/*    
			  if (rowNode == null) {
	               // Log.d(TAG,"the rootInActiveWindow is null");
	            } else {
	                recycle2(rowNode);
	            }
			  */
				int childCount = RootNode.getChildCount();
				int nstatus =app.getmessagestatus();
		    	app.setmessagestatus(2);
		//		app.setOrdermessageInfo(app.getOrdermessageInfo()+"classname"+RootNode.getClassName()+"TYPE_WINDOW_STATE_CHANGED 共有"+childCount+"记录数"); 
				 for (int i = 0; i < childCount; i++) {
			            AccessibilityNodeInfo node = RootNode.getChild(i);
			            if ( node !=null) {
			         //   	Toast.makeText(this, "+++二维码test"+node.getContentDescription(), Toast.LENGTH_LONG).show();	

							List<AccessibilityNodeInfo> list2 = node.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/apt");			            	
						//	   Toast.makeText(this, "com.alipay.android.phone.messageboxapp:id/tv_name", Toast.LENGTH_LONG).show();
							List<AccessibilityNodeInfo> list_view_tvname = node.findAccessibilityNodeInfosByViewId("com.alipay.android.phone.messageboxapp:id/tv_name");		
							List<AccessibilityNodeInfo> zfbList = node.findAccessibilityNodeInfosByText("二维码");		
							List<AccessibilityNodeInfo> tbzbroomchatList = node.findAccessibilityNodeInfosByViewId("com.taobao.taobao:id/taolive_chat_item_content");
							if ((tbzbroomchatList!=null) && (tbzbroomchatList.size() != 0))
							{
								/*String strMsgInfo="";
						
					          //   app.setmessagestatus(2);
								for (AccessibilityNodeInfo nroomchat : tbzbroomchatList)
								{		
								AccessibilityNodeInfo chat_node;	
								AccessibilityNodeInfo pchat_node;	
								chat_node=nroomchat;
								   if (chat_node!=null)
								   {
									// String strMsgInfo=""+chat_node.getText();
									  pchat_node=chat_node.getParent().getParent();
									  if ((pchat_node!=null))//&& !strMsgInfo.contains("直播印记"))
									  {
									  strMsgInfo=strMsgInfo+  pchat_node.getChild(1).getText();
									//  strMsgInfo=strMsgInfo+  pchat_node.getChild(2)。getChild(1).getText();
									///  strMsgInfo=strMsgInfo+  pchat_node.getChild(3).getChild(1).getText();
									//  strMsgInfo=strMsgInfo+  pchat_node.getChild(4).getChild(1).getText();
									  }
					
									  Toast.makeText(this, strMsgInfo, Toast.LENGTH_LONG).show();	  
									  
							        }*/
								}
								  if (app.getmessagestatus()==2)
								  {
									  new Thread(new threadrecordchatcontent()).start(); 
									//  producerecordchatcontent();
									  app.setmessagestatus(nstatus);
									  return ;
								//Toast.makeText(this, strMsgInfo, Toast.LENGTH_LONG).show();	 
								  }
								//app.setmessagestatus(nstatus);
							//}
								
								
							  if (app.getmessagestatus()==7)
							  {
						//	  List<AccessibilityNodeInfo> layout_item_content_money = node.findAccessibilityNodeInfosByText("￥");//￥0.00
							  List<AccessibilityNodeInfo> layout_item_content_money = node.findAccessibilityNodeInfosByText("com.alipay.android.phone.messageboxapp:id/layout_item_content_BN");//￥0.00  
                    		  {
                    			  if (layout_item_content_money != null )
                    			  {
                    					for (AccessibilityNodeInfo item_money2 : layout_item_content_money){

                    			//			AccessibilityNodeInfo parent = item_money2.getParent();

                    						
                  					//	  Toast.makeText(this, "收款金额1", Toast.LENGTH_LONG).show();
        					
                    					//	  Toast.makeText(this, "收款金额"+item_money2.getChild(0).getChild(0).getChild(0).getText(), Toast.LENGTH_LONG).show();

        								}
                    				  
                    				//  Toast.makeText(this, "收款金额2", Toast.LENGTH_LONG).show();
                    				 
                    				  app.setmessagestatus(1);
                    				  
                    			  }
                    	 }
							  }
                    		  
						    if ((zfbList!=null)&&(!zfbList.isEmpty()))
							{
						    
								for (AccessibilityNodeInfo n2 : zfbList){
									bQRcode = true;
					
								//	Toast.makeText(this, "+++二维码test"+n2.getText(), Toast.LENGTH_LONG).show();	

								}
						
							}
						 /*   List<AccessibilityNodeInfo> listcontent_desc = node.findAccessibilityNodeInfosByViewId("com.eg.android.AlipayGphone");	
			                  if ((listcontent_desc!=null))
			                   {
			                	  Toast.makeText(this, "+++desc交易成功", Toast.LENGTH_LONG).show();	 
			                   }
					     
						*/

							if (list_view_tvname != null && list_view_tvname.size() != 0)
							{
								for (AccessibilityNodeInfo n : list_view_tvname)
								{
							//2	Toast.makeText(this, "+++ListView:id/c5q-wechat:id/apt team"+n.getClassName(), Toast.LENGTH_LONG).show();
								
								AccessibilityNodeInfo parent_node;		
								parent_node=n.getParent();
								if (parent_node!=null)
								{
									  String strMsgInfo=""+n.getText();
									  if (strMsgInfo.contains("支付助手"))
									  {
								//	 Toast.makeText(this, "+ 支付助手 "+parent_node.getClassName(), Toast.LENGTH_LONG).show();			  
										  if (app.getmessagestatus()==7)
										  {
											//  List<AccessibilityNodeInfo> zfbList = n.findAccessibilityNodeInfosByText("二维码收款到账通知");
											//	if (zfbList == null || zfbList.size() ==0) return;
											
                                           if (bQRcode)
                                           {
											//parent_node.performAction(AccessibilityNodeInfo.ACTION_CLICK); 
                                  
											app.setmessagestatus(1);
                                           }
										  }
											return ;
									  }
								 }
								 }
								
							}
							if (list2 != null && list2.size() != 0)
							{	
							for (AccessibilityNodeInfo n2 : list2)
							{
						//2	Toast.makeText(this, "+++ListView:id/c5q-wechat:id/apt team"+n.getClassName(), Toast.LENGTH_LONG).show();
							AccessibilityNodeInfo parent_node2;		
							parent_node2=n2.getParent();
							if (parent_node2!=null)
							{
							//3	Toast.makeText(this, "+ Welcome to parent_node "+parent_node.getClassName(), Toast.LENGTH_LONG).show();
								  String strMsgInfo=""+n2.getText();
								  if (strMsgInfo.contains("微信支付凭证"))
							  //  if (strMsgInfo.contains("Welcome"))
								  {
								//	  Toast.makeText(this, "+ 微信支付凭证 "+parent_node.getClassName(), Toast.LENGTH_LONG).show();
									 
										parent_node2.performAction(AccessibilityNodeInfo.ACTION_CLICK); 
									
										return ;
								  }
							}
						
						
								/*parent_node=parent_node.getParent().getParent();
								if (parent_node!=null)
								{
							     String strMsgInfo=""+parent_node.getText();
								 Toast.makeText(this, "+ Welcome to parent_node"+parent_node.getClassName(), Toast.LENGTH_LONG).show();
								       
							
								 parent_node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
							//	Toast.makeText(this, "+ Welcome to team"+n.getClassName(), Toast.LENGTH_LONG).show();
			       				
						
								}*/
							}
						
						}		
			        	
	
						
			            	
			            	
			          //   delay(8000); 	
			         //   app.setOrdermessageInfo(app.getOrdermessageInfo()+"++node.getClassName()"+node.getClassName()); 
			        	int childCount2 = node.getChildCount();
			        	{
			        		 for (int j = 0; j < childCount2; j++) {
			      			   
						            AccessibilityNodeInfo node2 = node.getChild(j);
						            if(node2!=null){
						            	
						            	
						            	
						        	    
									    List<AccessibilityNodeInfo> list3 = node.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/aa4");			            	
									
										if (list3 != null && list3.size() != 0)
										{
											//   Toast.makeText(this, "ListView:idcom.tencent.mm:id/aa4", Toast.LENGTH_LONG).show();//ga a_d
											    AccessibilityNodeInfo parent_node_wx=null;			
										for (AccessibilityNodeInfo n3 : list3)
										{
									//	Toast.makeText(this, "++ListView:id/a_d-wechat:id/ga team 打开页面后第二个页面"+n3.getClassName(), Toast.LENGTH_LONG).show();
										AccessibilityNodeInfo parent_node2;		
										parent_node2=n3.getParent();
										if (parent_node2!=null)
										{
										// 	Toast.makeText(this, "+ Welcome to parent_node2 "+parent_node2.getClassName()+parent_node2.getText(), Toast.LENGTH_LONG).show();
											  String strMsgInfo3=""+n3.getText();
											  if (strMsgInfo3.contains("收款成功"))//if (strMsgInfo3.contains("微信支付凭证"))
										      {
													//parent_node2.performAction(AccessibilityNodeInfo.ACTION_CLICK); 
													parent_node_wx=parent_node2;
												//	return ;
											  }
										  }   
										}
										  if (parent_node_wx!=null)
										  {
											//	Toast.makeText(this, "+ parent_node_wx "+parent_node_wx.getText(), Toast.LENGTH_LONG).show();
											  if (app.getmessagestatus()==7)
											  {
											  parent_node_wx.performAction(AccessibilityNodeInfo.ACTION_CLICK); 
												app.setmessagestatus(1);
											  }
											return ;
										  }
										    
										}
									
						            	
						            	//if (node2.getText()!=null)
						            //	{
						            	 
						            	/*	if (CLASS_NAME_LISTVIEW.equals(node2.getClassName()))
						            		{
						            			 
						            	        app.setOrdermessageInfo(app.getOrdermessageInfo()+"subnode.getClassName()"+node2.getClassName()+node2);//.getClassName()); 	
						            		
						            	     	int childCount3 = node2.getChildCount();
									        	{
									        		
									        		    for (int k = 0; k < childCount3; k++) {
									      			   
												           AccessibilityNodeInfo node3 = node2.getChild(k);
												            if(node3!=null){
												            
												            	app.setOrdermessageInfo(app.getOrdermessageInfo()+"  subsubnode.getClassName()"+node3); 	
												      
												                            }           	
						            		                                             } 	
									        	}*/
									      //  }
						            //	}
						                app.setOrdermessageInfo(app.getOrdermessageInfo()+"subnode.getClassName()"+node2.getClassName()+node2.getText()+node2.getContentDescription());//.getClassName()); 	
					            		
						            	int childCount3 = node2.getChildCount();
							        	{
							        		
							        		    for (int k = 0; k < childCount3; k++) {
							      			   
										           AccessibilityNodeInfo node3 = node2.getChild(k);
										            if(node3!=null){
										            	if (node3.getText()!=null)
										            	{
										            		
										            		
										            	app.setOrdermessageInfo(app.getOrdermessageInfo()+"  subsubnode.getClassName()"+node3.getText()+node3.getContentDescription()); 	
										            	}
										            	int childCount4 = node3.getChildCount();
											        	{
											        		
											        		    for (int o = 0; o < childCount4; o++) {
											      			   
														           AccessibilityNodeInfo node4 = node3.getChild(o);
														            if(node4!=null){
														            	if (node4.getText()!=null)
														            	{
														            	 	   String NewMsg=node4.getText().toString();
														            	app.setOrdermessageInfo(app.getOrdermessageInfo()+"  subsubsubnode.getClassName()test"+node4.getClassName()+NewMsg); 	
														            	   Toast.makeText(this, node4.getText(), Toast.LENGTH_LONG).show();
														           
														            	if(NewMsg.equals("1")){
														            	 	app.setOrdermessageInfo(app.getOrdermessageInfo()+"  找到了未读消息1 打开消息"); 	
														            		AccessibilityNodeInfo parent = node4.getParent();
														            		if (parent!=null)
														            		{
														            			AccessibilityNodeInfo pparent = parent.getParent();	
														         
														            			pparent.getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
														            			   Toast.makeText(this, "打开新消息", Toast.LENGTH_LONG).show();
														            			   return ;
														            		}
														            		else if(NewMsg.equals("2"))
														            		{
														            		 	app.setOrdermessageInfo(app.getOrdermessageInfo()+"  找到了未读消息2  打开消息"); 	
															            		AccessibilityNodeInfo parent2 = node4.getParent();
															            		if (parent2!=null)
															            		{
															            			AccessibilityNodeInfo pparent2 = parent2.getParent();	
															         
															            			pparent2.getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
															            			   Toast.makeText(this, "打开新消息", Toast.LENGTH_LONG).show();
															            			   return ;
															            		}
														            			
														            		}

																		   	   
														            	   
														               }
														            	String ServiceNotification=node4.getText().toString();
														            /*   
														            	if (ServiceNotification.contains("Service Notification"))
														                {
														            	   	app.setOrdermessageInfo(app.getOrdermessageInfo()+"  subsubsubnode.getClassName()点击servicenotification"); 	
																	         
														            		node4.performAction(AccessibilityNodeInfo.ACTION_CLICK);
														            		return;
														                }
														            	*/
														            	int childCount5 = node4.getChildCount();
															        	{
															        		
															        		    for (int g = 0; g < childCount5; g++) {
															      			   
																		           AccessibilityNodeInfo node5 = node4.getChild(o);
																		            if(node5!=null){
																		            	if (node5.getText()!=null)
																		            	{
																		            	app.setOrdermessageInfo(app.getOrdermessageInfo()+"  gosubsubsubnode.getClassName()"+node5.getText()+node5.describeContents()); 	
																		            	}
																		            }
																		      }
															        	}
														            	}
														            }
														      }
											        	}
										            }
										      }
							        	}
						    //        }
						     // }
			        	}
			            }
			        }
				/*222   AccessibilityNodeInfo targetNode = null;
				   targetNode = this.getLinearLayoutNode(RootNode);
				 // targetNode = this.getFrameLayoutNode(RootNode);
			      if (targetNode != null) {
			    	 app.setOrdermessageInfo(app.getOrdermessageInfo()+"找到LinearLayoutNode");  
			    	  AccessibilityNodeInfo TextViewtNode = null;
			    	  TextViewtNode = this.getTextViewNode(RootNode);
			    	  if (TextViewtNode != null) {	
			    		  
			    		  app.setOrdermessageInfo(app.getOrdermessageInfo()+"找到TextViewNode"+TextViewtNode); 
			    		 
			    		  List<AccessibilityNodeInfo> list = TextViewtNode.findAccessibilityNodeInfosByText("微信支付凭证");
                           if ((list!=null)&&(!list.isEmpty()))
			    			{
			    				 app.setOrdermessageInfo(app.getOrdermessageInfo()+"微信支付凭证");
			    
			    			}     
                           TextViewtNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				          // 	  }
			    	        //  TextViewtNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			               app.setOrdermessageInfo(app.getOrdermessageInfo()+"找到TextView 执行点击操作 targetNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)"); 
			    	       }
			    	  else
			    	  {
			    		targetNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			    	  }
			    	  
			    	  */
			    	  /*
			    	  delay(8000);//延迟等待窗口状态改变后 继续自动化操作
					  AccessibilityNodeInfo NewRootNode  = getRootInActiveWindow();
					  if (NewRootNode!=null)
					  {
					    AccessibilityNodeInfo targetNewNode = null;
					    targetNewNode = this.getFrameLayoutNode(NewRootNode);
					     if (targetNewNode != null) {
					    	  AccessibilityNodeInfo FrameLayoutNode = null;
					    	  FrameLayoutNode = this.getFrameLayoutNode(targetNode);
					    	  if (FrameLayoutNode!=null)
					    	  {
					            app.setOrdermessageInfo(app.getOrdermessageInfo()+"进入新的窗口"+FrameLayoutNode); 
					    	  }
					      }
					  }
			    	  return;
			       }*/
				 
//				LogUtil.i("Debug", "getRootInActiveWindow:"+nodeInfo);

	
		
	
		}
				 }	
		}
	}
	//	String messagetip = event.getText().toString();
	//	FileUtil.writeContentTofile(messagetip)	;	


	
	@Override
	public void onInterrupt()
	{
		Toast.makeText(this, "退出支付消息记录服务", Toast.LENGTH_SHORT).show();
		  if (listener!=null){ listener.unregisterListener();}
		  if (screenManager!=null){ screenManager=null;}
	}
	@Override
    public void onCreate() {
        super.onCreate();
        this.app = (MainApplication) super.getApplication();
        GRPservice = this;
       // Toast.makeText(this, " listener.registerListener success", Toast.LENGTH_SHORT).show();
    //   final ScreenManager screenManager = ScreenManager.getInstance(GrabLuckyRedPacketService.this);
       screenManager = ScreenManager.getInstance(this.app);
        listener = new ScreenBroadcastListener(this);
         listener.registerListener(new ScreenBroadcastListener.ScreenStateListener() {
           @Override
            public void onScreenOn() {
        	   screenManager.finishActivity();

            }

            @Override
            public void onScreenOff() {
            	screenManager.startActivity();
     
            	
            }
        });
        
    }


	
	@Override
	protected void onServiceConnected()
	{
		super.onServiceConnected();
	//	LogUtil.i("Debug", "连接抢红包服务");
        //发送广播 -> 服务成功连接
        super.sendBroadcast(new Intent("ACTION_ACCESSIBILITY_SERVICE_CONNECT"));
		Toast.makeText(this, "启动支付消息记录服务", Toast.LENGTH_SHORT).show();
		 sendAction(true);  
		if (app!=null)
		{
	  	app.setmessagestatus(7);
		}
		if(GRPservice==null ){GRPservice = this;}
	}
    @Override
    public void onDestroy() {
        super.onDestroy();
    
        GRPservice = null;
        //发送广播 -> 辅助服务已断开
        super.sendBroadcast(new Intent("ACTION_ACCESSIBILITY_SERVICE_DISCONNECT"));
        sendAction(false);  
        if (listener!=null){ listener.unregisterListener();}
        if (screenManager!=null){ screenManager=null;}
    
    }

	/** 打开通知栏消息 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void openNotify(AccessibilityEvent event)
	{
		if (event.getParcelableData() == null || !(event.getParcelableData() instanceof Notification))
		{
			return;
		}
		// 将微信的通知栏消息打开
	//	LogUtil.i("Debug", "将微信的通知栏消息打开 Notification");
		Notification notification = (Notification) event.getParcelableData();
		PendingIntent pendingIntent = notification.contentIntent;
			try
		{
	
			pendingIntent.send();
	
		} catch (PendingIntent.CanceledException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private AccessibilityNodeInfo getRootNode(){

		AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();

		if(nodeInfo == null) {

			Log.w(TAG, "rootWindow为空");

			return null;

		}

		return nodeInfo;

	}
	private void backChatWindow() {

		AccessibilityNodeInfo root =	getRootNode();

		List<AccessibilityNodeInfo> list=root.findAccessibilityNodeInfosByText("红包详情");

		if (list == null || list.size() ==0) return;

		for (AccessibilityNodeInfo node : list){

			AccessibilityNodeInfo parent = node.getParent();

			parent.getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);

		}

	}


	

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void openHongBao(AccessibilityEvent event)
	{
		delay(666);
		CharSequence className = event.getClassName();

		checkScreen(getApplicationContext());
		  UserInfo user=app.getUserInfo();
	
		user.paymessage=user.paymessage+"openHongBao";
		if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI".equals(className))
		{
			// 点中了红包，下一步就是去拆红包
			//Toast.makeText(this, "点中了红包，下一步就是去拆红包className"+className, Toast.LENGTH_SHORT).show();
		//	LogUtil.i("Debug", "点中了红包，下一步就是去拆红包com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI--checkKey1");
			checkKey1();
			user.paymessage=user.paymessage+"openHongBao checkKey1";
			//isReceivingHongbao=false;
		} else if ("com.tencent.mm.ui.LauncherUI".equals(className) || "com.tencent.mobileqq.activity.ChatActivity".equals(className))
		{
			// 在聊天界面,去点中红包
	 //	Toast.makeText(this, "在聊天界面,去点中红包"+className, Toast.LENGTH_SHORT).show();
		//	LogUtil.i("Debug", " 在聊天界面,去点中红包2");
			checkKey2();
			user.paymessage=user.paymessage+"openHongBao checkKey2";
		}
		else
		{
		// 在聊天界面,去点中红包
		//	Toast.makeText(this, "在聊天界面,去点中红包2"+className, Toast.LENGTH_SHORT).show();
		//	LogUtil.i("Debug", " 在聊天界面,去点中红包3");
			checkKey2();
			user.paymessage=user.paymessage+"openHongBao checkKey2";
		}
	}

	

	private void checkScreen(Context context)
	{
		// TODO Auto-generated method stub
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		if (!pm.isScreenOn())
		{
			//LogUtil.i("Debug", " 检查屏幕是否亮着并且唤醒屏幕wakeUpAndUnlock");
			wakeUpAndUnlock(context);
		}

	}

	private void wakeUpAndUnlock(Context context)
	{
		
		KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
		// 解锁
		kl.disableKeyguard();
		// 获取电源管理器对象
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		// 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
		// 点亮屏幕
		wl.acquire();
		// 释放
		wl.release();
	}
	
	
	
	
	
    private AccessibilityNodeInfo FrameLayoutNode = null;
    private AccessibilityNodeInfo grabNode = null;
    private AccessibilityNodeInfo LinearLayoutNode = null;
    private AccessibilityNodeInfo TextViewNode = null;
    private AccessibilityNodeInfo ViewNode = null;
    /**
     * 订单记录节点
     *
     * @param FrameLayoutNode
     * @return
     */
    private AccessibilityNodeInfo getFrameLayoutNode(AccessibilityNodeInfo rootNode) {
        this.FrameLayoutNode = null;
        this.findFrameLayoutNode(rootNode);
  
        return this.FrameLayoutNode;
    }

   
    
    
    /**
     * 订单记录节点
     *
     * @param LinearLayoutNode
     * @return
     */
    private AccessibilityNodeInfo getLinearLayoutNode(AccessibilityNodeInfo rootNode) {
        this.LinearLayoutNode = null;
        this.findLinearLayoutNode(rootNode);
  
        return this.LinearLayoutNode;
    }
    
    
    
    /**
     * 订单记录节点2
     *
     * @param FrameLayoutNode
     * @return
     */
    private AccessibilityNodeInfo getTextViewNode(AccessibilityNodeInfo rootNode) {
        this.TextViewNode = null;
        this.findTextViewNode(rootNode);
        
        return this.TextViewNode;
    }
    
    /**
     * 订单记录节点2
     *
     * @param FrameLayoutNode
     * @return
     */
    private AccessibilityNodeInfo getViewNode(AccessibilityNodeInfo rootNode) {
        this.ViewNode = null;
        this.findViewNode(rootNode);
        
        return this.ViewNode;
    }
    
    /**
     * 取得拆红包节点
     *
     * @param rootNode
     * @return
     */
    private AccessibilityNodeInfo getGrabNode(AccessibilityNodeInfo rootNode) {
        this.grabNode = null;
      
        this.findGrabNode(rootNode);
  
        return this.grabNode;
    }
    /**
     * 递归遍历，寻找CLASS_NAME_LinearLayout节点
     *
     * @param rootNode
     */
    private void findFrameLayoutNode(AccessibilityNodeInfo rootNode) {
        int childCount = rootNode.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (this.FrameLayoutNode != null) {
                rootNode.recycle();
                return;
            }
            AccessibilityNodeInfo node = rootNode.getChild(i);
            if (CLASS_NAME_FRAMEVIEW.equals(node.getClassName())) {//如果是FrameLayout
                this.FrameLayoutNode = node;
                return;
            } else {
                this.findFrameLayoutNode(node);
            }
        }
      }
    /**
     * 递归遍历，寻找CLASS_NAME_LinearLayout节点
     *
     * @param rootNode
     */
    private void findLinearLayoutNode(AccessibilityNodeInfo rootNode) {
        int childCount = rootNode.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (this.LinearLayoutNode != null) {
                rootNode.recycle();
                return;
            }
            AccessibilityNodeInfo node = rootNode.getChild(i);
            if (CLASS_NAME_LinearLayout.equals(node.getClassName())) {//如果是LinearLayoutNode
                this.LinearLayoutNode = node;
                return;
            } else {
                this.findFrameLayoutNode(node);
            }
        }
      }
    
    
    /**
     * 递归遍历，寻找CLASS_NAME_TextView节点
     *
     * @param rootNode
     */
    private void findTextViewNode(AccessibilityNodeInfo rootNode) {
        int childCount = rootNode.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (this.TextViewNode != null) {
                rootNode.recycle();
                return;
            }
            AccessibilityNodeInfo node = rootNode.getChild(i);
            if (CLASS_NAME_VIEW.equals(node.getClassName())) {//android_view_View
                this.TextViewNode = node;
                return;
            } else {
                this.findTextViewNode(node);
            }
        }
      }
    
    /**
     * 递归遍历，寻找CLASS_NAME_TView节点
     *
     * @param rootNode
     */
    private void findViewNode(AccessibilityNodeInfo rootNode) {
        int childCount = rootNode.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (this.ViewNode != null) {
                rootNode.recycle();
                return;
            }
            AccessibilityNodeInfo node = rootNode.getChild(i);
            if (CLASS_NAME_VIEW2.equals(node.getClassName())) {//android_view_View
                this.ViewNode = node;
                return;
            } else {
                this.findViewNode(node);
            }
        }
      }
    
    
    private void findGrabNode(AccessibilityNodeInfo rootNode) {
        int childCount = rootNode.getChildCount();
 
        for (int i = 0; i < childCount; i++) {
            if (this.grabNode != null) {
                rootNode.recycle();
                return;
            }
            AccessibilityNodeInfo node = rootNode.getChild(i);
            if (CLASS_NAME_BUTTON.equals(node.getClassName())) {//如果是Button
                this.grabNode = node;
                return;
            } else {
                this.findGrabNode(node);
            }
        }
      }
	
	

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void checkKey1()
	{
		AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
		if (nodeInfo == null)
			return;

		List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("拆红包");

		if (list == null || list.size() == 0)
		{
			list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/b2c");//versionCode 700 3.6.9版
			if (list == null || list.size() == 0)
			{
				list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/b43");
				if (list == null || list.size() == 0)
				{
					list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ba_");//
					
					if (list == null || list.size() == 0)
					{					 
						   AccessibilityNodeInfo targetNode = null;
						   targetNode = this.getGrabNode(nodeInfo);
						      if (targetNode != null) {
						    	  targetNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
						  	//	  backChatWindow();
						    	  isReceivingHongbao=false;
						    	//  Toast.makeText(this, "isReceivingHongbao=false;", Toast.LENGTH_SHORT).show();
						    	  return;
						       }
						//   Toast.makeText(this, "com.tencent.mm:id/ba_拆红包2", Toast.LENGTH_SHORT).show();
				
					}
				}
			}
		}

		if (list == null || list.size() == 0){return ;}
		for (AccessibilityNodeInfo n : list)
		{
		
			n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			// Toast.makeText(this, "isReceivingHongbao=false;22", Toast.LENGTH_SHORT).show();
		//	backChatWindow();
		
		}
	
	}
	
	
	
	 /**
     * 判断当前服务是否正在运行
     */
    public static boolean isRunning() {
   
    	
 
   // AccessibilityManager accessibilityManager = (AccessibilityManager) GRPservice.getSystemService(Context.ACCESSIBILITY_SERVICE);
    
    	AccessibilityManager accessibilityManager = (AccessibilityManager) GRPservice.getSystemService("com.jiandande.luckymoney.GrabLuckyRedPacketService");
        
   /*     AccessibilityServiceInfo info = GRPservice.getServiceInfo();
        if (info == null) {
            return false;
        }
      List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Iterator<AccessibilityServiceInfo> iterator = list.iterator();

        boolean isConnect = false;
        while (iterator.hasNext()) {
            AccessibilityServiceInfo i = iterator.next();
            if (i.getId().equals(info.getId())) {
                isConnect = true;
                break;
            }
        }
        if (!isConnect) {
            return false;
        }*/
        return true;
    }

    
  //创建用户的回调函数
  		private TransferPayResponse createUserResponse=new TransferPayResponse(){

  			@Override
  			public void transComplete() {
  				
  				}

			@Override
			public void transFailed(String httpCode, String errorInfo) {
				// TODO Auto-generated method stub
				
			}
  		
  			};

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void checkKey2()
	{
		  UserInfo user=app.getUserInfo();
		AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
		if (nodeInfo == null)
		{
			//Log.w(TAG, "rootWindow为空");
			user.paymessage=user.paymessage+"rootWindow为空";
			return;
		}
		//Toast.makeText(this, "checkKey2", Toast.LENGTH_SHORT).show();
	//	LogUtil.i("Debug", " checkKey2");
		user.paymessage=user.paymessage+"return test"+""+nodeInfo.getText();
		List<AccessibilityNodeInfo> wxList = nodeInfo.findAccessibilityNodeInfosByText("领取红包");
		List<AccessibilityNodeInfo> qqList = nodeInfo.findAccessibilityNodeInfosByText("QQ红包");
		List<AccessibilityNodeInfo> wxorderList = nodeInfo.findAccessibilityNodeInfosByText("备注");
		if (wxList.isEmpty())
		{
			 wxList = nodeInfo.findAccessibilityNodeInfosByText("微信红包");
		}
		if (!qqList.isEmpty())
		{
			qqList = nodeInfo.findAccessibilityNodeInfosByText("QQ红包");
			for (AccessibilityNodeInfo n : qqList)
			{
				n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			//	LogUtil.i("Debug", "-->领取QQ红包:000TEST");
				break;
			}
		}

		if (!wxList.isEmpty())
		{
		//	Toast.makeText(this, "!wxList.isEmpty()", Toast.LENGTH_SHORT).show();
			// 界面上的红包总个数
			int totalCount = wxList.size();
			// 领取最近发的红包
			for (int i = totalCount - 1; i >= 0; i--)
			{
				// 如果为领取过该红包，则执行点击、
				AccessibilityNodeInfo parent = wxList.get(i).getParent();
		
				if (parent != null)
				{
		
				  //  String strRecord=""+parent;
				   // strRecord = strRecord.substring(0,58);
				//	LogUtil.i("Debug", "-->领取微信红包:"+Integer.toString(i) + strRecord);
				//	LogUtil.i("Debug", "-->parentrecord[i]:"+Integer.toString(i) + WXparentrecord[i]);
				//	if (!WXparentrecord[i].contains(strRecord))
						
				//	{		
						//WXparentrecord[i]=strRecord;	
				 	parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				
					//Toast.makeText(this, strRecord, Toast.LENGTH_SHORT).show();
					//Toast.makeText(this, "parentrecord[i]:"+WXparentrecord[i], Toast.LENGTH_SHORT).show();
					//}
					
					break;
				}
			}
		} else if (!qqList.isEmpty())
		{

			// 界面上的红包总个数
			int totalCount = qqList.size();
			// 领取最近发的红包
			for (int i = totalCount - 1; i >= 0; i--)
			{
				//Toast.makeText(this, "红包数码:"+Integer.toString(i), Toast.LENGTH_SHORT).show();
				// 如果为领取过该红包，则执行点击、
				AccessibilityNodeInfo parent = qqList.get(i).getParent();
				Log.i(TAG, "-->领取红包:" + parent);
			//	LogUtil.i("Debug", "-->领取QQ红包:"+Integer.toString(i) + parent);
				if (parent != null)
				{
				    String strRecord=""+parent;
				    strRecord = strRecord.substring(0,58);
				
				//	if (!QQparentrecord[i].contains(strRecord))
				//	{
				//	Toast.makeText(this, "红包数码:i="+Integer.toString(i)+QQparentrecord[i]+"strRecord:"+strRecord, Toast.LENGTH_SHORT).show();			
				//	QQparentrecord[i]=strRecord;	
					parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				//	}
					break;
				}
			}

		}
		else if (!wxorderList.isEmpty())
		{

		
			user.paymessage=user.paymessage+"!wxorderList.isEmpty()";
			int totalCount = qqList.size();
			// 领取最近发的红包
			for (int i = totalCount - 1; i >= 0; i--)
			{
				//Toast.makeText(this, "红包数码:"+Integer.toString(i), Toast.LENGTH_SHORT).show();
				// 如果为领取过该红包，则执行点击、
				AccessibilityNodeInfo parent = qqList.get(i).getParent();
				Log.i(TAG, "-->领取红包:" + parent);
			//	LogUtil.i("Debug", "-->领取QQ红包:"+Integer.toString(i) + parent);
				if (parent != null)
				{
				    String strRecord=""+parent;
				    strRecord = strRecord.substring(0,58);
				
				//	if (!QQparentrecord[i].contains(strRecord))
				//	{
				//	Toast.makeText(this, "红包数码:i="+Integer.toString(i)+QQparentrecord[i]+"strRecord:"+strRecord, Toast.LENGTH_SHORT).show();			
				//	QQparentrecord[i]=strRecord;	
					parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				//	}
					break;
				}
			}
		}

	}

}
