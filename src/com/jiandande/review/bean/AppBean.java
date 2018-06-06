package com.jiandande.review.bean;

import java.util.ArrayList;



public class AppBean {
	public String title;
	public String desc;

	public static ArrayList<AppBean> findBeanList() {
		ArrayList<AppBean> beanList = new ArrayList<AppBean>();
		AppBean bean = new AppBean();
		bean.title = "签到赚积分";
		bean.desc = "每天签到获得不定额积分";
		beanList.add(bean);
		bean = new AppBean();
		bean.title = "幸运大抽奖";
		bean.desc = "祝愿每天都成为幸运日,进入幸运大转盘,每天赚取不定额积分";
		beanList.add(bean);
		bean = new AppBean();
		bean.title = "联盟任务一";
		bean.desc = "通过联盟赚取更多的积分,在联盟任务墙里赚取的积分将自动同步到简单得积分";
		
		beanList.add(bean);
		bean = new AppBean();
		bean.title = "联盟任务二";
		bean.desc = "赚取更多的积分,在联盟任务二里赚取的积分将自动同步到简单得积分";
		beanList.add(bean);
		bean = new AppBean();
		bean.title = "邀请密友";
		bean.desc = "每日与好友分享,成功邀请密友各得666积分(需要完善个人资料)";
		beanList.add(bean);
		bean = new AppBean();
		bean.title = "开通VIP会员";
		bean.desc = "开通VIP,尊享任务积分加成等特权,更多的服务，更多的特权,如有疑问请加QQ群:205275168";
		beanList.add(bean);
		return beanList;
	}
}
