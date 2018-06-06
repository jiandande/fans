package com.jiandande.review.dialog;



import java.io.File;



import com.jiandande.money.R;
import com.jiandande.review.activity.WelComeActivity;
import com.jiandande.review.bean.VersionModel;
import com.jiandande.review.dialog.DownloadDialog.OnDownLoadListener;
import com.jiandande.review.transcation.CheckVersionAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.FileUtil;
import com.jiandande.review.util.LogUtil;
import com.jiandande.review.util.SystemUtil;

import android.app.Activity;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;



public class CheckVersionThread{

	private String downloadUrl;
	private String updateMsg;
	private CheckVersionAction versionAction = null;
	private DialogFactory dialog;
	private Activity mContext;
	
	private boolean isShowLoadDialog = false;
	private LoadingDialog loadingDialog;
	
	
	public CheckVersionThread(Activity context,DialogFactory dialog) {
		mContext = context;
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
				toIndex();
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
				loadingDialog.cancel();
				downloadUrl = version.path;
				updateMsg=version.msg;
				int code=SystemUtil.getversonCode(mContext);
				LogUtil.i("version", "	int code=SystemUtil.getversonCode(mContext); "+version.versionCode+"code:"+code);
				if(version.versionCode==code){
					LogUtil.i("version", "	CheckVersionAction.NO_NEW_VERSION ");
					compareVersion(CheckVersionAction.NO_NEW_VERSION);
				}else{
				
					/*String[] unUpdateCode=version.unUpdateCode.split(",");
					LogUtil.i("version", "	unUpdateCode");
					for(String temp:unUpdateCode){
						if(code==Integer.parseInt(temp)){
							LogUtil.i("version", "	CheckVersionAction.NO_NEW_VERSION 3");
							compareVersion(CheckVersionAction.NO_NEW_VERSION);
							return;
						}
					}*/
					//Toast.makeText(mContext, "version.needUpdate==1",Toast.LENGTH_LONG).show();
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
			LogUtil.i("version", "transComplete()222 ");
		}
	};
	
	/**
	 * 当发现已经是最新版本  删除之前的安装包
	 */
	private void isNewVersion()
	{
		String filePath = "";
		if(FileUtil.checkSDCard())
		{
			filePath = Environment.getExternalStorageDirectory()+File.separator+"shouzhangbao"+File.separator+"downloads";
		}else{
			filePath = mContext.getCacheDir().getAbsolutePath()+File.separator+"shouzhangbao"+File.separator+"downloads";
		}
		
		File file = new File(filePath);
		if(file.exists() && file.canWrite() && file.isDirectory())
		{
			LogUtil.i("file", "删除已有文件");
			File[] tempFiles = file.listFiles();
			if(tempFiles.length > 0)
			{
				for (int i = 0; i < tempFiles.length; i++) {
					File tempFile = tempFiles[i];
					if(tempFile.canRead() && tempFile.canWrite())
					{
						LogUtil.i("file", "删除文件:"+tempFile.getName());
						tempFile.delete();
					}
				}
			}
		}
	}
	
	private void compareVersion(int type)
	{
		switch (type) {
		case CheckVersionAction.NO_NEW_VERSION:
			if(isShowLoadDialog)
			{
				if(dialog != null )
				{
					dialog = null;
					dialog = new DialogFactory(mContext);
				}
				dialog.show(mContext.getString(R.string.isnew_version));
			}else{
				toIndex();
			}
			isNewVersion();
			LogUtil.e("version", "当前已经是最新的版本了");
			break;
		case CheckVersionAction.SELECT_VERSION_UPDATE:
			if(dialog != null )
			{
				dialog = null;
				dialog = new DialogFactory(mContext);
			}
			dialog
			.getAlter(mContext.getString(R.string.update_title),updateMsg)
			.setOkButtonText(mContext.getString(R.string.update))
			.setYishuaCancelable(false)
			.setButtonVisible(YishuaDialog.DIALOG_OK_AND_NO_BUTTON_VISIBLE)
			.setOkClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancelYiShuaDialog();
					toDownload(false);
					LogUtil.i("version", "正在更新版本");
				}
			} )
			.setNoButtonText(mContext.getString(R.string.cancel))
			.setNoClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!isShowLoadDialog)
					{
						toIndex();
					}
					dialog.cancelYiShuaDialog();
				}
			} )
			.show();
			break;
		case CheckVersionAction.MAST_UPDATE_VERSION:
			toDownload(true);
			break;
		}
	}
	
	
	
	public void toIndex()
	{
		if(mContext instanceof WelComeActivity ){
			WelComeActivity activity=(WelComeActivity)mContext;
			activity.checkFinishVersion();
		}
		//
	}
	private DownloadDialog downloadDialog ;
	public void toDownload(boolean mastDownload)
	{
		if(downloadDialog==null)
		{
			downloadDialog = new DownloadDialog(mContext,downloadUrl,versionAction.respondData.model.versionCode,mastDownload);
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
		toIndex();
	}
	
	
}
