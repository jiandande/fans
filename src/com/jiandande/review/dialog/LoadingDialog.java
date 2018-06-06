package com.jiandande.review.dialog;



import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import com.jiandande.money.R;
/***
 * 请求服务器加载数据的dialog
 * @author smz
 *
 */
public class LoadingDialog extends Dialog{

	private RelativeLayout layout;
	private TextView mTv;
	private Timer timer = null ;
	int count = 1;
	private Activity mContext;
	private String message ;
	public LoadingDialog(Activity context) {
		super(context, R.style.Theme_CustomDialog);
		mContext = context;
		message=context.getString(R.string.intering_writing);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_activity);
		setCancelable(false);
		mTv = (TextView) this.findViewById(R.id.my_dialog_textview);
		layout = (RelativeLayout) findViewById(R.id.load_dialog_layout);
		timer= new Timer();
	}
	/***
	 * 弹出消息
	 * @param msg
	 */
	public void show(String msg)
	{
		this.message = msg;
		if(!isShowing())
		{
			show();
		}
	}
	
	/**
	 * 设置弹出框消息
	 * 
	 */
	public void setMessage(String msg){
	    this.message = msg;
	}
	
	
	@Override
	public void show() {
//		if(isShowing())
//		{
//			cancel();
//		}
		try {
			super.show();
			layout.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_dialog_show));
		} catch (Exception e) {
			e.printStackTrace();
			return ;
		}
		if(timer==null)
		{
			timer = new Timer();
		}
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if(count>3)
				{
					count = 1;
				}
				handler.sendEmptyMessage(count);
				count++;

			}
		},0,1000);
	}

	@Override
	public void cancel() {
		if(timer!=null)
		{
			timer.cancel();
			timer.purge();
			timer=null;
		}
		try {
			super.cancel();
		} catch (Exception e) {
			message=mContext.getString(R.string.intering_writing);
			e.printStackTrace();
		}
		message=mContext.getString(R.string.intering_writing);
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			mTv.setText(null);
			switch (msg.what) {
			case 1:
				mTv.setText(message + ".    ");
				break;
			case 2:
				mTv.setText(message + ". .  ");
				break;
			case 3:
				mTv.setText(message + ". . .");
				break;
		}
			super.handleMessage(msg);
		}
	};
	

	/**
	 * 不处理返回键
	 * 
	 * @author wanggh
	 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)){
            return true;
        }
        return true;
    }

}
