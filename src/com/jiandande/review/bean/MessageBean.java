package com.jiandande.review.bean;

import java.util.ArrayList;



public class MessageBean {
	public   int msgid;
	public   String sendUserName;	
	public  String content ;	
	
	
	public MessageBean() {
	   	
	}
	public MessageBean(String sendUserName,String content){
	  this.sendUserName=sendUserName;
	  this.content=content;
	  //this.time=time;	
   }
	
	static String appInfoMsg="简单得APP，闲暇之余赚点零花钱，每日登录签到抽奖获取不定额的积分,积分可以兑换现金等礼品";
	public static MessageBean appInfo=new MessageBean("签到抽奖赚取积分",appInfoMsg);//JianDanDe.apk
	static String rateInfoMsg="通过联盟任务赚取更多的积分,每下载一个应用赚取不定额积分(例如：一个应用获得2800积分，将获得2.8元)";
	public static MessageBean rateInfo=new MessageBean("体验APP赚零用钱",rateInfoMsg);
	static String otherInfoMsg="简单得APP提供抢红包功能，支持QQ红包,微信红包,在睡觉休息的时候也能帮你自动抢红包";
	public static MessageBean otherInfo=new MessageBean("开启辅助自动抢红包",otherInfoMsg);
	static String VipInfoMsg="开通VIP会员尊享更多VIP特权服务。。。";
	public static MessageBean VipInfo=new MessageBean("开通VIP尊享更多特权服务",VipInfoMsg);
	static String ShareInfoMsg="改功能可以在微信朋友圈分享相册里的照片。。。";
	public static MessageBean ShareInfo=new MessageBean("分享图文到微信朋友圈",ShareInfoMsg);
	
	
	public static ArrayList<MessageBean>  createMessage(ArrayList<MessageBean> datas) {
		if(datas==null){
			datas=new ArrayList<MessageBean>();
			datas.add(appInfo);
			datas.add(rateInfo);
			datas.add(VipInfo);
			datas.add(otherInfo);
			return datas;
		}
		int  size=datas.size();
		 if(size==0){
			 datas.add(appInfo);
				datas.add(rateInfo);
			
				datas.add(VipInfo);
				datas.add(otherInfo);
	
		} else
		if(size==1){
			datas.add(rateInfo);
			datas.add(otherInfo);
		}else if(size==2){
			datas.add(otherInfo);
		}
		return datas;
	}
}
