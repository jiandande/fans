package com.jiandande.review.transfer;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;




import com.jiandande.review.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.SSLException;

public class HttpsClient {
    private static final String TAG = "qdbHttps";
    
    private static final String CHARSET = HTTP.UTF_8;
    private static HttpClient httpClient;
    
    /**钱袋宝平台连接超时*/
    public static final int CONN_TIME_OUT = 25;
    /**钱袋宝平台请求超时*/
    public static final int RECV_TIME_OUT = 90;
    
    /**易酷服务器连接超时*/
    public static final int EK_SERVER_CONN_TIME_OUT = 30;
    /**易酷服务器请求超时*/
    public static final int EK_SERVER_RECV_TIME_OUT = 30;
    
    //异常错误信息
    private static String errInfo;
    
    //连接超时时间
    public static int connTimeout = CONN_TIME_OUT ;
    //应答超时时间
    public static int recvTimeout = RECV_TIME_OUT ;
    /**服务器端的响应码*/
    private static String responseCode ;
    
    private static ClientConnectionManager conMgr;
    
    private HttpsClient(){
        
    }
    
    public static synchronized HttpClient getHttpClient() {
        if (null == httpClient) {
            HttpParams params = new BasicHttpParams();
            // 设置一些基本参数
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, CHARSET);
            HttpProtocolParams.setUseExpectContinue(params, false);
            // 超时设置
            /* 从连接池中取连接的超时时间 */
//            ConnManagerParams.setTimeout(params, 1000);
//            /* 连接超时 */
//            HttpConnectionParams.setConnectionTimeout(params, connTimeout*1000);
//            /* 请求超时 */
//            HttpConnectionParams.setSoTimeout(params, recvTimeout*1000);
            
            // 设置我们的HttpClient支持HTTP和HTTPS两种模式
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            
            SSLSocketFactory sf;
			try {
				KeyStore trustStore =KeyStore.getInstance(KeyStore.getDefaultType());
				trustStore.load(null,null);
				sf = new FySSlSocketFactory(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				schReg.register(new Scheme("https", sf, 443));
			} catch (Exception e) {
				schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
				e.printStackTrace();
			}
            // 使用线程安全的连接管理来创建HttpClient
			conMgr = new ThreadSafeClientConnManager(params, schReg);
			
//			conMgr.requestConnection(new HttpRoute(new HttpHost(AbstractTransaction.BASE_URL)), null);
            httpClient = new DefaultHttpClient(conMgr, params);
            ((DefaultHttpClient) httpClient).setHttpRequestRetryHandler(requestRetryHandler);
//            ((DefaultHttpClient) httpClient).addRequestInterceptor(itcp, index)
        }
        
        return httpClient;
    }
    
    
    public static InputStream get(String url,Map<String, String> sendData)
    {
    	 LogUtil.i(TAG, url);
         HttpGet request = new HttpGet(url);
         try {
             HttpContext localContent = new BasicHttpContext();
             BasicHttpParams params = new BasicHttpParams();
             /* 连接超时 */
             HttpConnectionParams.setConnectionTimeout(params, EK_SERVER_CONN_TIME_OUT*1000);
             /* 请求超时 */
             HttpConnectionParams.setSoTimeout(params, EK_SERVER_RECV_TIME_OUT*1000);
             if(sendData != null && sendData.size() > 0)
             {
            	 Iterator<String> it =  sendData.keySet().iterator();
            	 while (it.hasNext())
            	 {
            		 String key = it.next();
            		 String value = sendData.get(key);
            		 LogUtil.i(TAG, key +"=" +value);
            		 params.setParameter(key,value);
            	 }
             }
             request.setParams(params);
             // 发送请求
             responseCode = HttpCodeHelper.HTTP_TIME_OUT;
             HttpClient client = getHttpClient();
             //检验网络如果是中国移动wap上面，就需要添加中国移动代理
//            if(NetworkUtil.getNetworkType(QDBAppliaction.mCurrentActivity)==NetworkUtil.WAP_CONNECTED)
//             {
//             	HttpHost proxy = new HttpHost("10.0.0.172", 80);
//             	client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
//             }
             errInfo = "";   //发送以前清空错误信息
             HttpResponse response = client.execute(request,localContent);
             LogUtil.i(TAG, "StatusCode="+response.getStatusLine().getStatusCode());
             if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
             	request.abort();   //终止请求
             	LogUtil.i(TAG, "response is not 200");
             	errInfo += Integer.toString(response.getStatusLine().getStatusCode());
             	return null;
             }
//             readHeader(response);
             Header header = response.getFirstHeader("responseCode");
             if(response != null && header != null )
             {
             	responseCode =header.getValue();  //得到服务器端的响应码
             }
             LogUtil.i(TAG,"responsecode="+responseCode);
             HttpEntity resEntity =  response.getEntity();
            
         	return resEntity.getContent();
         } catch (UnsupportedEncodingException e) {
         	request.abort();
         	e.printStackTrace();
         	LogUtil.i(TAG, "UnsupportedEncodingException="+e.getMessage());
             errInfo += e.getMessage();
             return null;
         } catch (ClientProtocolException e) {
         	request.abort();
         	e.printStackTrace();
         	LogUtil.i(TAG, "ClientProtocolException="+e.getMessage());
             errInfo += e.getMessage();
             return null;
         } catch (IOException e) {
//         	request.abort();
         	e.printStackTrace();
         	LogUtil.i(TAG, "IOException="+e.getMessage());
            errInfo += e.getMessage();
            executeException(e);
            return null;
         }
    }
  
    public static InputStream post(String url, String xml){
        LogUtil.i(TAG, url);
        HttpPost request = new HttpPost(url);
        request.setHeader("Content-Type", "text/xml");
        request.setHeader("charset", "UTF-8");
//        request.setHeader("connection","keep-alive");
//        request.setHeader("Keep-Alive","120");
        BasicHttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, CONN_TIME_OUT*1000);/* 连接超时 */
        HttpConnectionParams.setSoTimeout(params, RECV_TIME_OUT*1000);/* 请求超时 */
        request.setParams(params);
        try {
            HttpContext localContent = new BasicHttpContext();
            // 创建POST请求
            if(xml != null )
            {
            	StringEntity entity = new StringEntity(xml, CHARSET);
                request.setEntity(entity);
            }
            // 发送请求 
            HttpClient client = getHttpClient();
            LogUtil.i(TAG, "---------读取request header----------");
            readHeader(request.getAllHeaders());
            LogUtil.i(TAG, "---------读取request header 完成----------");
            errInfo = "";   //发送以前清空错误信息
            responseCode = HttpCodeHelper.HTTP_TIME_OUT;
            HttpResponse response = client.execute(request,localContent);
            LogUtil.i(TAG, "---------读取response header----------");
            readHeader(response.getAllHeaders());
            LogUtil.i(TAG, "---------读取response header 完成----------");
            LogUtil.i(TAG, "StatusCode="+response.getStatusLine().getStatusCode());
            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            	request.abort();   //终止请求
            	LogUtil.i(TAG, "response is not 200");
            	errInfo += Integer.toString(response.getStatusLine().getStatusCode());
            	return null;
            }
//            readHeader(response);
            Header header = response.getFirstHeader("responseCode");
            if(response != null && header != null )
            {
            	responseCode =header.getValue();  //得到服务器端的响应码
            }
            LogUtil.i(TAG,"responsecode="+responseCode);
            HttpEntity resEntity =  response.getEntity();
           
        	return resEntity.getContent();
        } catch (UnsupportedEncodingException e) {
        	request.abort();
        	e.printStackTrace();
        	LogUtil.i(TAG, "UnsupportedEncodingException="+e.getMessage());
            errInfo += e.getMessage();
            return null;
        } catch (ClientProtocolException e) {
        	request.abort();
        	e.printStackTrace();
        	LogUtil.i(TAG, "ClientProtocolException="+e.getMessage());
            errInfo += e.getMessage();
            return null;
        } catch (IOException e) {
//        	request.abort();
        	e.printStackTrace();
        	LogUtil.i(TAG, "IOException="+e.getMessage());
            errInfo += e.getMessage();
            executeException(e);
            return null;
        }
    }
   
    public static InputStream post(String url,  ArrayList<NameValuePair> paramList){
        LogUtil.i(TAG, url);
        HttpPost request = new HttpPost(url);
        //request.setHeader("Content-Type", "text/xml");
        request.setHeader("charset", "UTF-8");
//        request.setHeader("connection","keep-alive");
//        request.setHeader("Keep-Alive","120");
        BasicHttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, CONN_TIME_OUT*1000);/* 连接超时 */
        HttpConnectionParams.setSoTimeout(params, RECV_TIME_OUT*1000);/* 请求超时 */
        request.setParams(params);
        try {
            HttpContext localContent = new BasicHttpContext();
            // 创建POST请求
            // 创建POST请求
            if(paramList != null)
            {
            	//222UrlEncodedFormEntity entity=new UrlEncodedFormEntity(paramList, HTTP.UTF_8);  
            	
            	UrlEncodedFormEntity entity=new UrlEncodedFormEntity(paramList, "gb2312");   
                request.setEntity(entity);
            }
            // 发送请求 
            HttpClient client = getHttpClient();
            LogUtil.i(TAG, "---------读取request header----------");
            readHeader(request.getAllHeaders());
            LogUtil.i(TAG, "---------读取request header 完成----------");
            errInfo = "";   //发送以前清空错误信息
            responseCode = HttpCodeHelper.HTTP_TIME_OUT;
            HttpResponse response = client.execute(request,localContent);
            LogUtil.i(TAG, "---------读取response header----------");
            readHeader(response.getAllHeaders());
            LogUtil.i(TAG, "---------读取response header 完成----------");
            LogUtil.i(TAG, "StatusCode="+response.getStatusLine().getStatusCode());
            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            	request.abort();   //终止请求
            	LogUtil.i(TAG, "response is not 200");
            	errInfo += Integer.toString(response.getStatusLine().getStatusCode());
            	return null;
            }
            HttpEntity resEntity =  response.getEntity();
           
        	return resEntity.getContent();
        } catch (UnsupportedEncodingException e) {
        	request.abort();
        	e.printStackTrace();
        	LogUtil.i(TAG, "UnsupportedEncodingException="+e.getMessage());
            errInfo += e.getMessage();
            return null;
        } catch (ClientProtocolException e) {
        	request.abort();
        	e.printStackTrace();
        	LogUtil.i(TAG, "ClientProtocolException="+e.getMessage());
            errInfo += e.getMessage();
            return null;
        } catch (IOException e) {
//        	request.abort();
        	e.printStackTrace();
        	LogUtil.i(TAG, "IOException="+e.getMessage());
            errInfo += e.getMessage();
            executeException(e);
            return null;
        }
    }
   
    /***
     * 监听错误信息
     * @param e
     */
    private static void executeException(Exception e)
    {
    	if(e == null )
    	{
    		return;
    	}
    	if(e.getMessage() != null )
    	{
    		LogUtil.i(TAG, "Exception="+e.getMessage());
    		if(e.getMessage().toLowerCase().contains("timed out"))
    		{
    			responseCode = HttpCodeHelper.HTTP_TIME_OUT;
    			LogUtil.e(TAG, "请求超时");
    		}else{
    			responseCode = HttpCodeHelper.HTTP_REQUEST_ERROR;
    			LogUtil.i(TAG, "其他请求异常错误");
    		}
    	}
    }
    
    /***
     * 读取header中的内容
     * @param response
     */
    private static void readHeader(Header[] headers)
    {
    	if(headers != null && headers.length > 0 )
    	{
    		for(Header header : headers)
    		{
//    			Log.i(TAG, header.getName()+"="+header.getValue());
    		}
    	}
    }

    public static int getConnTimeout() {
        return connTimeout;
    }

    public static void setConnTimeout(int timeout) {
        connTimeout = timeout;
    }
    
    public static int getRecvTimeout() {
        return recvTimeout;
    }

    public static void setRecvTimeout(int timeout) {
        recvTimeout = timeout;
    }

    public static String getErrInfo() {
        return errInfo;
    }

    /**
     * 得到服务器响应码
     * @return
     */
	public static String getResponseCode() {
		return responseCode;
	}
	
	/**
     * 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
     */ 
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
        
        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            LogUtil.e("palmpay", "==************************通讯出错了*********************"+Integer.toString(executionCount));
            LogUtil.e(TAG, "exception.name="+exception.getClass().getName());
//            // 设置恢复策略，在发生异常时候将自动重试N次 
//            if (executionCount >= 3) { 
//                // 如果超过最大重试次数，那么就不要继续了 
//                return false; 
//            } 
           if(exception instanceof SSLException)
           {
        	   LogUtil.i(TAG, "SSLException，重试连接 ");
        	   return true;
           }
            if (exception instanceof NoHttpResponseException) { 
                // 如果服务器丢掉了连接，那么就重试 
            	LogUtil.i(TAG, "服务器丢掉了连接，重试连接 ");
                return true; 
            } 
//            if (exception instanceof SSLHandshakeException) { 
//                // 不要重试SSL握手异常 
//                return false; 
//            } 
//            HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST); 
//            boolean idempotent = (request instanceof HttpEntityEnclosingRequest); 
//            if (!idempotent) { 
//                // 如果请求被认为是幂等的，那么就重试 
//                return true; 
//            } 
            executeException(exception);
            return false; 
        }
    };

	public static void setHttpClient(HttpClient httpClient) {
		
	}
	
	public static void httpShutDown()
	{
		if(httpClient != null && httpClient.getConnectionManager() != null)
		{
			httpClient.getConnectionManager().closeExpiredConnections();
			
		}
	}
	
  public static void preConnect(String url){
        LogUtil.i(TAG, "preConnect:" + url);
        HttpPost request = new HttpPost(url);
        try {
            HttpContext localContent = new BasicHttpContext();
            // 创建POST请求
            // 发送请求 
            HttpClient client = getHttpClient();
            errInfo = "";   //发送以前清空错误信息
            responseCode = HttpCodeHelper.HTTP_TIME_OUT;
            LogUtil.i(TAG,"======start connect");
            HttpResponse response = client.execute(request,localContent);
            if(response != null )
            {
            	HttpEntity entity =  response.getEntity();
            	InputStream inputStream = entity.getContent();
            	if(inputStream != null )
            	{
            		inputStream.close();
            	}
            	LogUtil.i(TAG, "StatusCode="+response.getStatusLine().getStatusCode());
            }
            response = null;
        } catch (UnsupportedEncodingException e) {
        	request.abort();
        	e.printStackTrace();
        	LogUtil.i(TAG, "UnsupportedEncodingException="+e.getMessage());
            errInfo += e.getMessage();
        } catch (ClientProtocolException e) {
        	request.abort();
        	e.printStackTrace();
        	LogUtil.i(TAG, "ClientProtocolException="+e.getMessage());
            errInfo += e.getMessage();
        } catch (IOException e) {
        	request.abort();
        	e.printStackTrace();
        	LogUtil.i(TAG, "IOException="+e.getMessage());
            errInfo += e.getMessage();
            executeException(e);
        }catch (Exception e) {
        	request.abort();
        	e.printStackTrace();
        	LogUtil.i(TAG, "Exception="+e.getMessage());
            errInfo += e.getMessage();
            executeException(e);
        }
       
    }
	    
    
  public static void closeConnect(){
 	 HttpClient client = getHttpClient();
 	 client.getConnectionManager().shutdown();
 	 httpClient=null;
 }
    
    
    
}
