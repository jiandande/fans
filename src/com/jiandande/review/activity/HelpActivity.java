package com.jiandande.review.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jiandande.money.R;


public class HelpActivity extends AbstractActivity implements OnClickListener{

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helpdes);
		initView();
		addEventLister();
	}
}

