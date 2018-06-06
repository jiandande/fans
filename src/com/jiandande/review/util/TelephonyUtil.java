package com.jiandande.review.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;


/**
 * @author shimingzheng
 * @version 创建时间：2012-11-16 上午9:59:27
 * 说明：该方法主要用于获取基站信息
 */

public class TelephonyUtil {
	private static final String TAG = "TelStationUtil";
	
	Context mContext;
	public TelephonyUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext=context;
	}
	
	/**
	 * 获取拨打电话的次数
	 * @return
	 */
	public String getCellCount()
	{
		String count = "";
		try {
			Cursor cursor =mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI,
				    null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
			count = cursor.getCount()+"";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * 获取基站信息
	 * @param context
	 * @return
	 */
	public  String getSCell()
	{
		SCell sCell = null;
		try {
			TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
			if(telephonyManager == null )
			{
				return "";
			}
			
			CellLocation cellLocation =  telephonyManager.getCellLocation();
			if(cellLocation == null )
			{
				Log.i(TAG, "get gsm cell location is null");
				return "";
			}
			sCell = new SCell();
			if(cellLocation instanceof GsmCellLocation){
				GsmCellLocation gsmCellLocation=(GsmCellLocation)cellLocation; 
				sCell.CID = gsmCellLocation.getCid();
				sCell.LAC = gsmCellLocation.getLac();
			}else if(cellLocation instanceof CdmaCellLocation){
				CdmaCellLocation cdmaCellLocation=(CdmaCellLocation)cellLocation;
				sCell.CID = cdmaCellLocation.getBaseStationId();
				sCell.LAC = cdmaCellLocation.getNetworkId();
				sCell.MNC = cdmaCellLocation.getSystemId();
			}
			String operator = telephonyManager.getNetworkOperator();
			if(operator != null && operator.length() >3)
			{
				int mcc = Integer.parseInt(operator.substring(0, 3)); 
			    int mnc = Integer.parseInt(operator.substring(3)); 
			    sCell.MCC = mcc;
			    sCell.MNC = mnc;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return sCell.toString();
	}

	/**
	 *MCC，Mobile Country Code，移动国家代码（中国的为460）；
	 *MNC，Mobile Network Code，移动网络号码（中国移动为00，中国联通为01）；
	 *LAC，Location Area Code，位置区域码；
	 *CID，Cell Identity，基站编号，是个16位的数据（范围是0到65535）。
	 * @author shimingzheng
	 *
	 */
	public static class SCell{
		public int MCC = -1;
		public int MNC = -1;
		public int LAC = -1;
		public int CID = -1;
		@Override
		public String toString() {
			return  MCC + "," + MNC + "," + LAC + "," + CID;
		}
	}
}
