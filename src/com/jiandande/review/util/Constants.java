package com.jiandande.review.util;

/***
 * 常用key
 * 
 * @author smz
 * 
 */
public final class Constants {

	public  interface	OrderStatus{
		public static final int STATUS_SUCCESS = 1;
		public static final int STATUS_FINISH = 2;

		public static final int STATUS_PROCESS = 0;
		public static final int STATUS_FAIL = -1;
		
		public static final int STATUS_WAITFORRECEIVE = 3;//新增自营任务 待领取状态
	}
	
	public  interface	PlatType{
		String YOUMI="YOUMI";
		String BAIDU="BAIDU";
		String DIANLE="DIANLE";
		String WANPU = "WANPU";
		String DATOUNIAO ="DATOUNIAO";
	  }
	public  interface	OrderType{
		  int TYPE_PHONE=1;
		  int TYPE_QQ=2;
		  int TYPE_OWNBUSINESS=3;//自营任务
		  int TYPE_ALIPAY=0;
		  int TYPE_SIGN=5;
		  int TYPE_LUCK=6;
		  int TYPE_UNION1=7;
		  int TYPE_UNION2=8;
		  int TYPE_INVITOR=9;
	  }
	 
	
	
	
	
	
	
	
	
	
	 public interface ServerUrl{
	//	String baseurl="http://192.168.1.102/member" ;
	//	String baseurl="http://www.jiandande.com/member" ; 
		String baseurl="https://www.jiandande.com/member" ; //测试是否可以过滤运营商的流量广告?
		String feedBack=baseurl+"/user/feedBack";
		String addMoney=baseurl+"/addmoney";
		String applyOrder=baseurl+"/giftpay_mobile.php";
		String findOrder=baseurl+"/ReturnChargeOrder.php";
		String adminQueryOrder=baseurl+"/order/adminQueryOrder";
			
		String bindMoreInform=baseurl+"/binduserInfo.php";
		String clientLogin=baseurl+"/login_phone.php";
		String ClientSignIn=baseurl+"/ajax_sign_mobile.php";
		String SyncUnionTask=baseurl+"/SyncUnionTaskMoney.php";//同步联盟任务积分

		
		
				
		String clientgetaword = baseurl+"/getawordranking.php";
		String clientawardranking = baseurl+"/awardranking.php";
		
		String clientluckyday = baseurl+"/luckyday/index.html";
		
		String ownbusinesstask = baseurl+"/ownbusinesstask.php";
		
		String clientluckypoint = baseurl+"/ajax_sign_lucky.php";
		String clientRegister=baseurl+"/reg_new_mobile.php";
		
		String findBackPassword=baseurl+"/user/findBackPassword";
		String findBindInfo=baseurl+"/user/findBindInfo";
		String perfectInform=baseurl+"/user/perfectInform";
		String updateMoreInform=baseurl+"/user/updateMoreInform";
		String findUserInfo = baseurl+"/login_phone.php";
	
		//String checkVersion =  baseurl+"/mypbulic/checkVerson";
		String checkVersion =  baseurl+"/checkVerson.php";
	//	String findbalanceChange = baseurl+"/order/findBanaceChange";
		String can_download = baseurl+"/user/canDownLoad";
		
	 }
	 public interface ParamName {
		    String RESD_CODE = "respCode";
			String RESD_DESC = "respDesc";
			String WP_POINT = "wp_point";
			String WANPU_DOWNLOAD = "WANPU_DOWNLOAD";
			static String MYSELF="self";
			static String CHECK_VERSION = "check_version";
			static String FEED_CONTENT = "feedcontent";
			static String ORDER = "order";
			static String BAIDU_DOWNLOAD = "baidu_download";
			static String YOUMI_DOWNLOAD = "youmi_download";
			static String DIANLE_DOWNLOAD = "dianle_download";
			static String PAGE = "page";
			
			static String MID = "mid";
			static String BUYID = "buyid";
			
			static String APPNAME = "appname";
			static String DELETE = "delete";
			static String USER = "user";
			static String SENDTIME = "sendTime";
			static String SIGNER_DATA = "signData";
			static String USERNAME = "username";
			static String USERIMAGE = "userimage";
			static String AUTO_LOGIN = "auto_login";
			static String PASSWORD = "userpwd";
			static String SEX = "sex";
			static String LUCKYPOINT = "luckyscore";//幸运大转盘积分
			static String UnionPOINT = "addmoney";//联盟积分
			static String PLAYTYPE = "plattype";//联盟积分
			static String EMAIL = "email";
			static String PRICE = "price";
			static String MESSAGE = "message";
			static String DOWNLOADOK = "downloadok";
		
			static String IMEI = "imei";
			static String INVITECODE = "invitecode";
			static String QQNO = "qqno";
			static String QQ_NAME = "qqname";
			static String PHONE_NO = "phoneno";
			static String ATTRBUTE = "attribute";
			static String ALIPAYNO = "alipay_no";
			static String ALIPAYNAME = "alipay_name";
			static String USERID = "userid"; 
			static String PACKAGENAME = "packagename"; 
			static String PNAME = "pname"; 
			static String OWNBUSINESS = "ownbusiness";
			static String UNAME = "uname";//昵称
			static String JDDNUMBER = "jddnumber";//IMEI  预防模拟IMEI 
			static String TYPE = "type";
			static String AMOUNT = "addmoney";
			static String FEE = "fee";
			static String ORDERID = "orderid";
			static String STATUS = "status";
			static String REMARK = "remark";
			static String STATE_DATE = "start_date";
			static String END_DATE = "end_date";
			static String RESULT = "result";
			static String  SOURCE="source";
		}


	public static final String SHARENAME = "jiandandeapp";
	public static final String APPNAME = "jiandande";
}
