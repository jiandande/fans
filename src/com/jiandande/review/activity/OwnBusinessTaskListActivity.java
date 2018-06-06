package com.jiandande.review.activity;



import java.io.File;


import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.jiandande.money.R;
import com.jiandande.review.adater.LoadUserAvatar;
import com.jiandande.review.adater.OwnBusinessOrderAdapter;
import com.jiandande.review.adater.LoadUserAvatar.ImageDownloadedCallBack;
import com.jiandande.review.bean.ExchangeBean;
import com.jiandande.review.bean.Pagination;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.dialog.CheckDownloadThread;
import com.jiandande.review.transcation.FindOrderAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transcation.OwnBusinessTaskAction;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.transfer.NetworkUtil;
import com.jiandande.review.util.CountDownTimerUtils;
import com.jiandande.review.util.CountVisitWebsiteTimerUtils;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.StringUtil;
import com.jiandande.review.util.Constants.OrderStatus;
import com.jiandande.review.util.Constants.ParamName;
public class OwnBusinessTaskListActivity extends AbstractActivity implements OnClickListener{
    boolean bFirstSetup;  //第一次安装 同步状态位
	FindOrderAction findOrderAction;
	OwnBusinessOrderAdapter orderAdater;
	ListView lisOrder;
	private LoadUserAvatar avatarLoader;
	ImageView download_soft_icon;  
	Pagination<ExchangeBean>pagination;
	ExchangeBean temporder;	
	Button  btnMyself,btnLoadMore;
	LinearLayout lyLoading;
	View footerView;
	int currentPage=1;
	String currentSelf="-1";
	OwnBusinessTaskAction OwnBusinessTaskAction;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myselftasklist);
		initView();
		addEventLister();
	}
	@Override
	protected void initView() {
		super.initView();
		findOrderAction=new FindOrderAction();
		lisOrder=(ListView)findViewById(R.id.lv);
		btnMyself=(Button)findViewById(R.id.myself);
		LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerView = layoutInflater.inflate(R.layout.list_footer, null);
		lisOrder.addFooterView(footerView);
		footerView.setVisibility(View.GONE);
		btnLoadMore=(Button)footerView.findViewById(R.id.list_bottom_btn);
		lyLoading=(LinearLayout)footerView.findViewById(R.id.loading);
		beginSyscMonthOrder(currentSelf,1);
	}
	

	@Override
	protected void addEventLister(){
		orderAdater = new OwnBusinessOrderAdapter(                                                
				OwnBusinessTaskListActivity.this, new ArrayList<ExchangeBean>());                                    
		lisOrder.setAdapter(orderAdater);                                             
		lisOrder.setOnItemClickListener(itemClickListener);   
		btnMyself.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnLoadMore.setOnClickListener(this);
	}
	OnItemClickListener itemClickListener =new  OnItemClickListener() {                               
		@Override                                                                                 
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,                     
				long arg3) {    		
			 temporder =(ExchangeBean) orderAdater.getItem(position); 
				//if (temporder.packagename.equalsIgnoreCase("www"))
				if (temporder.packagename.indexOf("www")!=-1)		
				{
					Intent intent=new Intent();
				   
					intent.setAction("android.intent.action.VIEW");     
	
				       
				       Uri content_url = Uri.parse(temporder.producturl);   
				        intent.setData(content_url);  
				           //没有设置默认浏览器情况下指定一个浏览器
				    	   if (isAppInstalled(OwnBusinessTaskListActivity.this,"com.tencent.mtt"))
				    	   {
				    	   intent.setClassName("com.tencent.mtt","com.tencent.mtt.MainActivity");
				    	   }
				    	   else if (isAppInstalled(OwnBusinessTaskListActivity.this,"com.uc.browser"))
				    	   {
				    		   intent.setClassName("com.uc.browser","com.uc.browser.ActivityUpdate");
				    	   }
				    	   else	   
				    	   {
				    		    intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
				    	   }       
				        startActivity(intent); 
				   //     CountVisitWebsiteTimerUtils mCountBrowserTimerUtils = new CountVisitWebsiteTimerUtils(OwnBusinessTaskListActivity.this,temporder, 20000, 1000);
				   //     mCountBrowserTimerUtils.start();
			           
				        String	taskpoint=StringUtil.moneyFormat2(temporder.amount);    	        
			    		String password=PreferenceUtil.getStrValue(OwnBusinessTaskListActivity.this,ParamName.PASSWORD,"");
						String email=PreferenceUtil.getStrValue(OwnBusinessTaskListActivity.this,ParamName.USERNAME,"");
						String imei=PreferenceUtil.getStrValue(OwnBusinessTaskListActivity.this, ParamName.IMEI, "");
						
						
						
						
						
						
						
						
						
						
						OwnBusinessTaskAction=new OwnBusinessTaskAction();
						OwnBusinessTaskAction.setPassword(taskpoint);
						OwnBusinessTaskAction.setEmail(email);
						OwnBusinessTaskAction.setImei(imei);
					//	OwnBusinessTaskAction.setOwnbusiness("666666");
						if( temporder.packagename.equals("www1")){
						OwnBusinessTaskAction.setOwnbusiness("222001");
						}else
						{
						OwnBusinessTaskAction.setOwnbusiness("222002");
						}
						OwnBusinessTaskAction.setPackagename(temporder.packagename);
						OwnBusinessTaskAction.setImei(imei);
						OwnBusinessTaskAction.setPname(temporder.pname);
						OwnBusinessTaskAction.setTransferPayResponse(GetOwnbusinessResPond);
						OwnBusinessTaskAction.transStart();
						
					return;
				}
		
	    String downloadok=PreferenceUtil.getStrValue(OwnBusinessTaskListActivity.this, temporder.packagename, "");
		
		     
		     
		     
		     
		     
		     
		     
		          
		
		  //   Toast.makeText(OwnBusinessTaskListActivity.this, downloadok+temporder.packagename, Toast.LENGTH_LONG).show();	
		     if (isAppInstalled(OwnBusinessTaskListActivity.this,temporder.packagename))
		     {	 	 
		    	//doStartApplicationWithPackageName(temporder.packagename);
	
		         if ( downloadok.equals(temporder.pname))
		    	{
		    		    String	taskpoint=StringUtil.moneyFormat2(temporder.amount);    	        
			    		String password=PreferenceUtil.getStrValue(OwnBusinessTaskListActivity.this,ParamName.PASSWORD,"");
						String email=PreferenceUtil.getStrValue(OwnBusinessTaskListActivity.this,ParamName.USERNAME,"");
						String imei=PreferenceUtil.getStrValue(OwnBusinessTaskListActivity.this, ParamName.IMEI, "");
						OwnBusinessTaskAction=new OwnBusinessTaskAction();
						OwnBusinessTaskAction.setPassword(taskpoint);
						OwnBusinessTaskAction.setEmail(email);
						OwnBusinessTaskAction.setImei(imei);
						OwnBusinessTaskAction.setOwnbusiness("666666");
						OwnBusinessTaskAction.setPackagename(temporder.packagename);
						OwnBusinessTaskAction.setImei(imei);
						OwnBusinessTaskAction.setPname(temporder.pname);
						OwnBusinessTaskAction.setTransferPayResponse(GetOwnbusinessResPond);
						if (loadingDialog.isShowing()) {
							loadingDialog.setMessage("查询任务奖励中,请稍候");
						} else {
							loadingDialog.show("查询任务奖励中,请稍候...");
						}
						OwnBusinessTaskAction.transStart();
		    	}	
						doStartApplicationWithPackageName(temporder.packagename);
		    			
		     return ;
		     }	 
		    	 
		//	if(OrderStatus.STATUS_WAITFORRECEIVE==temporder.status){
		//当手机wifi=2没有连接的时候 进行提示getNetworkType	、、
				 if(NetworkUtil.getNetworkType(OwnBusinessTaskListActivity.this) != NetworkUtil.WIFI_CONNECT){
				     new AlertDialog.Builder(OwnBusinessTaskListActivity.this).setTitle("温馨提示")//设置对话框标题  
				     .setMessage("当前wifi没有连接,您确定要继续下载?")//设置显示的内容    
				     .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮  
				         @Override  
				         public void onClick(DialogInterface dialog666, int which) {//确定按钮的响应事件  
				    		 CheckDownloadThread checkVersionThread;
				 			checkVersionThread = new CheckDownloadThread(OwnBusinessTaskListActivity.this,temporder.producturl,temporder.packagename,temporder.pname, dialog);
				 			checkVersionThread.setShowLoadDialog(false);
				 			checkVersionThread.run();
				 			bFirstSetup=true;
				  
				         }  
				  
				     }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮  
				         @Override  
				         public void onClick(DialogInterface dialog, int which) {//响应事件  
				             // TODO Auto-generated method stub  
				             return ;
				         }  
				     }).show();//在按键响应事件中显示此对话框     
	            }
				 else
				 {
			  		 CheckDownloadThread checkVersionThread;
			 			checkVersionThread = new CheckDownloadThread(OwnBusinessTaskListActivity.this,temporder.producturl,temporder.packagename,temporder.pname, dialog);
			 			checkVersionThread.setShowLoadDialog(false);
			 			checkVersionThread.run();
			 			bFirstSetup=true;
			 			
				
				 }
				  
				
				
	
		//	}
		//	else
		//	{
		//		Toast.makeText(OwnBusinessTaskListActivity.this,
		//				"恭喜您，该任务已达成,无需重复领取", Toast.LENGTH_LONG)
		//				.show();	
		//	}
		//	Toast.makeText(OwnBusinessTaskListActivity.this,
			//		String.valueOf(position), Toast.LENGTH_LONG)
				//	.show();	
			/*	Intent intent = new Intent();                                                         
			intent.setClass(ExchangeListActivity.this, ExChangeOrderActivity.class);           
			intent.putExtra(ParamName.ORDER, temporder);                                              
			startActivity(intent);          */                                                      
		}                                                                                         
	};
	
	  
	  
	  
	
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			super.onBackPressed();
			finish();
			break;
		case R.id.myself:	
			currentSelf="1";
			beginSyscMonthOrder("1",1);
			break;
		case R.id.list_bottom_btn:	
			lyLoading.setVisibility(View.VISIBLE);
			footerView.setVisibility(View.GONE);
			currentPage=currentPage+1;
	
			beginSyscMonthOrder(currentSelf,currentPage);
			break;
		}
	}
	public boolean fileIsExists(String strPath){
        try{
                File file=new File(strPath);
                if(!file.exists()){
            
                        return false;
                }
                
        }catch (Exception e) {
                // TODO: handle exception
                return false;
        }
        return true;
}

	private void doStartApplicationWithPackageName(String packagename) {  
		  
	    // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等  
	    PackageInfo packageinfo = null;  
	    try {  
	        packageinfo = getPackageManager().getPackageInfo(packagename, 0);  
	    } catch (NameNotFoundException e) {  
	        e.printStackTrace();  
	    }  
	    if (packageinfo == null) {  
	        return;  
	    }  
	  
	    // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent  
	    Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);  
	    resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);  
	    resolveIntent.setPackage(packageinfo.packageName);  
	  
	    // 通过getPackageManager()的queryIntentActivities方法遍历  
	    List<ResolveInfo> resolveinfoList = getPackageManager()  
	            .queryIntentActivities(resolveIntent, 0);  
	  
	    ResolveInfo resolveinfo = resolveinfoList.iterator().next();  
	    if (resolveinfo != null) {  
	        // packagename = 参数packname  
	        String packageName = resolveinfo.activityInfo.packageName;  
	        // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]  
	        String className = resolveinfo.activityInfo.name;  
	        // LAUNCHER Intent  
	        Intent intent = new Intent(Intent.ACTION_MAIN);  
	        intent.addCategory(Intent.CATEGORY_LAUNCHER);  
	  
	        // 设置ComponentName参数1:packagename参数2:MainActivity路径  
	        ComponentName cn = new ComponentName(packageName, className);  
	  
	        intent.setComponent(cn);  
	        startActivity(intent);  
	    }  
	}  
	
	private boolean isAppInstalled(Context context, String packageName ) {  
        PackageManager pm = context.getPackageManager();  
        boolean installed = false;  
        try {  
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);  
            installed = true;  
        } catch (PackageManager.NameNotFoundException e) {  
            installed = false;  
        }  
        return installed;  
    }  
	
	private void beginSyscMonthOrder(String myself,int page) {		
		findOrderAction.setEndDate("");
		findOrderAction.setMyself(myself);
		findOrderAction.setStartDate("");
		findOrderAction.setStatus("");
		findOrderAction.setType("");
		findOrderAction.setPage(page);
		UserInfo user=app.getUserInfo();
		findOrderAction.setMid(user.accountNo);
		findOrderAction.setBuyId("OwnBusinessTask");
	//	Toast.makeText(ExchangeListActivity.this, user.accountNo, Toast.LENGTH_LONG).show();
		findOrderAction.setTransferPayResponse(findOrderRespond);
		if (loadingDialog.isShowing()) {
			loadingDialog.setMessage("正在查询任务列表，请稍候");
		} else {
			loadingDialog.show("正在查询任务列表，请稍候");
		}
		findOrderAction.transStart();
	}
	

	//自营任务的回调函数
	private TransferPayResponse GetOwnbusinessResPond=new TransferPayResponse(){

		@Override
		public void transComplete() {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			} 
			
			if(HttpCodeHelper.RESPONSE_SUCCESS.equals(OwnBusinessTaskAction.respondData.respCode)){
				
				Toast.makeText(OwnBusinessTaskListActivity.this,OwnBusinessTaskAction.respondData.respDesc, Toast.LENGTH_LONG).show();
			}else if(HttpCodeHelper.PASSWORD_ERROR.equals(OwnBusinessTaskAction.respondData.respCode)){
				
			}else{
				Toast.makeText(OwnBusinessTaskListActivity.this,OwnBusinessTaskAction.respondData.respCode+"#"+OwnBusinessTaskAction.respondData.respDesc, Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			Toast.makeText(OwnBusinessTaskListActivity.this, errorInfo, Toast.LENGTH_LONG).show();	
		}
		
	};
	
	
	
	TransferPayResponse findOrderRespond = new TransferPayResponse() {

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			 if(loadingDialog!=null){
				 loadingDialog.cancel();
			 }
			Toast.makeText(OwnBusinessTaskListActivity.this, httpCode + "#" + errorInfo,
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void transComplete() {
			 if(loadingDialog!=null){
				 loadingDialog.cancel();
			 }
			if (HttpCodeHelper.RESPONSE_SUCCESS
					.equals(findOrderAction.respondData.respCode)) {
				 pagination=findOrderAction.respondData.pagination;
				 if(currentPage>1){
					 orderAdater.setDatas(pagination.datas, true);
				 }else{
					 orderAdater.setDatas(pagination.datas, false);
				 }
				 orderAdater.notifyDataSetChanged();
				 if(pagination.page<pagination.countPage){
					 footerView.setVisibility(View.VISIBLE);
					 lyLoading.setVisibility(View.GONE);
				 }else{
					 footerView.setVisibility(View.GONE);
				 }
				 
			} else {
				Toast.makeText(OwnBusinessTaskListActivity.this,
						findOrderAction.respondData.respDesc, Toast.LENGTH_LONG)
						.show();
			}
		}

	};
	
	
	
	  private void showUserAvatar(ImageView iamgeView, String avatar) {
			 // avatar="http://www.jiandande.com//member/templets/images/dfgirl.png";
			  final String url_avatar = avatar;
			
		        iamgeView.setTag(url_avatar);
		        if (url_avatar != null && !url_avatar.equals("")) {
		            Bitmap bitmap = avatarLoader.loadImage(iamgeView, url_avatar,
		                    new ImageDownloadedCallBack() {

		                        @Override
		                        public void onImageDownloaded(ImageView imageView,
		                                Bitmap bitmap) {
		                            if (imageView.getTag() == url_avatar) {
		                               imageView.setImageBitmap(bitmap);
		                            }
		                        }

		                    });
		            if (bitmap != null){
		               iamgeView.setImageBitmap(bitmap);

		        }
	
		        }
	  }
}
