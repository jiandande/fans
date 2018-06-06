package com.jiandande.review.activity;



import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import net.youmi.android.normal.spot.SpotManager;
import net.youmi.android.offers.OffersBrowserConfig;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.dlnetwork.Dianle;
import com.jiandande.money.R;
import com.jiandande.review.adater.ChargeAdapter;
import com.jiandande.review.bean.HistoryMoney;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.transcation.AddMoneyAction;
import com.jiandande.review.transcation.CanDownLoadAction;
import com.jiandande.review.transcation.ClientLoginAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transcation.ClientSignInAction;
import com.jiandande.review.transcation.SyncUnionTaskAction;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.StringUtil;
import com.jiandande.review.util.SystemUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Constants.PlatType;
import com.zy.phone.SDKInit;
import com.zy.phone.net.Integral;


public class ChargeCenterActivity extends AbstractActivity implements
		OnItemClickListener, Integral {

	private ListView listView;
	ChargeAdapter adapter;
	
    ClientSignInAction signinAction;
    SyncUnionTaskAction SyncUnionTaskMoney ;
    
	CanDownLoadAction canDownLoadAction;
	
	 private String AdpCode = "9f37e18a9f88cc6f";
	// 用户ID，用于记录开发者应用的唯一用户标识,没有为空
	private String Other = "";

//	 com.datouniao.AdPublisher.AppConnect appConnectInstance;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_center);
		initView();
		addEventLister();
		Dianle.initDianleContext(this, "c46a41076a0dc03b48dd1db1af963958");
		Dianle.setCustomActivity("com.dlnetwork.DianleOfferActivity");
		Dianle.setCustomService("com.dlnetwork.DianleOfferHelpService");
		ShareSDK.initSDK(this);
		String imei=PreferenceUtil.getStrValue(ChargeCenterActivity.this, ParamName.IMEI, "");
		Other=imei;
		SDKInit.initAd(this, AdpCode, Other);
	   
	

    	  // 有米Android SDK v4.10之后的sdk还需要配置下面代码，以告诉sdk使用了服务器回调
    //	  OffersManager.getInstance(this).setUsingServerCallBack(true);
    	//  AdManager.getInstance(this).init("434afaf01560ec82", "ce011c60e3c6c087", false , false );
	//	AdManager.getInstance(this).init("016758f32f8c74bb", "d5ec1bd136fb835e", false , true );
	//	OffersManager.getInstance(this).onAppLaunch();
        	  //有米
       	// userid 不能为空 或者 空串,否则设置无效, 字符串长度必须要小于50
   	//	String imei=PreferenceUtil.getStrValue(ChargeCenterActivity.this, ParamName.IMEI, "");
       	OffersManager.getInstance(this).setCustomUserId(imei);
       	// (可选)注册积分监听-随时随地获得积分的变动情况
       	OffersManager.getInstance(this).setUsingServerCallBack(true);
     		// 如果使用积分广告，请务必调用积分广告的初始化接口:
         OffersManager.getInstance(this).onAppLaunch();
         OffersBrowserConfig.getInstance(this).setPointsLayoutVisibility(true);
	}
	
	
	@SuppressLint("SdCardPath")	
	private void shareMultiplePictureToTimeLine(File... files) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                        "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");

        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        for (File f : files) {
                imageUris.add(Uri.fromFile(f));
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        intent.putExtra("Kdescription", "wwwwwwwwwwwwwwwwwwww");
        startActivity(intent);
}

	@SuppressLint("SdCardPath")	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		UserInfo user = app.getUserInfo();
		if (app.getUserInfo() == null) {
			Intent intent = new Intent();
			intent.setClass(ChargeCenterActivity.this, WelComeActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		if (position == 0) {
			//
			if (user.checkCount >222000 && user.vip <2) {
				Toast.makeText(this, "恭喜您,您已经赚取奖励已经达到222元了,请开通VIP2功能才可继续,充值VIP尊享奖励加成等特权~~", Toast.LENGTH_LONG).show();	
			}
			else
			{
			String password=PreferenceUtil.getStrValue(this,ParamName.PASSWORD,"");
			String email=PreferenceUtil.getStrValue(this,ParamName.USERNAME,"");
			String imei=PreferenceUtil.getStrValue(ChargeCenterActivity.this, ParamName.IMEI, "");
			signinAction=new ClientSignInAction();
			signinAction.setPassword(password);
			signinAction.setEmail(email);
			signinAction.setImei(imei);
			signinAction.setTransferPayResponse(GetPointsResPond);
			
			if (loadingDialog.isShowing()) {
				loadingDialog.setMessage("正在签到领取积分，请稍候");
			} else {
				loadingDialog.show("正在签到领取积分，请稍候");
			}
			signinAction.transStart();
			}
		//	Toast.makeText(this, "欢迎光临", Toast.LENGTH_LONG).show();
			
		} else if (position == 1) {
			if (user.checkCount >88800 && user.vip ==0) {
				Toast.makeText(this, "恭喜您,您已经赚取奖励已经达到88元了,请开通VIP1功能才可继续,充值VIP尊享奖励加成等特权~~", Toast.LENGTH_LONG).show();	
			}
			else
			{
			Intent intent=new Intent();
			intent.setClass(ChargeCenterActivity.this, LuckyPointsActivity.class);
			startActivity(intent);
			}
	
		} else if (position == 2) {
			
			String imei=PreferenceUtil.getStrValue(ChargeCenterActivity.this, ParamName.IMEI, "");
			Other=imei;
			SDKInit.initAdList(this);
			
			SDKInit.checkIntegral(this);	
		} else if (position == 3) {
	
			
			OffersManager.getInstance(this).showOffersWall();
		/*	// (可选)注册积分监听-随时随地获得积分的变动情况
			PointsManager.getInstance(this).registerNotify(app);

		
			float pointsBalance = PointsManager.getInstance(this).queryPoints();
	
			String pointsBalance2=pointsBalance+"";
			String password=PreferenceUtil.getStrValue(this,ParamName.PASSWORD,"");
			String email=PreferenceUtil.getStrValue(this,ParamName.USERNAME,"");
			String imei=PreferenceUtil.getStrValue(ChargeCenterActivity.this, ParamName.IMEI, "");
		
			SyncUnionTaskMoney=new SyncUnionTaskAction();
			SyncUnionTaskMoney.setPassword(pointsBalance2);
			SyncUnionTaskMoney.setPlatType(PlatType.YOUMI);
			SyncUnionTaskMoney.setPassword(pointsBalance2);
			SyncUnionTaskMoney.setEmail(email);
			SyncUnionTaskMoney.setImei(imei);
			SyncUnionTaskMoney.setTransferPayResponse(SyncUnionMoneyResPond);
			SyncUnionTaskMoney.transStart();
			Toast.makeText(ChargeCenterActivity.this,
					"联盟二本地积分:"+pointsBalance2,
					Toast.LENGTH_LONG).show();*/
		}  else if (position == 4) {
			/*String paramString1="222222000000";
			String localUri1="http://www.jaindande.com";
			Intent localIntent = new Intent("android.intent.action.SEND");
            localIntent.putExtra("android.intent.extra.TEXT", paramString1);
            localIntent.putExtra("sms_body", paramString1);
            localIntent.putExtra("Kdescription", paramString1);
            localIntent.putExtra("android.intent.extra.STREAM", localUri1);
            localIntent.setType("image/*");
            startActivity(Intent.createChooser(localIntent, "Share"));*/
//            context.startActivity(Intent.createChooser(localIntent, "Share"));


            // 遍历 SD 卡下 .png 文件通过微信分享
            File root = Environment.getExternalStorageDirectory();
            File[] files = root.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
               // String filepathpng =  "/sdcard/jiandande/";//pathname.getName()+"/jiandande";
                    if (pathname.getName().endsWith(".png"))
                        return true;
                    return false;
                }
            });
            shareMultiplePictureToTimeLine(files);	
			//ShareSDK.initSDK(this);
			UserInfo myuser = app.getUserInfo();
			if (app.getUserInfo() == null) {
				
			}
			 OnekeyShare oks = new OnekeyShare();
			 //关闭sso授权
			 oks.disableSSOWhenAuthorize(); 
			 
			// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
			 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
			 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
			 oks.setTitle("简单得");
			 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
			 oks.setTitleUrl("http://www.jiandande.com/member/invitefriend.php?mid="+myuser.accountNo);
			 // text是分享文本，所有平台都需要这个字段
			 oks.setText("一款能抢红包又能赚钱的软件,简单赚钱，赶快下载!邀请码:"+myuser.accountNo+"http://www.jiandande.com/member/invitefriend.php?mid="+myuser.accountNo);
			 //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
			 oks.setImageUrl("http://www.jiandande.com/jiandande.png");
			 
			 
			 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
			 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
			 // url仅在微信（包括好友和朋友圈）中使用
			 oks.setUrl("http://www.jiandande.com/member/invitefriend.php?mid="+myuser.accountNo);
			 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
			 oks.setComment("一款能抢红包又能赚钱的软件，轻松赚钱，赶快下载!邀请码:"+myuser.accountNo+"");
			 // site是分享此内容的网站名称，仅在QQ空间使用
			 oks.setSite("简单得");
			 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
			 oks.setSiteUrl("http://www.jiandande.com/member/invitefriend.php?mid="+myuser.accountNo);
			 
			// 启动分享GUI
			 oks.show(this);
			 }
		
		
         else if (position == 5) {
        	Intent intent=new Intent(ChargeCenterActivity.this,VipUserCenterActivity.class);
    		 startActivity(intent);   
        	 
        	 
        	 
        	 
        	//密友 新增好友  http://www.jiandande.com/member/index.php?action=newfriend&uid=E5AAB894704B967B
        	//我的好友 http://www.jiandande.com/member/myfriend.php?ftype=0
            //成为密友 关注  http://www.jiandande.com/member/myfriend.php?ftype=1 
        	 
        	 
//			com.datouniao.AdPublisher.AppConfig config = new com.datouniao.AdPublisher.AppConfig();
//			config.setAppID("78d360ff-a79a-4cdf-95b6-96b1799c1d37");//设置AppID
//			config.setSecretKey("quegnyhobroh");//设置AppKey
//			config.setCtx(this);
//			config.setClientUserID(app.getUserInfo().uid+"");
//			appConnectInstance = com.datouniao.AdPublisher.AppConnect.getInstance(config);
//			if (user.haveFilter) {
//				canDownLoadAction.setImei(new SystemUtil(this).getPhoneIMEI());
//				canDownLoadAction.setIp(SystemUtil.getIpAddr(this));
//				canDownLoadAction.setIndex(PlatType.DATOUNIAO);
//				canDownLoadAction.setTransferPayResponse(tCanResponse);
//				if (loadingDialog.isShowing()) {
//					loadingDialog.setMessage("正在服务器通讯中，请稍候");
//				} else {
//					loadingDialog.show("正在服务器通讯中，请稍候");
//				}
//				canDownLoadAction.transStart();
//			} else {	
//				appConnectInstance.ShowAdsOffers();
//			}
//
		}
	}

	protected void initView() {
		listView = (ListView) findViewById(R.id.lv);
		adapter = new ChargeAdapter(this);
		listView.setAdapter(adapter);
		canDownLoadAction = new CanDownLoadAction();
	}

	@Override
	protected void addEventLister() {
		super.addEventLister();
		listView.setOnItemClickListener(this);
	
	}
	
	/**
	 *  积分余额增减变动监听器
	 */
	public interface PointsChangeNotify {
	    /**
	     *  积分余额增减变动通知,该回调在 UI 线程中进行，可直接与 UI 进行交互
	     *  @param pointsBalance 当前积分余额
	     */
	    public void onPointBalanceChange(float pointsBalance);
	}


	@Override
	protected void onResume() {
		// 从服务器端获取当前用户的虚拟货币.
		// 返回结果在回调函数getUpdatePoints(...)中处理
		super.onResume();
	}

	int canCount = 0;
	private TransferPayResponse tCanResponse = new TransferPayResponse() {

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if (canCount < 3) {
				canDownLoadAction.transStart();
				canCount++;
			}
		}
		@Override
		public void transComplete() {
			if (loadingDialog != null) {
				loadingDialog.cancel();
			}
			if (HttpCodeHelper.RESPONSE_SUCCESS
					.equals(canDownLoadAction.respondData.respCode)) {
				//
				if (canDownLoadAction.respondData.can) {
					if (PlatType.BAIDU.equals(canDownLoadAction.sendData.index)) {
						com.baidu.mobads.appoffers.OffersManager
								.showOffers(ChargeCenterActivity.this);
					} else if (PlatType.YOUMI
							.equals(canDownLoadAction.sendData.index)) {
						net.youmi.android.offers.OffersManager.getInstance(
								ChargeCenterActivity.this).showOffersWall();
					} else if (PlatType.DIANLE
							.equals(canDownLoadAction.sendData.index)) {
						Dianle.showOffers(ChargeCenterActivity.this);
					} else if (PlatType.WANPU
							.equals(canDownLoadAction.sendData.index)) {
				
					}else if (PlatType.DATOUNIAO
							.equals(canDownLoadAction.sendData.index)) {
						//appConnectInstance.ShowAdsOffers();
					}
				} else {
					Toast.makeText(ChargeCenterActivity.this,
							canDownLoadAction.respondData.respDesc,
							Toast.LENGTH_LONG).show();
				}
			} else {
				if (canCount < 3) {
					canDownLoadAction.transStart();
					canCount++;
				}
			}
		}
	};
	//登录的回调函数
			private TransferPayResponse GetPointsResPond=new TransferPayResponse(){

				@Override
				public void transComplete() {
					if (loadingDialog!=null) {
						loadingDialog.cancel();
					} 
					
					if(HttpCodeHelper.RESPONSE_SUCCESS.equals(signinAction.respondData.respCode)){
						Toast.makeText(ChargeCenterActivity.this,signinAction.respondData.respDesc, Toast.LENGTH_LONG).show();
					}else if(HttpCodeHelper.PASSWORD_ERROR.equals(signinAction.respondData.respCode)){
						
					}else{
						Toast.makeText(ChargeCenterActivity.this,signinAction.respondData.respCode+"#"+signinAction.respondData.respDesc, Toast.LENGTH_LONG).show();
					}
				}

				@Override
				public void transFailed(String httpCode, String errorInfo) {
					if (loadingDialog!=null) {
						loadingDialog.cancel();
					}
					Toast.makeText(ChargeCenterActivity.this, errorInfo, Toast.LENGTH_LONG).show();	
				}
				
			};

			//同步联盟积分
			private TransferPayResponse SyncUnionMoneyResPond=new TransferPayResponse(){

				@Override
				public void transComplete() {
					if (loadingDialog!=null) {
						loadingDialog.cancel();
					} 
					
					if(HttpCodeHelper.RESPONSE_SUCCESS.equals(SyncUnionTaskMoney.respondData.respCode)){
						Toast.makeText(ChargeCenterActivity.this,SyncUnionTaskMoney.respondData.respDesc, Toast.LENGTH_LONG).show();
					}else if(HttpCodeHelper.PASSWORD_ERROR.equals(SyncUnionTaskMoney.respondData.respCode)){
						
					}else{
						Toast.makeText(ChargeCenterActivity.this,SyncUnionTaskMoney.respondData.respCode+"#"+signinAction.respondData.respDesc, Toast.LENGTH_LONG).show();
					}
				}

				@Override
				public void transFailed(String httpCode, String errorInfo) {
					if (loadingDialog!=null) {
						loadingDialog.cancel();
					}
					Toast.makeText(ChargeCenterActivity.this, errorInfo, Toast.LENGTH_LONG).show();	
				}
				
			};

	protected void onDestroy() {
		super.onDestroy();
		// 回收积分广告占用的资源
		// （可选）注销积分监听
		// 如果在onCreate调用了PointsManager.getInstance(this).registerNotify(this)进行积分余额监听器注册，那这里必须得注销
		PointsManager.getInstance(this).unRegisterNotify(app);
//		if (appConnectInstance != null)
//		{
//		appConnectInstance.finalize();
//		}
		// 开屏展示界面的 onDestroy() 回调方法中调用
		
	}

	@Override
	public void retAddIntegral(String arg0, String arg1) {

	}

	/**
	 * 查看积分 retcode 0：成功，1：失败，2：积分不够扣除 返回积分
	 */
	@Override
	public void retCheckIntegral(String retcode, String integral) {
		if (retcode.equals("0")) {
			/*String password=PreferenceUtil.getStrValue(this,ParamName.PASSWORD,"");
			String email=PreferenceUtil.getStrValue(this,ParamName.USERNAME,"");
			String imei=PreferenceUtil.getStrValue(ChargeCenterActivity.this, ParamName.IMEI, "");
			SyncUnionTaskMoney=new SyncUnionTaskAction();
			SyncUnionTaskMoney.setPassword(integral);
			SyncUnionTaskMoney.setEmail(email);
			SyncUnionTaskMoney.setImei(imei);
			SyncUnionTaskMoney.setTransferPayResponse(SyncUnionMoneyResPond);
			SyncUnionTaskMoney.transStart();*/
		}  else if (retcode.equals("1")) {
		//	Toast.makeText(ChargeCenterActivity.this, "温馨提示,如需同步联盟积分,请重新打开联盟任务...", Toast.LENGTH_LONG).show();	
		//	SDKInit.checkIntegral(this);	
	} 
	}


	@Override
	public void retMinusIntegral(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	};
}
