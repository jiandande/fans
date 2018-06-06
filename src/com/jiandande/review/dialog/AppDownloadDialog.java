package com.jiandande.review.dialog;



import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import com.jiandande.money.R;
import com.jiandande.review.transfer.NetworkUtil;
import com.jiandande.review.util.FileUtil;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.Constants.ParamName;

public class AppDownloadDialog extends Dialog implements android.view.View.OnClickListener{
	public static final int DOWNLOAD_PREPARE = 0;
	public static final int DOWNLOAD_WORK = 1;
	public static final int DOWNLOAD_OK = 2;
	public static final int DOWNLOAD_ERROR =3;
	public static final int NOT_NETWORK = 4;
	private static final String TAG = "DownloadDialog";
	private Activity mContext;
	private String path;
	private String tempPath;
	private String fileName;
	private int versionCode;
	private Button bt;
	private ProgressBar pb;
	
	private TextView tv;
	private TextView tvtip;
	private TextView downloadTv ;
	private boolean isMastDoanload = false;
	private boolean isHaveBackDownload = false;
	private String fileSizeText = null;
	private NotificationManager nm ;
	private boolean isSuccess = true;
	
	
	/**
	 * 需要下载的文件
	 */
	private String url ;
	private String packagename ;
	private String pname ;
	public AppDownloadDialog(Activity context,String url,String packagename,String pname,int versionCode,boolean isMastdoanload) {
		
		super(context,R.style.Theme_CustomDialog);
		this.isMastDoanload = isMastdoanload;
		mContext = context;
		this.url = url;
		this.packagename = packagename;
		this.versionCode=versionCode;
		this.pname=pname;
		getPath();
		nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		setCancelable(false);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_layuot);
		init();
	}
	@Override
	public void show() {
		super.show();
		downloadSize = 0;
		fileSize = 0;
		if(pb != null )
			pb.setProgress(0);
		if(tv != null )
			tv.setText(""+0+"%");
		toDoanload();
		listConn();
	}
	@Override
	public void cancel() {
		try {
			
			if(thread != null )
			{
				thread.interrupt();
			}
			thread = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(listNetworkTimer != null )
		{
			listNetworkTimer.cancel();
			listNetworkTimer.purge();
			listNetworkTimer = null;
		}
		super.cancel();
	}
	
	private void init() {
		bt = (Button) this.findViewById(R.id.down_bt);
		bt.setOnClickListener(this);
		tv = (TextView) this.findViewById(R.id.down_tv);
		tvtip = (TextView) this.findViewById(R.id.down_pname);
		pb = (ProgressBar) this.findViewById(R.id.down_pb);
		downloadTv = (TextView) this.findViewById(R.id.download_tv);
		if(isMastDoanload)
		{
			bt.setVisibility(View.GONE);
		}else{
	
			bt.setVisibility(View.GONE);
			bt.setText(mContext.getString(R.string.after_down));
		}
		tvtip.setText("恭喜您,正在下载"+pname+",请稍候...");
	}
	
	private Timer listNetworkTimer = null;
	private void listConn()
	{
		if(listNetworkTimer != null )
		{
			listNetworkTimer.cancel();
			listNetworkTimer.purge();
			listNetworkTimer = null;
		}
		listNetworkTimer = new Timer();
		listNetworkTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if(NetworkUtil.getNetworkType(mContext) == NetworkUtil.NO_NET_CONNECT)
				{
					sendMessage(NOT_NETWORK);
					cancel();
				}
			}
		},0,1000);
	}


	/**
	 * 文件下载
	 */
	private void downloadFile()
	{
		isSuccess = true;
		FileOutputStream fos  = null;
		InputStream is = null;
		try {
			URL u = new URL(url);
			URLConnection conn = u.openConnection();
			conn.connect();
			is = conn.getInputStream();
			conn.setConnectTimeout(10*1000);
			conn.setReadTimeout(20*1000);
			fileSize = conn.getContentLength();
			if(fileSize<1||is==null)
			{
				sendMessage(DOWNLOAD_ERROR);
			}else{
				sendMessage(DOWNLOAD_PREPARE);
				File downFile = new File(tempPath);
				if(downFile.exists())
				{
					downFile.delete();
				}
				fos = new FileOutputStream(downFile);
				byte[] bytes = new byte[1024*100];
				int len = -1;
				while((len = is.read(bytes))!=-1)
				{
					fos.write(bytes, 0, len);
					fos.flush();
					downloadSize+=len;
					sendMessage(DOWNLOAD_WORK);	
				}
				if(isSuccess)
				{
					copyFile();
					sendMessage(DOWNLOAD_OK);
				}
			}
		} catch (Exception e) {
			sendMessage(DOWNLOAD_ERROR);
			e.printStackTrace();
		} finally{
			try {
				is.close();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void copyFile()
	{
		File tempFile = new File(tempPath);
		if(!tempFile.exists())
		{
			return;
		}
		File newFile = new File(path);
		if(!newFile.exists())
		{
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileInputStream tempInputStream = null;
		FileOutputStream newOutPutStream = null;
		try {
			tempInputStream = new FileInputStream(tempFile);
			newOutPutStream = new FileOutputStream(newFile);
			byte[] buff = new byte[1024*200];
			int len = -1;
			while((len = tempInputStream.read(buff))!= -1)
			{
				newOutPutStream.write(buff, 0, len);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(newOutPutStream != null )
			{
				try {
					newOutPutStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(tempInputStream != null )
			{
				try {
					tempInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		tempFile.delete();
	}
	
	/**
	 * 文件一共的大小
	 */
	int fileSize = 0;
	/**
	 * 已经下载的大小
	 */
	int downloadSize = 0;
	/**
	 * handler处理消息
	 */
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_PREPARE:
				Toast.makeText(mContext, mContext.getString(R.string.ready_download), Toast.LENGTH_SHORT).show();
				pb.setVisibility(ProgressBar.VISIBLE);
				pb.setMax(fileSize);
				fileSizeText = FileUtil.formatFileSize(fileSize);
				break;
			case DOWNLOAD_WORK:
				pb.setProgress(downloadSize);
				if(fileSize <1)
				{
					fileSize = 1;
				}
			//	fileSize=Math.abs(fileSize);
			 int res = downloadSize*100/fileSize;//文件比较大的时候77%downloadsize 开始减少-77%?
					res=Math.abs(res);
		
			//	tv.setText(Integer.toString(res));
				tv.setText(""+res+"%");
				downloadTv.setText(FileUtil.formatFileSize(downloadSize)+"/"+fileSizeText);
				break;
			case DOWNLOAD_OK:
				File downFile = new File(path);
				if(!downFile.exists())
				{
					Toast.makeText(mContext, mContext.getString(R.string.download_error), Toast.LENGTH_SHORT).show();
					cancel();
					return;
				}
				showNotification();
				PreferenceUtil.saveStrValue(mContext,packagename,pname);
				downloadSize = 0;
				fileSize = 0;
				Toast.makeText(mContext, mContext.getString(R.string.down_finish), Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Intent.ACTION_VIEW);  
				intent.setDataAndType(Uri.parse("file://" + path),"application/vnd.android.package-archive"); 
				mContext.startActivity(intent);
//				nm.cancel(1);  //关闭通知
	//			System.exit(0);
				cancel();
			
		
				break;
			case DOWNLOAD_ERROR:
				isSuccess = false;
				Toast.makeText(mContext, mContext.getString(R.string.download_error), Toast.LENGTH_SHORT).show();
				downloadSize = 0;
				fileSize = 0;
				File file = new File(tempPath);
				file.delete();
				break;
			case NOT_NETWORK:
				Toast.makeText(mContext,  mContext.getString(R.string.net_error), Toast.LENGTH_SHORT).show();
				isSuccess = false;
//				downloadSize = 0;
//				fileSize = 0;
//				File file1 = new File(tempPath);
//				file1.delete();
				break;
			}
			super.handleMessage(msg);
		}
	};
	/**
	 * 得到文件的保存路径
	 * @return
	 * @throws IOException
	 */
	private void getPath() 
	{
		fileName = url.substring(url.lastIndexOf("/")+1);
		try {
			String dir=FileUtil.createDir(mContext, null)+File.separator;
			path = dir+versionCode+fileName;
			tempPath = path+".temp";
			FileUtil.clearFiles(dir);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 给hand发送消息
	 * @param what
	 */
	private void sendMessage(int what)
	{
		Message m = new Message();
		m.what = what;
		handler.sendMessage(m);
		if(onDownLoadListener != null )
		{
			onDownLoadListener.onDownLoadStatus(what);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.down_bt:
			if(!isHaveBackDownload)
			{
				mContext.moveTaskToBack(true);
			}
		
			break;

		default:
			break;
		}
	}
	private Thread thread;
	
	
	
	public void setHaveBackDownload(boolean isHaveBackDownload) {
		this.isHaveBackDownload = isHaveBackDownload;
	}
	private void toDoanload()
	{
		File file = new File(path);
		if(file.exists())
		{
			sendMessage(DOWNLOAD_OK);
		}else{
			if(thread == null )
			{
				 thread = new Thread(new Runnable() {
						@Override
						public void run() {
							downloadFile();				
						}
					});
			}
			thread.start();
		}
	}
	
	public static interface OnDownLoadListener{
		void onDownLoadStatus(int downloadStatus);
	}
	private OnDownLoadListener onDownLoadListener;
	
	public void setOnDownLoadListener(OnDownLoadListener onDownLoadListener) {
		this.onDownLoadListener = onDownLoadListener;
	}
	private void showNotification() {
		
		Notification notification = new Notification();
		notification.defaults |= Notification.DEFAULT_SOUND;   //声音通知
		notification.defaults |= Notification.DEFAULT_VIBRATE; //颤动通知
		notification.icon = R.drawable.ic_launcher;
		notification.tickerText = mContext.getString(R.string.app_name)+mContext.getString(R.string.down_finish);
		notification.flags = Notification.FLAG_AUTO_CANCEL|Notification.FLAG_NO_CLEAR;  //不可清除的通知
		Intent intent = new Intent(Intent.ACTION_VIEW);  
		intent.setDataAndType(Uri.parse("file://" + path),"application/vnd.android.package-archive");  
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
				intent, 0);
		String fixMessage= mContext.getString(R.string.app_name)+mContext.getString(R.string.fix_alert);
		notification.setLatestEventInfo(mContext, mContext.getString(R.string.down_alert), fixMessage, pendingIntent);
		nm.notify(R.string.app_name, notification);
	}
	
}
