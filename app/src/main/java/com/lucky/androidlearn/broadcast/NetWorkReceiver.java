package com.lucky.androidlearn.broadcast;

import com.jingewenku.abrahamcaijin.commonutil.AppNetworkMgr;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * 判断网络状态广播
 * @author fengzhou
 */
public class NetWorkReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
			boolean isConnected = AppNetworkMgr.isNetworkConnected(context);
//	        System.out.println("网络状态：" + isConnected);
//	        System.out.println("wifi状态：" + NetUtils.isWifiConnected(context));
//	        System.out.println("移动网络状态：" + NetUtils.isMobileConnected(context));
//	        System.out.println("网络连接类型：" + NetUtils.getConnectedType(context));
	        if (isConnected) {
//	           Util.showToast("网络已经成功连接");
	        } else {
	           AppToastMgr.ToastShortCenter(context, "网络已经断开连接");
	        }
		}
	}
	
}
