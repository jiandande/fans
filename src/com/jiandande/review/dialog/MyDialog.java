package com.jiandande.review.dialog;
//package com.epalmpay.realname.dialog;
///**
// * @author hj
// * @version 创建时间：2013-12-19 下午5:30:08
// * 说明：
// */
//import com.epalmpay.realname.R;
//import com.palmpay.realname.util.SystemUtil;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.TextView;
//
//public class MyDialog extends Dialog {
//	/** 不显示按钮 */
//	public static final int DIALOG_NO_BUTTON_VISIBLE = 0;
//	/** 显示一个确定和一个取消的按钮 */
//	public static final int DIALOG_OK_AND_NO_BUTTON_VISIBLE = 2;
//	/**显示一个确定按钮 */
//	public static final int DIALOG_OK_BUTTON_VISIBLE = 1;
//	private final static int UPDATE_BACK_KEY= 7;
//	private final static int UPDATE_BT_NO = 5;
//	private final static int UPDATE_BT_OK = 4;
//	private final static int UPDATE_BUTTON_VISIBLE = 6;
//	private final static int UPDATE_ICON = 2;
//	private final static int UPDATE_MSG = 3;
//	private final static int UPDATE_TITLE = 1;
//
//	private Button btNo;
//	private Button btOk;
//	private ImageView icon;
//	private int iconImg;
//	private LinearLayout layout;
//
//	private Context mContext;
//	private TextView msg;
//
//	private String msgText;
//
//	private OnClickListener noClick;
//
//	private String noText;
//
//	private OnClickListener okclick;
//
//	private String okText;
//
//	private TextView title;
//
//	private String titleText;
//	
//	private boolean backKey = true;
//
//	private int visibleFlag;
//
//	public MyDialog(Context context) {
//		super(context, R.style.Theme_CustomDialog);
//		mContext = context;
//	}
//
//	/***
//	 * 退出弹出框
//	 */
//	@Override
//	public void cancel() {
//		try {
//			//if(isShowing())
//				super.cancel();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	/***
//	 * 加载view
//	 */
//	private void initView() {
//		this.setCancelable(backKey);
//		layout = (LinearLayout) this.findViewById(R.id.dialog_layout);
////		layout.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.show_animation));
//		icon = (ImageView) this.findViewById(R.id.dialog_ico);
//		title = (TextView) this.findViewById(R.id.dialog_title);
//		msg = (TextView) this.findViewById(R.id.dialog_msg);
//		btOk = (Button) this.findViewById(R.id.dialog_bt_ok);
//		btOk.setBackgroundResource(R.drawable.long_btn_xml);
//		btOk.setOnClickListener(okclick);
//		btNo = (Button) this.findViewById(R.id.dialog_bt_no);
//		btNo.setBackgroundResource(R.drawable.long_btn_xml);
//		btNo.setOnClickListener(noClick);
//		btNo.setVisibility(View.GONE);
//		btOk.setBackgroundResource(R.drawable.long_btn_xml);
//	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.my_dialog);
//		initView();
//	}
//	
//	/***
//	 * 设置按钮显示数量
//	 * 
//	 * @param buttonFlag
//	 */
//	private void setButtonVisible() {
//		int ps =  SystemUtil.dipToPx(mContext, 10);
//		switch (visibleFlag) {
//		case DIALOG_NO_BUTTON_VISIBLE:
//			btOk.setVisibility(View.GONE);
//			btNo.setVisibility(View.GONE);
//			break;
//		case DIALOG_OK_BUTTON_VISIBLE:
//			btNo.setVisibility(View.GONE);
//			btOk.setVisibility(View.VISIBLE);
//			LinearLayout.LayoutParams btOkLin_ = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 0.5f);
//			btOkLin_.setMargins(ps, ps, ps, ps);
//			btOk.setLayoutParams(btOkLin_);
//			btOk.setBackgroundResource(R.drawable.long_btn_xml);
//			break;
//		case DIALOG_OK_AND_NO_BUTTON_VISIBLE:
//			btNo.setVisibility(View.VISIBLE);
//			btOk.setVisibility(View.VISIBLE);
//			btOk.setBackgroundResource(R.drawable.long_btn_xml);
//			btNo.setBackgroundResource(R.drawable.long_btn_xml);
//			
//			LinearLayout.LayoutParams btOkLin = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 0.5f);
//			btOkLin.setMargins( ps, ps, SystemUtil.dipToPx(mContext, 3), ps);
//			btOk.setLayoutParams(btOkLin);
//			
//			LinearLayout.LayoutParams btNoLin = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 0.5f);
//			btNoLin.setMargins(SystemUtil.dipToPx(mContext, 3),ps,ps,ps);
//			btNo.setLayoutParams(btNoLin);
//			break;
//		}
//	}
//
//	public MyDialog setButtonVisible(int visibleFlag) {
//		this.visibleFlag = visibleFlag;
//		handler.sendEmptyMessage(UPDATE_BUTTON_VISIBLE);
//		return this;
//	}
//
//	
//	/**
//	 * 返回键设置
//	 */
//	public MyDialog setYishuaCancelable(boolean flag) {
//		backKey = flag;
//		handler.sendEmptyMessage(UPDATE_BACK_KEY);
//		return this;
//	}
//	
//
//	public MyDialog setIcon(int iconImg) {
//		this.iconImg = iconImg;
//		handler.sendEmptyMessage(UPDATE_ICON);
//		return this;
//	}
//
//	public MyDialog setMessage(CharSequence msgText) {
//		this.msgText = msgText.toString();
//		handler.sendEmptyMessage(UPDATE_MSG);
//		return this;
//	}
//
//	public MyDialog setMessage(int msgText) {
//		this.msgText = mContext.getString(msgText);
//		handler.sendEmptyMessage(UPDATE_MSG);
//		return this;
//	}
//	public MyDialog setNoButtonText(String noText) {
//		this.noText = noText;
//		handler.sendEmptyMessage(UPDATE_BT_NO);
//		return this;
//	}
//	/***
//	 * 监听返回键
//	 * 
//	 * @param click
//	 * @return
//	 */
//	public MyDialog setNoClick(OnClickListener listener) {
//		if(this.noClick!=null)
//		{
//			this.noClick = null;
//		}
//		this.noClick = listener;
//		return this;
//	}
//	public MyDialog setOkButtonText(String okText) {
//		this.okText = okText;
//		handler.sendEmptyMessage(UPDATE_BT_OK);
//		return this;
//	}
//	/***
//	 * 监听确定键
//	 * 
//	 * @param click
//	 * @return
//	 */
//	public MyDialog setOkClick(OnClickListener listener) {
//		if(this.okclick!=null)
//		{
//			this.okclick = null;
//		}
//		this.okclick = listener;
//		return this;
//	}
//	public MyDialog setTit(CharSequence textTitle) {
//		this.titleText = textTitle.toString();
//		handler.sendEmptyMessage(UPDATE_TITLE);
//		return this;
//	}
//	public MyDialog setTit(int titleText) {
//		this.titleText = mContext.getString(titleText);
//		handler.sendEmptyMessage(UPDATE_TITLE);
//		return this;
//	}
//	
//	@Override
//	public void show() {
//		if (this.isShowing()) {
//			cancel();
//		}
//		try {
//			super.show();
////			layout.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_dialog_show));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void show(String msg) {
//		msgText = msg;
//		handler.sendEmptyMessage(UPDATE_MSG);
//		show();
//	}
//	
//	private Handler handler = new Handler() {
//
//		@Override
//		public void handleMessage(Message message) {
//			switch (message.what) {
//			case UPDATE_TITLE:
//				title.setText(titleText);
//				break;
//			case UPDATE_ICON:
//				icon.setImageResource(iconImg);
//				break;
//			case UPDATE_MSG:
//				msg.setText(msgText);
//				break;
//			case UPDATE_BT_OK:
//				btOk.setText(okText);
//				break;
//			case UPDATE_BT_NO:
//				btNo.setText(noText);
//				break;
//			case UPDATE_BUTTON_VISIBLE:
//				setButtonVisible();
//				break;
//			case UPDATE_BACK_KEY:
//				setCancelable(backKey);
//				break;
//			}
//			super.handleMessage(message);
//		}
//	};
//
//}
//
