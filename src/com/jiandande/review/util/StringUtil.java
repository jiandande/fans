package com.jiandande.review.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



import com.jiandande.review.bean.UserInfo;
import com.jiandande.review.util.Constants.OrderStatus;
import com.jiandande.review.util.Constants.OrderType;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;

public class StringUtil {
	private static final String SHARE_NAME = "JianDanDE.COM";

	
	

	/**
	 * 检验是否为空
	 * 
	 * @param obj
	 * @return boolean
	 */
	public static boolean checkNotNull(Object obj) {
		if (null == obj ||"".equals(obj)) {
			return false;
		} else {
			if(obj.toString().trim().length()<=0)
			{
				return false;
			}
			return true;
		}
	}

	/**
	 * 检验是否为空
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean checkNotNull(String str) {
		if ( null == str ||str.trim().equals("") || str.length() == 0||"null".equalsIgnoreCase(str)) {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean checkIsNull(String str) {
		if ( null == str ||str.trim().equals("") || str.length() == 0||"null".equalsIgnoreCase(str)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 检验输入框是否为空
	 * @param editable
	 * @return
	 */
	public static boolean checkEditableNotNull(Editable editable)
	{
		if(editable==null)
		{
			return false;
		}else if(!checkNotNull(editable.toString())){
			return false;
		}else{
			return true;
		}
	}


	/***
	 * 从SharedPreferences中取出数据
	 * 
	 * @param context
	 * @param key
	 */
	public static String getSharedParam(Context context, String key) {
		SharedPreferences preferences = context.getSharedPreferences(
				SHARE_NAME, Context.MODE_PRIVATE);
		return preferences.getString(key, "");
	}
	
	/***
	 * 保存数据到SharedPreferences中
	 * 
	 * @param context
	 * @param key
	 * @param param
	 */
	public static void setSharedParam(Context context, String key, String param) {
		SharedPreferences preferences = context.getSharedPreferences(
				SHARE_NAME, Context.MODE_PRIVATE);
		preferences.edit().putString(key, param).commit();
	}
	
	/***
	 * 从SharedPreferences中取出数据
	 * 
	 * @param context
	 * @param key
	 */
	public static boolean getSharedBoolean(Context context, String key) {
		SharedPreferences preferences = context.getSharedPreferences(
				SHARE_NAME, Context.MODE_PRIVATE);
		return preferences.getBoolean(key, false);
	}
	
	/***
	 * 保存数据到SharedPreferences中
	 * 
	 * @param context
	 * @param key
	 * @param param
	 */
	public static void setSharedBoolean(Context context, String key, boolean param) {
		SharedPreferences preferences = context.getSharedPreferences(
				SHARE_NAME, Context.MODE_PRIVATE);
		preferences.edit().putBoolean(key, param).commit();
	}
	
	/***
	 * 从SharedPreferences中取出数据
	 * 
	 * @param context
	 * @param key
	 */
	public static float getSharedDouble(Context context, String key) {
		SharedPreferences preferences = context.getSharedPreferences(
				SHARE_NAME, Context.MODE_PRIVATE);
		return preferences.getFloat(key, 0);
	}
	
	/***
	 * 保存数据到SharedPreferences中
	 * 
	 * @param context
	 * @param key
	 * @param param
	 */
	public static void setSharedFloat(Context context, String key, Float param) {
		SharedPreferences preferences = context.getSharedPreferences(
				SHARE_NAME, Context.MODE_PRIVATE);
		preferences.edit().putFloat(key, param).commit();
	}
	
	/***
	 * 从SharedPreferences中取出数据
	 * 
	 * @param context
	 * @param key
	 */
	public static int getSharedInt(Context context, String key) {
		SharedPreferences preferences = context.getSharedPreferences(
				SHARE_NAME, Context.MODE_PRIVATE);
		return preferences.getInt(key, 0);
	}
	
	/***
	 * 保存数据到SharedPreferences中
	 * 
	 * @param context
	 * @param key
	 * @param param
	 */
	public static void setSharedInt(Context context, String key, int param) {
		SharedPreferences preferences = context.getSharedPreferences(
				SHARE_NAME, Context.MODE_PRIVATE);
		preferences.edit().putInt(key, param).commit();
	}
	
    /***
     * 递归把第一个字符为0替换空
     * @param str
     * @return
     */
    public static String replaceStartZero(String str)
    {
    	if(str.startsWith("0"))
    	{
    		str = replaceStartZero(str.substring(1,str.length()));
    	}
    	return str;
    }
    
    /***
     * 把以分的金额单位转化为以元为单位
     * @param money
     * @return 100=￥1.00
     */
    public static String formatMoney(String money)
    {
    	if(money==null||money.length()==0)
    	{
    		return "￥0.00";
    	}
    	else{
    		money = replaceStartZero(money);
    		if(money==null||money.length()==0)
        	{
        		return "￥0.00";
        	}
    		String newMoney = "";
    		int length = money.length();
    		if(length==1)
    		{
    			newMoney +="0.0"+money;
    		}else if(length==2)
    		{
    			newMoney +="0."+money;
    		}else
    		{
    			newMoney =money.substring(0,length-2)+"."+money.substring(length-2,length);
    		}
    		return "￥"+newMoney;
    	}
    }
    
    /***
     * 把以分的金额单位转化为以元为单位
     * @param money
     * @return
     */
    public static String formatPayMoney(int money)
    {
    	return formatPayMoney(money+"");
    }
    
    /***
     * 把以分的金额单位转化为以元为单位
     * @param money
     * @return
     */
    public static String formatPayMoney(String money)
    {
    	if(money==null||money.length()==0)
    	{
    		return "0.00";
    	}
    	else{
    		money = money.replace(".", money);
    		money = replaceStartZero(money);
    		if(money==null||money.length()==0)
        	{
        		return "0.00";
        	}
    		String newMoney = "";
    		int length = money.length();
    		if(length==1)
    		{
    			newMoney +="0.0"+money;
    		}else if(length==2)
    		{
    			newMoney +="0."+money;
    		}else
    		{
    			newMoney =money.substring(0,length-2)+"."+money.substring(length-2,length);
    		}
    		return newMoney;
    	}
    }

    /**
     * 格式化金额   100 = 100.00
     * @param money
     * @return
     */
    public static String formateMoney(String money)
    {
    	if(checkNotNull(money))
    	{
    		money = "0";
    	}
    	return money + ".00";
    }
    
    public static String moneyFormat(double money)
    {
    	NumberFormat formater = new DecimalFormat("0.00");
    	String shouxufei = formater.format(money);
		return shouxufei;
    }
    public static String moneyFormat2(double money)
    {
    	NumberFormat formater = new DecimalFormat("0");
    	String shouxufei = formater.format(money);
		return shouxufei;
    }
    
    /**
     * 屏蔽卡号
     * @param cardNo  银行卡号
     * @return  屏蔽后的卡号
     */
    public static String formatCardNo(String cardNo) {
    	if(!checkNotNull(cardNo))
    	{
    		LogUtil.e("屏蔽卡号"," cardNo null");
    		return "";
    	}
    	int length = cardNo.length();
//    	if(length<14||length>20)
//    	{
//    		LogUtil.e("屏蔽卡号","length error");
//    		return "";
//    	}
    	String shieldedText = "";
    	for (int i = 0; i < length-10; i++) {
    		shieldedText+="●";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(cardNo.substring(0, 6))
			.append(shieldedText)
			.append(cardNo.substring(length-4,length));
		LogUtil.i("屏蔽后卡号",sb.toString());
		return sb.toString();
	}
   

    /***
     * 格式化时间
     * @param time  比如： 20120331151638
     * @return   2012-03-31 15:16:38
     */
    public static String formatTime(String time)
    {
    	String timeText = "";
    	if(checkNotNull(time)&&time.length()==14)
    	{
    		StringBuilder sb = new StringBuilder();
    		sb.append(time.substring(0, 4))
    			.append("-")
    			.append(time.substring(4,6))
    			.append("-")
    			.append(time.substring(6,8))
    			.append(" ")
    			.append(time.substring(8, 10))
    			.append(":")
    			.append(time.substring(10,12))
    			.append(":")
    			.append(time.substring(12, 14));
    		timeText = sb.toString();
    		sb = null;
    		
    	}
    	return timeText;
    }
    /***
     * 得到当前日期
     * @return   2012年4月10日   =   20120410
     */
    public static String getCurDate()
    {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    	return dateFormat.format(new Date());
    }
    
    /***
     * 得到标准格式当前时间
     * @return   2012-04-17 11:11:19
     */
    public static String getCurTime()
    {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	return dateFormat.format(new Date());
    }
    
    
    /***
     * 得到格式化后当前时间
     * @return   20120417111119
     */
    public static String getFormatCurTime()
    {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    	return dateFormat.format(new Date());
    }
    
    /***
     * 得到格式化后当前时间
     * @return   20120417111119
     */
    public static String getFormatLongTime(long longTime)
    {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    	return dateFormat.format(new Date(longTime));
    }
    /**
     * int to boolean 
     * @param state
     * @return
     */
    public static boolean intToBoolean(int state)
    {
    	if(state==1)
    	{
    		return true;
    	}else{
    		return false;
    	}
    }
    
    /**
     * boolean to int 
     * @param state
     * @return
     */
    public static int booleanToInt (boolean state)
    {
    	if(state)
    	{
    		return 1;
    	}else{
    		return 0;
    	}
    }
    
    /***
	 * String to Unicode
     * @param str
     * @return
     */
    public static String GBK2Unicode(String str) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char chr1 = (char) str.charAt(i);
			if (!isNeedConvert(chr1)) {
				result.append(chr1);
				continue;
			}
			result.append("\\u" + Integer.toHexString((int) chr1));
		}
		return result.toString();
	}

	public static boolean isNeedConvert(char para) {
		return ((para & (0x00FF)) != para);
	}

	/**
	 * String to Unicode2
	 * @param str
	 * @return
	 */
	public static String GBK2Unicode2(String str) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char chr1 = (char) str.charAt(i);
			result.append("&#" + Integer.toString((int) chr1) + ";");
		}
		return result.toString();
	}

	/**
	 * @param status
	 * @return
	 * 判断是否实名认证
	 */
	public static boolean checkIsReal(String status) {
		char realname=status.charAt(0);
		 return realname=='1'||realname=='2';
	}

	
	

	

	public static boolean checkIsNullOrNULL(String str) {
		if ( null == str ||str.trim().equals("") || str.length() == 0||"NULL".equalsIgnoreCase(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	public static Calendar formateCalendar(String date) {
		if(!checkIsNull(date)){
			String[]dates=date.split("-");
			if(dates.length==3){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date dt=sdf.parse(date);
					Calendar cal=Calendar.getInstance();
					cal.setTime(dt);
					return cal;
				} catch (ParseException e) {
					e.printStackTrace();
				}	
			}
		}
		return null;
	}

	public static String formatBankCard(CharSequence content) {
		if(content!=null){
			String  str=content.toString();
			int length=str.length();
			int count=length/4;
			StringBuffer buffer=new StringBuffer();
			for(int i=0;i<=count;i++){
				if(i==count){
					buffer.append(str.substring(4*i));
				}else{
					buffer.append(str.substring(i*4,(i+1)*4));
					buffer.append(" ");
				}
			}
			return buffer.toString();
		}
		return null;
	}
	
	
	public static String clearFormat(String account) {
		if(account!=null){
			return account.replaceAll(" ", "");
		}
		return account;	
	}

	public static double calculateFee(int aplyCount) {
		if(aplyCount>0){
			return 1;
		}
		return 0;
	}

	public static CharSequence formatTransStatus(int status) {
		if(OrderStatus.STATUS_SUCCESS==status){
			return "已付款";
		}else if(OrderStatus.STATUS_WAITFORRECEIVE==status){
			return "待领取";		
		}else if(OrderStatus.STATUS_FAIL==status){
			return "未审核";
		}else if(OrderStatus.STATUS_PROCESS==status){
			return "审核中";
		}else if(OrderStatus.STATUS_FINISH==status){
			return "已完成";
		}
		
		return "未知";
	}

	public static CharSequence formatTransType(int orderType) {
		if(OrderType.TYPE_ALIPAY==orderType){
			return "支付宝转账";
		}else if(OrderType.TYPE_PHONE==orderType){
			return "手机充值";
		}else if(OrderType.TYPE_QQ==orderType){
			return "QQ充值";
		}else if(OrderType.TYPE_SIGN==orderType){
		return "签到奖励";
	}else if(OrderType.TYPE_LUCK==orderType){
		return "幸运大抽奖";
	}else if(OrderType.TYPE_OWNBUSINESS==orderType){
		return "自营任务奖励";
	}
	
	else if(OrderType.TYPE_UNION1==orderType){
		return "联盟一奖励";
	}else if(OrderType.TYPE_UNION2==orderType){
		return "联盟二奖励";
	}else if(OrderType.TYPE_INVITOR==orderType){
		return "邀请密友奖励";
	}
		
		return "奖励";
	}
	
	public static CharSequence formatTransID(int orderId) {
		
		return ""+orderId;
	}
	
	public static String formatData(String dataFormat, long timeStamp) {
		if (timeStamp == 0) {
			return "";
		}
		timeStamp = timeStamp * 1000;
		String result = "";
		SimpleDateFormat format = new SimpleDateFormat(dataFormat);
		result = format.format(new Date(timeStamp));
		return result;
	}

	
	
	
   
	@SuppressLint("SimpleDateFormat")
	public static CharSequence formatTime(long submitTime) {
		try {
		//	Date date=new Date(1478599350);
		//	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//	return formatter.format(date);
		
		return 	formatData("yyyy-MM-dd HH:mm:ss", submitTime);
		
		} catch (Exception e) {
			e.printStackTrace();
			return ""+submitTime;
		}
	}

	public static CharSequence getUserName(UserInfo user) {
		// TODO Auto-generated method stub
		if(checkIsNull(user.email)){
			return user.accountNo;
		}
		int index =user.email.lastIndexOf("@");
		if(index>0){
			return user.email.substring(0, index);
		}
		return user.email;
	}


}
