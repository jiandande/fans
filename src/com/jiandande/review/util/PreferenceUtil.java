package com.jiandande.review.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.jiandande.review.util.Constants.ParamName;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {


	public static void saveStrValue(Context context, String name,
			String value) {
   
		SharedPreferences preferences=context.getSharedPreferences(Constants.SHARENAME,Context.MODE_WORLD_WRITEABLE );
		preferences.edit().putString(name, value).commit();
	}

	public static String getStrValue(Context context,String key,String defValue) {
	
		SharedPreferences preferences=context.getSharedPreferences(Constants.SHARENAME,Context.MODE_WORLD_WRITEABLE );
		return preferences.getString(key, defValue);
	
   
	}

}
