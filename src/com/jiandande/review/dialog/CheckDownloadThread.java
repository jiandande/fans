package com.jiandande.review.dialog;



import java.io.File;







import com.jiandande.money.R;
import com.jiandande.review.activity.OwnBusinessTaskListActivity;
import com.jiandande.review.activity.WelComeActivity;
import com.jiandande.review.bean.VersionModel;
import com.jiandande.review.dialog.AppDownloadDialog.OnDownLoadListener;
import com.jiandande.review.transcation.CheckVersionAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.FileUtil;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.SystemUtil;

import android.app.Activity;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;



public class CheckDownloadThread{

	private String downloadUrl;
	private String packagename2;
	private String pname;
	private String updateMsg;
	private CheckVersionAction versionAction = null;
	private DialogFactory dialog;
	private Activity mContext;
	
	private boolean isShowLoadDialog = false;
	private LoadingDialog loadingDialog;
	
	
	public CheckDownloadThread(OwnBusinessTaskListActivity ownBusinessTaskListActivity,String producturl,String packagename, String downpname,DialogFactory dialog) {
		mContext = ownBusinessTaskListActivity;
		downloadUrl =producturl;
		pname  = downpname;
		packagename2= packagename;
		this.dialog =dialog;
		versionAction = new CheckVersionAction();
		this.isShowLoadDialog = false;
		loadingDialog = new LoadingDialog(mContext);
		
		versionAction.setTransferPayResponse(tPayResponse);
	}

	public void setShowLoadDialog(boolean isShowLoadDialog) {
		this.isShowLoadDialog = isShowLoadDialog;
	}
	
	public void run() {
		if(!FileUtil.checkSDCard())  //内存卡如果不存在，就不检验版本
		{
			if(isShowLoadDialog)
			{
				Toast.makeText(mContext, mContext.getString(R.string.add_card),Toast.LENGTH_LONG).show();
			}else{
				//toIndex();
			}
			return;
		}
		if(isShowLoadDialog)
		{
			loadingDialog.show(mContext.getString(R.string.checking_version));
		}
	//	Toast.makeText(mContext, " versionAction.transStart()", Toast.LENGTH_LONG).show();
		versionAction.transStart();
	}
	
	
	TransferPayResponse tPayResponse=new TransferPayResponse() {
		
		@Override
		public void transFailed(String httpCode, String errorInfo) {
			compareVersion(CheckVersionAction.NO_NEW_VERSION);
		}
		
		@Override
		public void transComplete() {
			LogUtil.i("version", "transComplete() ");
			if(HttpCodeHelper.RESPONSE_SUCCESS.equals(versionAction.respondData.respCode)){
				LogUtil.i("version", "RESPONSE_SUCCESS ");
				VersionModel version=versionAction.respondData.model;
				version.appName=pname;
				loadingDialog.cancel();
			//	downloadUrl = version.path;
				updateMsg="222";//version.msg;
				int code=SystemUtil.getversonCode(mContext);
				LogUtil.i("version", "	int code=SystemUtil.getversonCode(mContext); "+version.versionCode+"code:"+code);
				if(version.versionCode==code){
					compareVersion(CheckVersionAction.NO_NEW_VERSION);
				}else{
				
					LogUtil.i("version", "	needUpdate"+Integer.toString(version.needUpdate));
					if(version.needUpdate==1){
						LogUtil.i("version", "	CheckVersionAction..MAST_UPDATE_VERSION ");
						compareVersion(CheckVersionAction.MAST_UPDATE_VERSION);
					}else{
						compareVersion(CheckVersionAction.SELECT_VERSION_UPDATE);
					}
				}
				
			}else{
				compareVersion(CheckVersionAction.NO_NEW_VERSION);
			}
	
		}
	};
	

	
	private void compareVersion(int type)
	{
	
			toDownload(true);
	
	}
	
	
	
	public void toIndex()
	{
		if(mContext instanceof WelComeActivity ){
			WelComeActivity activity=(WelComeActivity)mContext;
			activity.checkFinishVersion();
		}
		//
	}
	private AppDownloadDialog downloadDialog ;
	public void toDownload(boolean mastDownload)
	{
		if(downloadDialog==null)
		{
			downloadDialog = new AppDownloadDialog(mContext,downloadUrl,packagename2,pname,versionAction.respondData.model.versionCode,mastDownload);//versionAction.respondData.model.versionCode
			downloadDialog.setHaveBackDownload(isShowLoadDialog);
			downloadDialog.setOnDownLoadListener(onDownLoadListener);
		}
		try {
			downloadDialog.show();
		} catch (Exception e) {
			downloadDialog.cancel();
			e.printStackTrace();
		}
	}
	
	OnDownLoadListener onDownLoadListener = new OnDownLoadListener() {
		@Override
		public void onDownLoadStatus(int downloadStatus) {
			switch (downloadStatus) {
			case DownloadDialog.DOWNLOAD_ERROR:
				downloadError(mContext.getString(R.string.download_error));
				break;
			case DownloadDialog.NOT_NETWORK:
				downloadError(mContext.getString(R.string.net_error));
				break;
			default:
				break;
			}
		}
	};
	
	private void downloadError(String msg)
	{
		
		if(downloadDialog != null )
		{
			downloadDialog.cancel();
		}
	//	toIndex();
	}
	
	
}
