package com.jiandande.review.transfer;


/***
 * 简单得 HTTP 响应码以及对照信息
 * 
 * modify
 * 
 */
public class HttpCodeHelper {

	public static final String SIGN_ERROR = "1";

	public static final String HTTP_TIME_OUT = "连接超时";
	public static final String RESPONSE_SUCCESS = "666666";
	public static final String ERROR = "-1";
	public static final String NO_PSAM = "3";
	public static final String VERSION_ERROR = "4";//软件版本错误

	public static final String NOT_LOGIN = "2";

	public static final String ORDER_NO_ESXIT = "3";

	public static final String INFO_NOT_SAME = "4";

	public static final String PARAM_ERROR = "5";

	public static final String BIND_PASSWORD = "6";

	public static final String PASSWORD_ERROR = "7";

	public static final String BALANCE_ERORR = "8";

	public static final String NOT_BINDINFO = "9";

	public static final String USER_NOT_ESXIT = "10";


	public static final String NO_NEW_VERSION = "11";

	public static final String NEED_LOGIN = "12";

	public static final String HTTP_REQUEST_ERROR = "-2";
	
	public static String getMessage(String respCode) {
	    if(RESPONSE_SUCCESS.equals(respCode)){
	    	return "成功";
	    }else if(SIGN_ERROR.equals(respCode)){
	    	return "数据签名错误，请到下载最新应用";
	    }else if(NOT_LOGIN.equals(respCode)){
	    	return "没有登陆";
	    }else if(ORDER_NO_ESXIT.equals(respCode)){
	    	return "订单不存在";
	    }else if(INFO_NOT_SAME.equals(respCode)){
	    	return "输入的信息不对应";
	    }else if(PARAM_ERROR.equals(respCode)){
	    	return "上传的信息不对";
	    }else if(BIND_PASSWORD.equals(respCode)){
	    	return "验证密码不对";
	    }else if(BALANCE_ERORR.equals(respCode)){
	    	return "余额不足";
	    }else if(BALANCE_ERORR.equals(respCode)){
	    	return "未绑定信息";
	    }else if(USER_NOT_ESXIT.equals(respCode)){
	    	return "用户不存在";
	    }else if(NO_NEW_VERSION.equals(respCode)){
	    	return "没有新的版本";
	    }else if(NEED_LOGIN.equals(respCode)){
	    	return "需要登录，转到登录页面";
	    }
	    return "未知错误";
	}
}