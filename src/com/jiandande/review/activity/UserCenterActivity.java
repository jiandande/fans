package com.jiandande.review.activity;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.MediaStore.Audio;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.android.pushservice.BasicPushNotificationBuilder;
import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.jiandande.money.R;
import com.jiandande.review.adater.FileUtil;
import com.jiandande.review.adater.LoadUserAvatar;
import com.jiandande.review.adater.LoadUserAvatar.ImageDownloadedCallBack;
import com.jiandande.review.adater.MessageAdapter;
import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.StringUtil;
import com.jiandande.review.util.Constants.ParamName;
import com.jiandande.review.util.Utils;
import com.zy.phone.net.Integral;
import com.inmobi.ads.*;






@SuppressLint("SdCardPath")
public class UserCenterActivity extends AbstractActivity implements OnClickListener,OnItemClickListener, Integral{
	TextView txtUserName;
	TextView txtTotal;
	TextView txtBanace;
	TextView txtSetting;
	TextView txtmoney2;
	TextView tvAndroidNotices;
	private ImageView iv_vip;
	private ImageView iv_viplevel;
	private ImageView iv_avatar;
    private String imageName;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final int UPDATE_FXID = 4;// 结果
    private static final int UPDATE_NICK = 5;// 结果
    private LoadUserAvatar avatarLoader;
    private FileUtil fileUtil;
    private ListView listView;
    MessageAdapter adapter;
	InMobiInterstitial rewardedAd;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_center);
		  avatarLoader = new LoadUserAvatar(this, "/sdcard/jiandande/");
		  try {
		  File file=new File("/sdcard/jiandande/");
          if(!file.exists()){
        	  file.mkdirs();
          }
			}finally{}
		initView();
		addEventLister();	
	//	InMobiSdk.init(UserCenterActivity.this, "08cca8540a64459aa2c5296103b160e7");
		tvAndroidNotices = (TextView)findViewById(R.id.scrolltext);  
		tvAndroidNotices.setMovementMethod(ScrollingMovementMethod.getInstance());
		showUserInfo();
	    /***无语    能接收绑定推送      通知栏显示不了信息 
		Resources resource = this.getResources();
        String pkgName = this.getPackageName();
        // Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
        // 这里把apikey存放于manifest文件中，只是一种存放方式，
        // 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
        // "api_key")
        // ！！请将AndroidManifest.xml api_key 字段值修改为自己的 api_key 方可使用 ！！
        // ！！ATTENTION：You need to modify the value of api_key to your own in AndroidManifest.xml to use this Demo !!
        // 启动百度push
         PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
                Utils.getMetaValue(UserCenterActivity.this, "api_key"));
           */
         // Push: 无账号初始化，用api key绑定

        // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
        // PushManager.enableLbs(getApplicationContext());

        /**
         * 以下通知栏设置2选1。使用默认通知时，无需添加以下设置代码。
         */

        // 1.默认通知
        // 若您的应用需要适配Android O（8.x）系统，且将目标版本targetSdkVersion设置为26及以上时：
        // SDK提供设置Android O（8.x）新特性---通知渠道的设置接口。
        // 若不额外设置，SDK将使用渠道名默认值"Push"；您也可以仿照以下3行代码自定义channelId/channelName。
        // 注：非targetSdkVersion 26的应用无需以下调用且不会生效
		   /**   BasicPushNotificationBuilder bBuilder = new BasicPushNotificationBuilder();
        bBuilder.setChannelId("testDefaultChannelId");
        bBuilder.setChannelName("testDefaultChannelName");
       //  PushManager.setDefaultNotificationBuilder(this, bBuilder); //使自定义channel生效
        
        // 2.自定义通知
        // 设置自定义的通知样式，具体API介绍见用户手册
        // 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
        // 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                resource.getIdentifier(
                        "notification_custom_builder", "layout", pkgName),
                resource.getIdentifier("notification_icon", "id", pkgName),
                resource.getIdentifier("notification_title", "id", pkgName),
                resource.getIdentifier("notification_text", "id", pkgName));
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(resource.getIdentifier(
                "simple_notification_icon", "drawable", pkgName));
        cBuilder.setNotificationSound(Uri.withAppendedPath(
                Audio.Media.INTERNAL_CONTENT_URI, "6").toString());
        // 若您的应用需要适配Android O（8.x）系统，且将目标版本targetSdkVersion设置为26及以上时：
        // 可自定义channelId/channelName, 若不设置则使用默认值"Push"；
        // 注：非targetSdkVersion 26的应用无需以下2行调用且不会生效
        cBuilder.setChannelId("testId");
        cBuilder.setChannelName("testName");
        // 推送高级设置，通知栏样式设置为下面的ID，ID应与server下发字段notification_builder_id值保持一致
        PushManager.setNotificationBuilder(this, 1, cBuilder);
    */
		
		
		
	  /*因为停掉啦，决定暂时不继续集成...     Resources resource = this.getResources();
	       String pkgName = this.getPackageName();
		  PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
	                 Utils.getMetaValue(UserCenterActivity.this, "api_key"));
		  
		  Toast.makeText(UserCenterActivity.this,Utils.getMetaValue(UserCenterActivity.this, "api_key") , Toast.LENGTH_LONG).show();
	         // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
	        // PushManager.enableLbs(getApplicationContext());

	        // Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
	        // 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
	        // 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
	        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
	                resource.getIdentifier(
	                        "notification_custom_builder", "layout", pkgName),
	                resource.getIdentifier("notification_icon", "id", pkgName),
	                resource.getIdentifier("notification_title", "id", pkgName),
	                resource.getIdentifier("notification_text", "id", pkgName));
	        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
	        cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);

	        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
	        cBuilder.setLayoutDrawable(resource.getIdentifier(
	                "simple_notification_icon", "drawable", pkgName));
	        cBuilder.setNotificationSound(Uri.withAppendedPath(
	                Audio.Media.INTERNAL_CONTENT_URI, "6").toString());
	        // 推送高级设置，通知栏样式设置为下面的ID
	        PushManager.setNotificationBuilder(this, 1, cBuilder);*/
	}
	@Override
	protected void addEventLister() {
		super.addEventLister();
		listView.setOnItemClickListener(this);
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (position == 0) {	
			Toast.makeText(this, "通过 积分 - 签到赚积分,每天都可以领取不定额的积分!", Toast.LENGTH_LONG).show();
			/* rewardedAd = new InMobiInterstitial(this, 1497055451652L, new InMobiInterstitial.InterstitialAdListener() {
				 
				 @Override
				public void onAdRewardActionCompleted(InMobiInterstitial ad, Map rewards) {
					//Write code here to dispense the reward
				}
				@Override
				public void onAdDisplayed(InMobiInterstitial ad) {}
				@Override
				public void onAdDismissed(InMobiInterstitial ad) {}
				@Override
				public void onAdInteraction(InMobiInterstitial ad, Map params) {}
				@Override
				public void onAdLoadSucceeded(final InMobiInterstitial ad) {
					
					Toast.makeText(UserCenterActivity.this, "onAdLoadSucceeded ad", Toast.LENGTH_LONG).show();
				}
				@Override
				public void onAdLoadFailed(InMobiInterstitial ad, InMobiAdRequestStatus requestStatus) {
					Toast.makeText(UserCenterActivity.this, "onAdLoadFailed ad"+"666"+requestStatus, Toast.LENGTH_LONG).show();
				}
				@Override
				public void onUserLeftApplication(InMobiInterstitial ad) {}

				});
		    	rewardedAd.load();
				if(rewardedAd.isReady())
					rewardedAd.show();*/
		}
		else if (position == 1) {
			//InMobiBanner banner = (InMobiBanner)findViewById(R.id.banner);
			//banner.load();
			Toast.makeText(this, "通过 积分 -联盟任务一二,更多-自营任务 可以领取更多的积分!", Toast.LENGTH_LONG).show();
		}
		else if (position == 2) {
			showVIPDialog();
			
	}
		else if (position == 3) { if (isAccessibilitySettingsOn(this)) {
	          //  this.asSwitch.setImageResource(R.mipmap.open);
	        } else {
	        	open();
	            }}
		
		else if (position == 4) {
		    Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
			
	}
	}
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
	@Override
	public void onClick(View v) {	
	    switch (v.getId()) {
    case R.id.iv_avatar:
        break;     
	 }	
	}
	private void open()
	{
		try
		{
			Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
			startActivity(intent);
			Toast.makeText(this, "找到“简单的抢红包辅助”，然后开启服务即可", Toast.LENGTH_LONG).show();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

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

	@Override
	protected void initView() {
		super.initView();
		listView = (ListView) findViewById(R.id.lv);
		adapter = new MessageAdapter(this);
		adapter.setDatas(app.getMessages(), false);
		listView.setAdapter(adapter);	
		txtBanace=(TextView)findViewById(R.id.pointcount);
		txtSetting=(TextView)findViewById(R.id.setting);
		txtmoney2=(TextView)findViewById(R.id.money2);
		txtTotal=(TextView)findViewById(R.id.beancount);
		txtUserName=(TextView)findViewById(R.id.imei);
		adapter.notifyDataSetChanged();
		String avatar=PreferenceUtil.getStrValue(this,ParamName.USERIMAGE,"");

	     iv_avatar = (ImageView) this.findViewById(R.id.iv_avatar);
	      iv_avatar.setOnClickListener(new OnClickListener() { 
	    	   @Override
	    	   public void onClick(View arg0) {
	    
	    	        showPhotoDialog();
	  
	    	   }
	    	  });
	      if (fileIsExists(avatar))
	      {
	      showUserAvatar(iv_avatar, avatar);
	      }
	      
	      iv_vip = (ImageView) this.findViewById(R.id.iv_vip);
	      iv_viplevel = (ImageView) this.findViewById(R.id.iv_vip2);
	      iv_viplevel.getDrawable().setLevel(2);
	  	
	      iv_vip.setOnClickListener(new OnClickListener() {
	    	   
	    	   @Override
	    	   public void onClick(View arg0) {
	    
	    	        showVIPDialog();
	  
	    	   }
	    	  });   
	      iv_viplevel.setOnClickListener(new OnClickListener() {
	    	   
	    	   @Override
	    	   public void onClick(View arg0) {
	    
	    	        showVIPDialog();
	  
	    	   }
	    	  });   
	}	
	//http://www.jiandande.com//member/templets/images/dfboy.png
	//http://www.jiandande.com//member/templets/images/dfgirl.png
	  public static final String URL_Avatar = "http://www.jiandande.com/jiandande/upload/";
	  private void showUserAvatar(ImageView iamgeView, String avatar) {
		  final String url_avatar = URL_Avatar + avatar;
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
	            if (bitmap != null)
	                iamgeView.setImageBitmap(bitmap);

	        }
	  }
	
	    @SuppressLint("SimpleDateFormat")
	    private String getNowTime() {
	        Date date = new Date(System.currentTimeMillis());
	        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
	        return dateFormat.format(date);
	    }    
	  private void showPhotoDialog() {
	        final AlertDialog dlg = new AlertDialog.Builder(this).create();
	        dlg.show();
	        Window window = dlg.getWindow();
	        // *** 主要就是在这里实现这种效果的.
	        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
	        window.setContentView(R.layout.alertdialog);
	        // 为确认按钮添加事件,执行退出应用操作
	        TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
	        tv_paizhao.setText("拍照");
	        tv_paizhao.setOnClickListener(new View.OnClickListener() {
	            @SuppressLint("SdCardPath")
	            public void onClick(View v) {

	                imageName = getNowTime() + ".png";
	            
	                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	                // 指定调用相机拍照后照片的储存路径
	                intent.putExtra(MediaStore.EXTRA_OUTPUT,
	                        Uri.fromFile(new File("/sdcard/jiandande/", imageName)));
	         
	                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
	                dlg.cancel();
	            }
	        });
	        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
	        tv_xiangce.setText("相册");
	        tv_xiangce.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {

	                getNowTime();
	                imageName = getNowTime() + ".png";
	        
	                Intent intent = new Intent(Intent.ACTION_PICK, null);
	                intent.setDataAndType(
	                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
	                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

	                dlg.cancel();
	            }
	        });
	    }
	  private void showVIPDialog() {
			Intent intent=new Intent(UserCenterActivity.this,VipUserCenterActivity.class);
		    startActivity(intent);   
	    } 
	  
	  @SuppressLint("SdCardPath")
	    @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (resultCode == RESULT_OK) {
	            switch (requestCode) {
	            case PHOTO_REQUEST_TAKEPHOTO:

	                startPhotoZoom(
	                        Uri.fromFile(new File("/sdcard/jiandande/", imageName)),
	                        480);
	            
	                break;

	            case PHOTO_REQUEST_GALLERY:
	                if (data != null)
	                    startPhotoZoom(data.getData(), 480);
	                
	                break;
	            case PHOTO_REQUEST_CUT:
	                // BitmapFactory.Options options = new BitmapFactory.Options();
	                //
	                // /**
	                // * 最关键在此，把options.inJustDecodeBounds = true;
	                // * 这里再decodeFile()，返回的bitmap为空
	                // * ，但此时调用options.outHeight时，已经包含了图片的高了
	                // */
	                // options.inJustDecodeBounds = true;
	                Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/jiandande/"
	                        + imageName);  
	            	PreferenceUtil.saveStrValue(this,ParamName.USERIMAGE,"/sdcard/jiandande/"+imageName);
	                iv_avatar.setImageBitmap(bitmap);
	               // updateAvatarInServer(imageName);
	                break;
	            }
	            super.onActivityResult(requestCode, resultCode, data);
	        }
	    }
	  @SuppressLint("SdCardPath")
	    private void startPhotoZoom(Uri uri1, int size) {
	        Intent intent = new Intent("com.android.camera.action.CROP");
	        intent.setDataAndType(uri1, "image/*");
	        // crop为true是设置在开启的intent中设置显示的view可以剪裁
	        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
	        intent.putExtra("aspectX", 1);
	        intent.putExtra("aspectY", 1);
	        // outputX,outputY 是剪裁图片的宽高
	        intent.putExtra("outputX", size);
	        intent.putExtra("outputY", size);
	        intent.putExtra("return-data", false);

	        intent.putExtra(MediaStore.EXTRA_OUTPUT,
	                Uri.fromFile(new File("/sdcard/jiandande/", imageName)));
	        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
	        intent.putExtra("noFaceDetection", true); // no face detection
	        startActivityForResult(intent, PHOTO_REQUEST_CUT);
	    }
	


	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  private void showUserInfo() {
		UserInfo user=app.getUserInfo();
		
		if(user==null){//长期后台运行,内存变量如果被回收释放则重新加载
			Intent intent=new Intent();
			intent.setClass(UserCenterActivity.this, WelComeActivity.class);
		    startActivity(intent);
		    finish();
		}else{
		    
		
			
			txtUserName.setText("会员ID:"+StringUtil.getUserName(user));
			txtTotal.setText("总  赚："+StringUtil.moneyFormat(user.sumCount/1000)+"元");
			txtSetting.setText("联盟一："+StringUtil.moneyFormat(user.settling/1000)+"元");
			txtmoney2.setText("联盟二："+StringUtil.moneyFormat(user.money2/1000)+"元");
			txtBanace.setText("简单得："+StringUtil.moneyFormat(user.balance/1000)+"元");
			listView.setVisibility(View.VISIBLE);
		//	Toast.makeText(this,"VIP"+user.vip, Toast.LENGTH_LONG).show();	
			if (iv_viplevel!=null){ iv_viplevel.getDrawable().setLevel(user.vip);}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	//	showUserInfo();
	}
	@Override
	public void retAddIntegral(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void retCheckIntegral(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void retMinusIntegral(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
