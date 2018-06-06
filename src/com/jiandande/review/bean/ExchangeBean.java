package com.jiandande.review.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.jiandande.review.util.Constants.OrderType;



public class ExchangeBean implements Parcelable {

	public int orderId;
	public String pname;
	public int orderType = OrderType.TYPE_ALIPAY;
	public double amount;
	public int status;
	public long submitTime;
	public String filesize;
	public String producturl;
	public String appdescription;
	public String packagename;
	public String appiconurl;
	//public ImageView download_soft_icon;

	/*	public double fee;
	public long submitTime;
	public long lastUpdateTime;
	public int submiterId;// 申请人
	public String phoneNo;
	public String attribute;
	public String qqNo;
	public String qqName;
	public String alpayNo;
	public String alpayName;

	public String remark;// 说明
	public int adminId;
	public String submiterAccountName;

	public String setType="1";
	public double otherAmount;*/
	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(this.orderId);
		dest.writeInt(this.orderType);
		dest.writeDouble(this.amount);
		//dest.writeDouble(fee);
		dest.writeLong(submitTime);
		dest.writeInt(this.status);
		dest.writeString(this.pname);
		dest.writeString(this.filesize);
		dest.writeString(this.producturl);
		dest.writeString(this.appdescription);
		dest.writeString(this.packagename);
		dest.writeString(this.appiconurl);
	
	/*	dest.writeLong(this.lastUpdateTime);
		dest.writeInt(this.submiterId);
		dest.writeString(this.phoneNo);
		dest.writeString(attribute);
		dest.writeString(qqNo);
		dest.writeString(qqName);

		dest.writeString(this.alpayNo);
		dest.writeString(this.alpayName);

		dest.writeString(remark);
		dest.writeInt(adminId);
		dest.writeString(submiterAccountName);
		dest.writeString(orderNo);
		dest.writeDouble(otherAmount);
			dest.writeString(setType);

	*/
	}

	public static final Parcelable.Creator<ExchangeBean> CREATOR = new Parcelable.Creator<ExchangeBean>() {
		@Override
		public ExchangeBean createFromParcel(Parcel source) {
			ExchangeBean bean = new ExchangeBean();
			// 从Parcel中读取数据，返回ProductBean对象
			bean.orderId = source.readInt();
			bean.orderType = source.readInt();
			bean.amount = source.readDouble();
			bean.status = source.readInt();
			bean.pname = source.readString();
		//	bean.fee = source.readDouble();
			bean.submitTime = source.readLong();
			bean.filesize = source.readString();
			bean.producturl = source.readString();
			bean.appdescription = source.readString();
			bean.packagename = source.readString();
			bean.appiconurl = source.readString();
		/*	bean.lastUpdateTime = source.readLong();
			bean.submiterId = source.readInt();// 申请人
			bean.phoneNo = source.readString();
			bean.attribute = source.readString();
			bean.qqNo = source.readString();
			bean.qqName = source.readString();
			bean.alpayNo = source.readString();
			bean.alpayName = source.readString();
	
			bean.remark = source.readString();// 说明
			bean.adminId = source.readInt();
			bean.submiterAccountName = source.readString();*/
		//	bean.orderNo = source.readString();	
		//	bean.otherAmount= source.readDouble();	
		//	bean.setType= source.readString();	
			
			return bean;
		}

		@Override
		public ExchangeBean[] newArray(int size) {
			return new ExchangeBean[size];
		}
	};

	@Override
	public String toString() {

		return orderType + "..." + orderId + "...." + "..." + status + "....";
	}

}
