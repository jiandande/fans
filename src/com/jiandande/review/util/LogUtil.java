package com.jiandande.review.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class LogUtil {
    
	public static String Tag = "phone";
	
    public static final boolean DEBUG_FLAG = false;
    private static final boolean DEBUG_SAVE_FILE = false;

    private static final String LOG_PATH = "/mnt/sdcard/mylog/jiandande";
	private static FileOutputStream logFos = null;
	private static SimpleDateFormat sdf=new SimpleDateFormat("MM_dd_hh_mm_ss");
	
    private LogUtil() {
        
    }
    public static void v(String tag, String msg) {
        if(DEBUG_FLAG){
            android.util.Log.v(tag, "" + msg);
        }
        outLogToFile(msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if(DEBUG_FLAG){
            android.util.Log.v(tag, "" + msg, tr);
        }
        outLogToFile(msg);
    }

    public static void d(String tag, String msg) {
        if(DEBUG_FLAG){
            android.util.Log.d(tag, "" + msg);
        }
        outLogToFile(msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if(DEBUG_FLAG){
            android.util.Log.d(tag, "" + msg, tr);
        }
        outLogToFile(msg);
    }

    public static void i(String tag, String msg) {
        if(DEBUG_FLAG){
            android.util.Log.i(tag, "" + msg);
        }
        outLogToFile(msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if(DEBUG_FLAG){
            android.util.Log.i(tag, "" + msg, tr);
        }
        outLogToFile(msg);
    }

    public static void w(String tag, String msg) {
        if(DEBUG_FLAG){
            android.util.Log.w(tag, "" + msg);
        }
        outLogToFile(msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if(DEBUG_FLAG){
            android.util.Log.w(tag, "" + msg, tr);
        }
        outLogToFile(msg);
    }

    public static void e(String tag, String msg) {
        if(DEBUG_FLAG){
            android.util.Log.e(tag, "" + msg);
        }
        outLogToFile(msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if(DEBUG_FLAG){
            android.util.Log.e(tag, "" + msg, tr);
        }
        outLogToFile(msg);
    }
    

	public static void closeLog() {
		if (logFos != null) {
			try {
				logFos.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
	
    public static void initLogFile() {
		if (DEBUG_FLAG||DEBUG_SAVE_FILE) {
			try {
				String timeTag = sdf.format(new Date());
				File file = new File(LOG_PATH);
				if (!file.exists()) {
					file.mkdirs();
				}
				file = new File(LOG_PATH + "/javalog_" + timeTag + ".txt");
				if (!file.exists()) {
					file.createNewFile();
				}
				logFos = new FileOutputStream(file, true);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    private static void outLogToFile(String content){
    	if(DEBUG_SAVE_FILE){
    		if(logFos==null){
    			initLogFile();
    		}
    		String timeTag = sdf.format(new Date());
        	try {
    			logFos.write((timeTag+content).getBytes());
    		} catch (Exception e) {
    			
    		}
    	}
    }
	public static String getTag(Class<?> className) {
		
		return className.getName();
	}
}
