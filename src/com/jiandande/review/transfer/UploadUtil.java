package com.jiandande.review.transfer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.jiandande.money.R;
import com.jiandande.review.util.FileUtil;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.StringUtil;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

/**
 * 
 * 上传工具类
 * @author spring sky<br>
 * 支持上传文件和参数
 */
public class UploadUtil {
	private static UploadUtil uploadUtil;
	private static final String BOUNDARY =  UUID.randomUUID().toString(); // 边界标识 随机生成
	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
	private UploadUtil() {

	}

	/**
	 * 单例模式获取上传工具类
	 * @return
	 */
	public static UploadUtil getInstance() {
		if (null == uploadUtil) {
			uploadUtil = new UploadUtil();
		}
		return uploadUtil;
	}

	private static final String TAG = "UploadUtil";
	private int readTimeOut = 10 * 1000; // 读取超时
	private int connectTimeout = 10 * 1000; // 超时时间
	/***
	 * 请求使用多长时间
	 */
	private static int requestTime = 0;
	private static final String CHARSET = "gb2312"; // 设置编码
//	private static final String CHARSET = "utf-8"; // 设置编码

	/***
	 * 上传成功
	 */
	public static final int UPLOAD_SUCCESS_CODE = 1;
	/**
	 * 文件不存在
	 */
	public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
	/**
	 * 服务器出错
	 */
	public static final int UPLOAD_SERVER_ERROR_CODE = 3;
	protected static final int WHAT_TO_UPLOAD = 1;
	protected static final int WHAT_UPLOAD_DONE = 2;
	
	/**
	 * android上传文件到服务器
	 * 
	 * @param filePath
	 *            需要上传的文件的路径
	 * @param fileKey
	 *            在网页上<input type=file name=xxx/> xxx就是这里的fileKey
	 * @param RequestURL
	 *            请求的URL
	 */
	public void uploadFile(final String filePath, final String fileKey,  final String RequestURL,
			final Map<String, String> param) {
		if (filePath == null) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,R.string.file_not_exsit,"");
			return;
		}
		try {
		new Thread(new Runnable() {  //开启线程上传文件
			@Override
			public void run() {
				//拍照之前检查照片大小
				String compressPath=FileUtil.decodePicFromPath(filePath);
				if(StringUtil.checkNotNull(compressPath)){
					File file = new File(compressPath);
					toUploadFile(file, fileKey, RequestURL, param);
				}else{
					sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,R.string.compress_error,"");
					return;
				}
			}
		}).start();		
		} catch (Exception e) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,R.string.file_not_exsit,"");
			e.printStackTrace();
			return;
		}
	}

	/**
	 * android上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * @param fileKey
	 *            在网页上<input type=file name=xxx/> xxx就是这里的fileKey
	 * @param RequestURL
	 *            请求的URL
	 */
	public void uploadFile(final File file, final String fileKey,
			final String RequestURL, final Map<String, String> param) {
		if (file == null || (!file.exists())) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,R.string.file_not_exsit,"");
			return;
		}
		new Thread(new Runnable() {  //开启线程上传文件
			@Override
			public void run() {
				toUploadFile(file, fileKey, RequestURL, param);
			}
		}).start();
		
	}
	
	/***
	 * 打印header中的key和value
	 * @param conn
	 */
	public void readHeader(HttpURLConnection conn)
	{
		if(conn != null )
		{
			String key = "";
			String values = "";
			String value = "";
			List<String> tempList = null;
			Map<String, List<String>> properties = conn.getRequestProperties();
			if(properties != null && properties.size() > 0)
			{
				Iterator<String> it = properties.keySet().iterator();
				while(it.hasNext())
				{
					key = it.next();
					tempList = properties.get(key);
					if(tempList != null && tempList.size() > 0)
					{
						values = "";
						for (int i = 0; i < tempList.size(); i++) {
							value = tempList.get(i)+"\n";
							values += value;
						}
					}
					LogUtil.i(TAG, key+"="+values);
				}
			}
		}
	}

	private void toUploadFile(File file, String fileKey, String RequestURL,
			Map<String, String> param) {
		String result = null;
		requestTime= 0;
		
		long requestTime = System.currentTimeMillis();
		long responseTime = 0;
		DataOutputStream dos = null;
		InputStream input=null;
		InputStream fis=null;
		HttpURLConnection conn=null;
		try {
			URL url = new URL(RequestURL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(readTimeOut);
			conn.setConnectTimeout(connectTimeout);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
			
//			readHeader(conn);
//			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			/**
			 * 当文件不为空，把文件包装并且上传
			 */
			dos = new DataOutputStream(conn.getOutputStream());
			StringBuffer sb = null;
			String params = "";
			LogUtil.i(TAG, "准备请求服务器上传");
			LogUtil.i(TAG, "准备请求服务器上传 文件大小"+file.length());
			/***
			 * 以下是用于上传参数
			 */
			if (param != null && param.size() > 0) {
				Iterator<String> it = param.keySet().iterator();
				while (it.hasNext()) {
					sb = null;
					sb = new StringBuffer();
					String key = it.next();
					String value = param.get(key);
					sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
					sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
					sb.append(value).append(LINE_END);
					params = sb.toString();
					LogUtil.i(TAG, params);
//					Log.i(TAG, key+"="+params+"##");
					dos.write(params.getBytes());
//					dos.flush();
				}
			}
			
			sb = null;
			params = null;
			sb = new StringBuffer();
			/**
			 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
			 * filename是文件的名字，包含后缀名的 比如:abc.png
			 */
			sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
			sb.append("Content-Disposition:form-data; name=\"" + fileKey
					+ "\"; filename=\"" + file.getName() + "\"" + LINE_END);
			sb.append("Content-Type:image/pjpeg" + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
			sb.append(LINE_END);
			params = sb.toString();
			sb = null;
			
			LogUtil.i(TAG,params);
//			Log.i(TAG, file.getName()+"=" + params+"##");
			dos.write(params.getBytes());
			/**上传文件*/
			fis = new FileInputStream(file);
//			if(onUploadProcessListener != null )onUploadProcessListener.initUpload((int)file.length());
			byte[] bytes = new byte[1024];
			int len = 0;
			//int curLen = 0;
			while ((len = fis.read(bytes)) != -1) {
				//curLen += len;
				dos.write(bytes, 0, len);
			}
//			onUploadProcessListener.onUploadProcess(curLen);
			LogUtil.i(TAG, "向服务器写入文件流");
			
			dos.write(LINE_END.getBytes());
			LogUtil.i(TAG, LINE_END);
			String endStr = PREFIX + BOUNDARY + PREFIX + LINE_END;
			LogUtil.i(TAG, endStr);
			byte[] end_data = endStr.getBytes();
			dos.write(end_data);
			dos.flush();
			
			LogUtil.i(TAG, "上传完成");
//			
//			dos.write(tempOutputStream.toByteArray());
			/**
			 * 获取响应码 200=成功 当响应成功，获取响应的流
			 */
			int res = conn.getResponseCode();
			responseTime = System.currentTimeMillis();
			requestTime = (int) ((responseTime-requestTime)/1000);
			LogUtil.e(TAG, "response code:" + res);
			if (res == 200) {
				LogUtil.e(TAG, "request success");
				input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				result = sb1.toString();
				if(HttpCodeHelper.RESPONSE_SUCCESS.equals(result))
				{
					sendMessage(UPLOAD_SUCCESS_CODE,0,"");
				}else{
					sendMessage(UPLOAD_SERVER_ERROR_CODE,-1,result+"");
				}
				LogUtil.e(TAG, "result : " + result);
				return;
			} else {
				LogUtil.e(TAG, "request error");
				sendMessage(UPLOAD_SERVER_ERROR_CODE,-1,res+"");
				return;
			}
		} catch (MalformedURLException e) {
			sendMessage(UPLOAD_SERVER_ERROR_CODE, -1, e.getMessage()+"");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			sendMessage(UPLOAD_SERVER_ERROR_CODE,-1,e.getMessage()+"");
			e.printStackTrace();
			return;
		}finally{
			if(dos!=null){
				try {
					dos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 if(fis!=null){
				 try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		}
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if(onUploadProcessListener != null )
				onUploadProcessListener.onUploadDone(msg.what,msg.arg1, msg.obj.toString());
			
		};
	};

	/**
	 * 发送上传结果
	 * @param responseCode
	 * @param responseMessage
	 */
	private void sendMessage(int responseCode,int mesId,String responseMessage)
	{
		Message message = Message.obtain();
		message.what = responseCode;
		message.arg1 = mesId;
		message.obj = responseMessage;
		handler.sendMessage(message);
	}
	
	
	/**
	 * android上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * @param fileKey
	 *            在网页上<input type=file name=xxx/> xxx就是这里的fileKey
	 * @param RequestURL
	 *            请求的URL
	 */
	public void uploadFile(final File[] file,
			final String RequestURL, final Map<String, String> param) {
		new Thread(new Runnable() {  //开启线程上传文件
			@Override
			public void run() {
				toUploadFile(file, RequestURL, param);
			}
		}).start();
	}
	
	private void toUploadFile(File[] files, String RequestURL,
			Map<String, String> param) {
		String result = null;
		requestTime= 0;
		
		long requestTime = System.currentTimeMillis();
		long responseTime = 0;
		DataOutputStream dos = null;
		InputStream input=null;
		InputStream fis=null;
		HttpURLConnection conn=null;
		try {
			URL url = new URL(RequestURL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(readTimeOut);
			conn.setConnectTimeout(connectTimeout);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
			
//			readHeader(conn);
//			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			/**
			 * 当文件不为空，把文件包装并且上传
			 */
			dos = new DataOutputStream(conn.getOutputStream());
			StringBuffer sb = null;
			String params = "";
			/***
			 * 以下是用于上传参数
			 */
			if (param != null && param.size() > 0) {
				Iterator<String> it = param.keySet().iterator();
				while (it.hasNext()) {
					sb = null;
					sb = new StringBuffer();
					String key = it.next();
					String value = param.get(key);
					sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
					sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
					sb.append(value).append(LINE_END);
					params = sb.toString();
					LogUtil.i(TAG, params);
					dos.write(params.getBytes());
				}
			}
			
			 params = null;
			 int fileSize=files.length;
			for(int i=0;i<fileSize;i++){
				File file =files[i];
				LogUtil.i(TAG, "准备请求服务器上传");
				LogUtil.i(TAG, "准备请求服务器上传 文件大小"+file.length());
				sb = new StringBuffer();
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
				 * filename是文件的名字，包含后缀名的 比如:abc.png
				 */
				sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
				sb.append("Content-Disposition:form-data; name=\"upload\"; filename=\"" + file.getName() + "\"" + LINE_END);
				sb.append("Content-Type:image/pjpeg" + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
				sb.append(LINE_END);
				params = sb.toString();
				sb = null;
				
				LogUtil.i(TAG,params);
				dos.write(params.getBytes());
				/**上传文件*/
				fis = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				//int curLen = 0;
				while ((len = fis.read(bytes)) != -1) {
					//curLen += len;
					dos.write(bytes, 0, len);
				}
				LogUtil.i(TAG, "向服务器写入文件流");
				
				dos.write(LINE_END.getBytes());
				LogUtil.i(TAG, LINE_END);
				String endStr = PREFIX + BOUNDARY + PREFIX + LINE_END;
				LogUtil.i(TAG, endStr);
				byte[] end_data = endStr.getBytes();
				dos.write(end_data);
			}
			dos.flush();
			
			LogUtil.i(TAG, "上传完成");
			/**
			 * 获取响应码 200=成功 当响应成功，获取响应的流
			 */
			int res = conn.getResponseCode();
			responseTime = System.currentTimeMillis();
			requestTime = (int) ((responseTime-requestTime)/1000);
			LogUtil.e(TAG, "response code:" + res);
			if (res == 200) {
				LogUtil.e(TAG, "request success");
				input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				result = sb1.toString();
				if(HttpCodeHelper.RESPONSE_SUCCESS.equals(result))
				{
					sendMessage(UPLOAD_SUCCESS_CODE,0,"");
				}else{
					sendMessage(UPLOAD_SERVER_ERROR_CODE,-1,result+"");
				}
				LogUtil.e(TAG, "result : " + result);
				return;
			} else {
				LogUtil.e(TAG, "request error");
				sendMessage(UPLOAD_SERVER_ERROR_CODE,-1,res+"");
				return;
			}
		} catch (MalformedURLException e) {
			sendMessage(UPLOAD_SERVER_ERROR_CODE, -1, e.getMessage()+"");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			sendMessage(UPLOAD_SERVER_ERROR_CODE,-1,e.getMessage()+"");
			e.printStackTrace();
			return;
		}finally{
			if(dos!=null){
				try {
					dos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 if(fis!=null){
				 try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		}
	}
	
	/**
	 * 下面是一个自定义的回调函数，用到回调上传文件是否完成
	 * 
	 * @author shimingzheng
	 * 
	 */
	public static interface OnUploadProcessListener {
		/**
		 * 上传响应
		 * @param responseCode
		 * @param message
		 */
		void onUploadDone(int responseCode, int msgid, String message);
		/**
		 * 上传中
		 * @param uploadSize
		 */
		void onUploadProcess(int uploadSize);
		/**
		 * 准备上传
		 * @param fileSize
		 */
		void initUpload(int fileSize);
	}
	private OnUploadProcessListener onUploadProcessListener;
	
	

	public void setOnUploadProcessListener(
			OnUploadProcessListener onUploadProcessListener) {
		this.onUploadProcessListener = onUploadProcessListener;
	}

	public int getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	/**
	 * 获取上传使用的时间
	 * @return
	 */
	public static int getRequestTime() {
		return requestTime;
	}
	
	public static interface uploadProcessListener{
		
	}
}
