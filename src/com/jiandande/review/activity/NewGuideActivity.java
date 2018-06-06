package com.jiandande.review.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jiandande.money.R;



public class NewGuideActivity extends AbstractActivity implements OnClickListener{
	
	
	protected void addEventLister(){
	
		btnBack.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 if(v.getId()==R.id.back){
			 super.onBackPressed();
			 finish();
		 }
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helpdes);
		initView();
		addEventLister();
	}
}
