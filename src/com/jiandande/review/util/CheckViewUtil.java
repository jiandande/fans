package com.jiandande.review.util;

import com.jiandande.money.R;
import com.jiandande.review.dialog.DialogFactory;

import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author hj
 * @version 创建时间：2013-12-19 下午6:08:42
 * 说明：
 */

public class CheckViewUtil {
	/***
	 * 检验输入的内容是否合法
	 * 
	 * @param text
	 *            需要检查的Editable文本
	 * @param ViewTag
	 *            当前view的名称标志
	 * @param manLength
	 *            最小长度
	 * @param maxLength
	 *            最大长度
	 * @param dialog
	 *            弹出消息
	 * @return boolean
	 */
	public static boolean checkLengthIsOk(Editable text, String ViewTag,
			int manLength, int maxLength, DialogFactory dialog) {
		return checkLengthIsOk(text.toString(), ViewTag, manLength, maxLength,
				dialog);
	}

	/***
	 * 检验输入的内容是否合法
	 * 
	 * @param text
	 *            需要检查的Editable文本
	 * @param ViewTag
	 *            当前view的名称标志
	 * @param okLength
	 *            标准长度
	 * @param dialog
	 *            弹出消息
	 * @return boolean
	 */
	public static boolean checkLengthIsOk(Editable text, String ViewTag,
			int okLength, DialogFactory dialog) {
		return checkLengthIsOk(text.toString(), ViewTag, okLength, dialog);
	}

	public static boolean checkLengthIsOk(Editable text, String ViewTag,
			Integer[] okLengths, DialogFactory dialog) {
		if (okLengths == null || okLengths.length <= 0) {
			return true;
		} else {
			String okLength = "";
			for (int i = 0; i < okLengths.length; i++) {
				okLength += okLengths[i] + ",";
			}
			okLength = okLength.substring(0, okLength.length() - 1);
			if (!StringUtil.checkNotNull(text)) {
//				String message=dialog.getmContext().getString(R.string.not_null);
//				dialog.showNullDialog(DialogFactory.ALERT_DIALOG, ViewTag
//						+" "+ message);
				return false;
			} else {
				boolean isOk = false;
				int textLength = text.toString().length();
				for (int i = 0; i < okLengths.length; i++) {
					if (textLength == okLengths[i]) {
						isOk = true;
						break;
					}
				}
				if (isOk) {
					return true;
				} else {
//					String message=dialog.getmContext().getString(R.string.length);
//					message=String.format(message,okLength+"",textLength+"");
//					dialog.showNullDialog(DialogFactory.ALERT_DIALOG, ViewTag+" "+message);
					return false;
				}
			}
		}
	}

	/***
	 * 检验输入的内容是否合法
	 * 
	 * @param text
	 *            需要检查的EditText文本
	 * @param ViewTag
	 *            当前view的名称标志
	 * @param manLength
	 *            最小长度
	 * @param maxLength
	 *            最大长度
	 * @param dialog
	 *            弹出消息
	 * @return boolean
	 */
	public static boolean checkLengthIsOk(EditText text, String ViewTag,
			int minLength, int maxLength, DialogFactory dialog) {
		return checkLengthIsOk(text.getText(), ViewTag, minLength, maxLength,
				dialog);
	}

	/***
	 * 检验输入的内容是否合法
	 * 
	 * @param text
	 *            需要检查的EditText文本
	 * @param ViewTag
	 *            当前view的名称标志
	 * @param okLength
	 *            标准长度
	 * @param dialog
	 *            弹出消息
	 * @return boolean
	 */

	public static boolean checkLengthIsOk(EditText text, String ViewTag,
			int okLength, DialogFactory dialog) {
		return checkLengthIsOk(text.getText(), ViewTag, okLength, dialog);
	}

	/***
	 * 检验输入的内容是否合法
	 * 
	 * @param text
	 *            需要检查的String文本
	 * @param ViewTag
	 *            当前view的名称标志
	 * @param manLength
	 *            最小长度
	 * @param maxLength
	 *            最大长度
	 * @param dialog
	 *            弹出消息
	 * @return boolean
	 */
	public static boolean checkLengthIsOk(String text, String ViewTag,
			int minLength, int maxLength, DialogFactory dialog) {
		if (!StringUtil.checkNotNull(text)) {
//			String message=dialog.getmContext().getString(R.string.not_null);
//			dialog.showNullDialog(DialogFactory.ALERT_DIALOG, ViewTag+" " + message);
			return false;
		} else {
			boolean isOk = false;
			int length = text.length();
			if (minLength > length) {
				
//				String message=dialog.getmContext().getString(R.string.less_n);
//				dialog.showNullDialog(DialogFactory.ALERT_DIALOG, ViewTag
//						+" "+ String.format(message,minLength+"" ));
			} else if (maxLength < length) {
//				String message=dialog.getmContext().getString(R.string.more_n);
//				dialog.showNullDialog(DialogFactory.ALERT_DIALOG, ViewTag
//						+" "+ String.format(message,maxLength+"" ));
			} else {
				isOk = true;
			}
			return isOk;
		}
	}

	/***
	 * 检验输入的内容是否合法
	 * 
	 * @param text
	 *            需要检查的String文本
	 * @param ViewTag
	 *            当前view的名称标志
	 * @param okLength
	 *            标准长度
	 * @param dialog
	 *            弹出消息
	 * @return boolean
	 */
	public static boolean checkLengthIsOk(String text, String ViewTag,
			int okLength, DialogFactory dialog) {
		if (!StringUtil.checkNotNull(text)) {
//			String message=dialog.getmContext().getString(R.string.not_null);
//			dialog.showNullDialog(DialogFactory.ALERT_DIALOG, ViewTag +" "+message);
			return false;
		} else {
			boolean isOk = false;
			int length = text.length();
			if (okLength > length) {
//				String message=dialog.getmContext().getString(R.string.not_less_n);
//				dialog.showNullDialog(DialogFactory.ALERT_DIALOG, ViewTag+" "+
//						String.format(message,okLength+"", okLength+""));
			} else if (okLength < length) {
//				String message=dialog.getmContext().getString(R.string.not_more_n);
//						dialog.showNullDialog(DialogFactory.ALERT_DIALOG, ViewTag+" "+
//								String.format(message,okLength+"", okLength+""));
			} else {
				isOk = true;
			}
			return isOk;
		}

	}

	/**判断一个textView　非空
	 * @param txtName
	 * @param ViewTag
	 * @param dialog
	 * @return
	 */
	public static boolean checkCardNotNull(TextView txtName, String ViewTag,
			DialogFactory dialog) {
		CharSequence content=txtName.getText();
		if (!StringUtil.checkNotNull(content)) {
			return false;
		}
		return true;
	}

	public static boolean checkCardEqal(String account, String reAcountNo) {
		if(account==null||reAcountNo==null){
			return false;
		}else {
			account=StringUtil.clearFormat(account);
			reAcountNo=StringUtil.clearFormat(reAcountNo);
			return account.equals(reAcountNo);
		}
	}

	
}
