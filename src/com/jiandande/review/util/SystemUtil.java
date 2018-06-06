package com.jiandande.review.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SystemUtil {

	public static boolean firstStartApp = true;

	public static final int TID_NOT_EXISTS = -1;
	/** 得到分辨率高度 */
	public static int heightPs = -1;
	/** 得到分辨率宽度 */
	public static int widthPs = -1;
	/** 得到屏幕密度 */
	public static int densityDpi = -1;
	/** 得到X轴密度 */
	public static float Xdpi = -1;
	/** 得到Y轴密度 */
	public static float Ydpi = -1;

	private Context context;

	public SystemUtil(Context context) {
		this.context = context;
	}

	/***
	 * 得到手机的屏幕基本信息
	 * 
	 * @param context
	 */
	public static void getScreen(Activity context) {
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		heightPs = metrics.heightPixels;
		widthPs = metrics.widthPixels;
		densityDpi = metrics.densityDpi;
		Xdpi = metrics.xdpi;
		Ydpi = metrics.ydpi;
		LogUtil.i("手机分辨率", "分辨率：" + widthPs + "X" + heightPs + "    屏幕密度："
				+ densityDpi + "    宽高密度：" + Xdpi + "X" + Ydpi);
	}

	  private String getMD5Str(String str) {       
	      MessageDigest messageDigest = null;       
	     
	      try {       
	          messageDigest = MessageDigest.getInstance("MD5");       
	     
	          messageDigest.reset();       
	     
	          messageDigest.update(str.getBytes("UTF-8"));       
	      } catch (NoSuchAlgorithmException e) {       
	          System.out.println("NoSuchAlgorithmException caught!");       
	          System.exit(-1);       
	      } catch (UnsupportedEncodingException e) {       
	          e.printStackTrace();       
	      }       
	     
	      byte[] byteArray = messageDigest.digest();       
	     
	      StringBuffer md5StrBuff = new StringBuffer();       
	        
	      for (int i = 0; i < byteArray.length; i++) {                   
	          if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)       
	              md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));       
	          else       
	              md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));       
	      }       
	    //16位加密，从第9位到25位  
	      return md5StrBuff.substring(8, 24).toString().toUpperCase();      
	  }  
	
	
	/***
	 * 获取客户端版本
	 * 
	 * @param context
	 * @return
	 */
	public PackageInfo getVersion() {
		PackageInfo info = null;
		try {
			PackageManager manager = context.getPackageManager();
			info = manager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			LogUtil.e("version", "获取版本失败");
			e.printStackTrace();
		}
		return info;
	}

	/***
	 * 获取手机model
	 */
	public static String getPhoneMode() {
		return android.os.Build.MODEL;
	}

	/**
	 * 把密度dip单位转化为像数px单位
	 * 
	 * @param context
	 * @param dip
	 * @return
	 */
	public static int dipToPx(Context context, int dip) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	/***
	 * 把像数px转化为密度dip单位
	 * 
	 * @param context
	 * @param px
	 * @return
	 */
	public static int pxToDip(Context context, int px) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px * scale + 0.5f * (px >= 0 ? 1 : -1));
	}

	/**
	 * 获取IMEI号码
	 * @throws NoSuchAlgorithmException 
	 */
	public String getPhoneIMEI() {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneIMEI = null;
		if (telephonyManager != null) {
			phoneIMEI = telephonyManager.getDeviceId();
		}
		if (!StringUtil.checkNotNull(phoneIMEI)) {
			/**
			 * 前6位数（TAC，Type Approval Code)是"型号核准号码"，一般代表机型。 2、接着的2位数（FAC，Final
			 * Assembly Code)是"最后装配号"，一般代表产地。 3、之后的6位数（SNR)是"串号"，一般代表生产顺序号。
			 * 4、最后1位数（SP)通常是"0"，为检验码，目前暂备用。
			 */
			Random random = new Random();
			int number = random.nextInt(10) * 100000 * +random.nextInt(10000);
			phoneIMEI = "86945900" + number + "0";
		}

		return getMD5Str(phoneIMEI);
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public static String getverson(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}

	public static int getversonCode(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			
			return info.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
			return 11;
		}
	}

	/**
	 * 获取IMEI号码
	 * @throws NoSuchAlgorithmException 
	 */
	public String getNewPhoneIMEI()  {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneIMEI = null;
		if (telephonyManager != null) {
			phoneIMEI = telephonyManager.getDeviceId();
		}
		if (!StringUtil.checkNotNull(phoneIMEI)) {
			phoneIMEI = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID); 
		}
		if (!StringUtil.checkNotNull(phoneIMEI)) {
			/**
			 * 前6位数（TAC，Type Approval Code)是"型号核准号码"，一般代表机型。 2、接着的2位数（FAC，Final
			 * Assembly Code)是"最后装配号"，一般代表产地。 3、之后的6位数（SNR)是"串号"，一般代表生产顺序号。
			 * 4、最后1位数（SP)通常是"0"，为检验码，目前暂备用。
			 */
			Random random = new Random();
			int number = random.nextInt(10) * 100000 * +random.nextInt(10000);
			phoneIMEI = "86945900" + number + "0";
		}
		
	
		return getMD5Str(phoneIMEI);

	}
	
	
	 public static String  getPhoneIMEI2(Context context)  {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneIMEI = null;
		if (telephonyManager != null) {
			phoneIMEI = telephonyManager.getDeviceId();
		}
		if (!StringUtil.checkNotNull(phoneIMEI)) {
			phoneIMEI = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID); 
		}
		if (!StringUtil.checkNotNull(phoneIMEI)) {
	
			Random random = new Random();
			int number = random.nextInt(10) * 100000 * +random.nextInt(10000);
			phoneIMEI = "86945900" + number + "0";
		}
		
	
		return phoneIMEI;

	}
	
	
	
	 public static String getIpAddr(Context context){
		   WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

	       WifiInfo info = wifiManager.getConnectionInfo();

	      return Formatter.formatIpAddress(info.getIpAddress());
	   }
}
