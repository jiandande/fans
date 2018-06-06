package com.jiandande.review.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jiandande.money.R;
import com.jiandande.review.transcation.FeedBackAction;
import com.jiandande.review.transcation.AbstractTransaction.TransferPayResponse;
import com.jiandande.review.transfer.HttpCodeHelper;
import com.jiandande.review.util.CheckViewUtil;


public class FeedBackActivity extends AbstractActivity implements OnClickListener{

	EditText edFeedContent;
	
	Button btnConfirm;
	
	FeedBackAction feedBackAction;
	@Override
	public void onClick(View v) {
		 if(v.getId()==R.id.back){
			 super.onBackPressed();
			 finish();
		 }else if(v.getId()==R.id.confirm){
			 if(super.checkValue(true)){
				 if(CheckViewUtil.checkCardNotNull(edFeedContent, "反馈内容", dialog)){
					 feedBackAction.setContent(edFeedContent.getText().toString().trim());
				}else{
					 Toast.makeText(FeedBackActivity.this, "反馈内容 不能为空", Toast.LENGTH_LONG).show();
					return ;
				}
				 if (loadingDialog.isShowing()) {
						loadingDialog.setMessage("正在通讯中请稍候");
					} else {
						loadingDialog.show("正在通讯中请稍候");
					}				
				 feedBackAction.setTransferPayResponse(feedResponse);
				 feedBackAction.transStart();
			 }
		  }
		}
TransferPayResponse  feedResponse=new TransferPayResponse() {
		
		public void transComplete() {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			if (HttpCodeHelper.RESPONSE_SUCCESS
					.equals(feedBackAction.respondData.respCode)) {
				// 保存
				Toast.makeText(FeedBackActivity.this,
						"我们会尽快处理您的意见，祝您生活愉快", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(FeedBackActivity.this,
						feedBackAction.respondData.respCode+"##"+feedBackAction.respondData.respDesc, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void transFailed(String httpCode, String errorInfo) {
			if (loadingDialog!=null) {
				loadingDialog.cancel();
			}
			Toast.makeText(FeedBackActivity.this, errorInfo, Toast.LENGTH_LONG)
					.show();
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playchat);
		initView();
		addEventLister();
		feedBackAction=new FeedBackAction();
	}
	
	protected void addEventLister(){
		btnConfirm.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	
	protected void initView(){
		super.initView();
		btnConfirm=(Button)findViewById(R.id.confirm);
		edFeedContent=(EditText)findViewById(R.id.et);
	}
}
