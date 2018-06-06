package com.jiandande.review.view;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class RedButton extends Button {

    public RedButton(Context context) {
        super(context);
        init(context);
    }

    public RedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RedButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
    	
    	setTypeface(Typeface.DEFAULT_BOLD);
    }
    
}
