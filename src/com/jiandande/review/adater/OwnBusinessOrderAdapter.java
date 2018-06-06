package com.jiandande.review.adater;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jiandande.money.R;
import com.jiandande.review.activity.BalanceChangeActivity;
import com.jiandande.review.activity.OwnBusinessTaskListActivity;
import com.jiandande.review.adater.LoadUserAvatar.ImageDownloadedCallBack;
import com.jiandande.review.bean.ExchangeBean;
import com.jiandande.review.util.PreferenceUtil;
import com.jiandande.review.util.StringUtil;
import com.jiandande.review.util.Constants.ParamName;


public class OwnBusinessOrderAdapter  extends BaseAdapter 
{
	private LoadSoftIcon avatarLoader;
	private ArrayList<ExchangeBean> mList = null;
	private Context mContext= null;
	private LayoutInflater layoutInflater;
	ImageView download_soft_icon;  
	String str1="";
	
	public OwnBusinessOrderAdapter(Context context ,ArrayList<ExchangeBean> datas){
		mContext = context;
		layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(datas==null){
			datas=new ArrayList<ExchangeBean>();
		}
		this.mList=datas;
	}

	 public void setDatas(ArrayList<ExchangeBean> datas,boolean isAdd) {
			if(isAdd){
				mList.addAll(datas);
			}else{
				mList.clear();
				mList.addAll(datas);
			}
		}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		
		ExchargeOrderItemHolder holder = null;
		if(convertView == null )
		{	
			holder = new ExchargeOrderItemHolder();
			convertView = layoutInflater.inflate(R.layout.myselftasklist_item, null);
			holder.txtPname = (TextView) convertView.findViewById(R.id.exchangeid2);
			holder.txtFileSize = (TextView) convertView.findViewById(R.id.filesize);
			holder.txtAppDescription = (TextView) convertView.findViewById(R.id.appdescription);
			holder.txtProductUrl = (TextView) convertView.findViewById(R.id.gift);
			holder.iv_avatar =(ImageView) convertView.findViewById(R.id.soft_down_img);
			holder.txtType = (TextView) convertView.findViewById(R.id.gift);
			holder.txtAmount = (TextView) convertView.findViewById(R.id.amount);
			holder.txtStatus = (TextView) convertView.findViewById(R.id.state);
			holder.txtTime = (TextView) convertView.findViewById(R.id.time);
			holder.txtAppiconurl = (TextView) convertView.findViewById(R.id.appiconurl);
			holder.txtPackagename = (TextView) convertView.findViewById(R.id.packagename);
			convertView.setTag(holder);
		}else{
			holder = (ExchargeOrderItemHolder) convertView.getTag();
		}
		ExchangeBean exchange=mList.get(position);
     //	holder.txtOrderId.setText(StringUtil.formatTransID(exchange.orderId));//exchange.orderId
     	if(exchange.pname.equals("积分兑换礼品"))
		{
		holder.txtType.setText(StringUtil.formatTransType(exchange.orderType));///StringUtil.GBK2Unicode(exchange.pname));
		}
     	
		else
		{
			holder.txtType.setText(StringUtil.formatTransType(exchange.orderType)+"["+exchange.pname+"]");	
		}
		holder.txtAmount.setText(StringUtil.moneyFormat2(exchange.amount));
		holder.txtStatus.setText(StringUtil.formatTransStatus(exchange.status));
		holder.txtTime.setText(StringUtil.formatTime(exchange.submitTime));
		holder.txtPname.setText(exchange.pname);
		holder.txtFileSize.setText(exchange.filesize);
		holder.txtProductUrl.setText(exchange.producturl);
		holder.iv_avatar =(ImageView) convertView.findViewById(R.id.soft_down_img);                                                           
	   // showUserAvatar(holder.iv_avatar, exchange.appiconurl);
		download_soft_icon=(ImageView) convertView.findViewById(R.id.soft_down_img); 
		avatarLoader = new LoadSoftIcon(mContext, "/sdcard/jiandande/");
		showAppAvatar(download_soft_icon, exchange.appiconurl);	 
		holder.txtAppDescription.setText(exchange.appdescription);                           
		holder.txtPackagename.setText(exchange.packagename);
		holder.txtAppiconurl.setText(exchange.appiconurl);
		return convertView;
	}
	
	private static class ExchargeOrderItemHolder{
		private TextView txtFileSize;
		private TextView txtType;
		private TextView txtAmount;
		private TextView txtStatus;
		private TextView txtTime;
		private TextView txtPname;                   
		private TextView txtProductUrl;
		private TextView txtAppDescription;
		private TextView txtPackagename;
		private TextView txtAppiconurl;
		private ImageView iv_avatar;
		
	}
	
	 public static Bitmap getBitmapFromURL(String src) {
	        try {
	    
	            URL url = new URL(src);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setDoInput(true);
	            connection.connect();
	            InputStream input = connection.getInputStream();
	            Bitmap myBitmap = BitmapFactory.decodeStream(input);
	 
	            return myBitmap;
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	 

	  private void showAppAvatar(ImageView iamgeView, String avatar) {
		 // avatar="http://www.jiandande.com//member/templets/images/dfgirl.png";
		  final String url_avatar = avatar;
		
	        iamgeView.setTag(url_avatar);
	        if (url_avatar != null && !url_avatar.equals("")) {
	            Bitmap bitmap = avatarLoader.loadImage(iamgeView, url_avatar,
	                    new ImageDownloadedCallBack() {

	                        @Override
	                        public void onImageDownloaded(ImageView imageView,
	                                Bitmap bitmap) {
	                            if (imageView.getTag() == url_avatar) {
	                               imageView.setImageBitmap(bitmap);
	                            }
	                        }

	                    });
	            if (bitmap != null)
	               iamgeView.setImageBitmap(bitmap);

	        }
	  }
}
