package com.jiandande.review.bean;
public class UserInfo {
	public static final int BINDED = 1;
	
	public static final int UNBINDED = 0;
	public int uid;	
	public int vip;	
	public int bindStatus;	
	public String imei;	
	public String accountNo;
	public String UserImage;
	public double balance;
	
	public double settled;
	public double settling;
	public double money2;//youmi
	
	public long lastUpdateTime;	
	public int aplyCount;
	public double sumCount;
	public double checkCount;//已兑积分	
	public String email;
	public String paymessage;
	
	public boolean haveFilter;
	public UserMoreInfo userMoreInfo;	
	public UserInfo() {
		this.uid=1;
		this.vip=0;
		this.bindStatus=0;
		this.imei="666666";
		
		this.accountNo="76589899788";
		this.haveFilter=false;
		this.UserImage="/sdcard/jiandande/";
		this.paymessage="";		
	}
}
