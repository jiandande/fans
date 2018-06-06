package com.jiandande.review.activity;

import com.jiandande.money.R;
import com.jiandande.review.adater.MoreAdapter;
import com.jiandande.review.dialog.CheckVersionThread;
import com.jiandande.review.dialog.YishuaDialog;
import com.jiandande.review.util.LogUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * 更多
 * 
 * @author smz
 * 
 */
public class MoreActivity extends AbstractActivity implements
		OnItemClickListener,OnClickListener {

	private ListView listView;
	private String[] more;
	private CheckVersionThread checkVersionThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_layout);
		initView();
		addEventLister();
		checkVersionThread = new CheckVersionThread(this, dialog);
		checkVersionThread.setShowLoadDialog(true);
	}

	@Override
	protected void initView() {
		super.initView();
		more = getResources().getStringArray(R.array.other_more);
		listView = (ListView) findViewById(R.id.list_view);
		MoreAdapter adapter = new MoreAdapter(this, more);
		listView.setAdapter(adapter);
	}

	@Override
	protected void addEventLister() {
		super.addEventLister();
		listView.setOnItemClickListener(this);
		btnBack.setOnClickListener(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_UPDATE_TEXT:
				text.setTextSize(16.0f);
				text.setTextColor(getResources().getColor(R.color.black));
				break;
			case MSG_UPDATE_VERSION:
				LogUtil.i(TAG, "请求版本更新");
				checkVersionThread.run();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	private TextView text;


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		text = (TextView) view.findViewById(R.id.item_id);
		text.setTextSize(20.0f);
		text.setTextColor(getResources().getColor(R.color.red));
		handler.sendEmptyMessageDelayed(MSG_UPDATE_TEXT, 100);
		
		LogUtil.i(TAG, "listview onclick");
		switch (position) {
		
		case 2: // 版本更新
			if(checkNet())
			{
				handler.sendEmptyMessage(MSG_UPDATE_VERSION);
			}
			break;
		case 3:  //关于
			
			break;
		case 4: // 退出
			dialog.getAlter("确认退出应用")
			.setButtonVisible(YishuaDialog.DIALOG_OK_AND_NO_BUTTON_VISIBLE)
			.setOkButtonText(getString(R.string.ok
					))
			.setOkClick( new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancelYiShuaDialog();
					app.exitAppaction();
					System.exit(0);
				}
			}).setNoButtonText(getString(R.string.cancel))
			.setNoClick(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancelYiShuaDialog();
				}
			}).show();
			break;
		}
	}
  @Override
	public void onClick(View v) {
	  switch (v.getId()) {
		case R.id.btn_back:
		onBackPressed();
	  }
	}
  
  @Override
 public void onBackPressed() {
	super.onBackPressed();
	finish();
 }
}
