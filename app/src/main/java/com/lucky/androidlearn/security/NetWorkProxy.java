package com.lucky.androidlearn.security;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

// 检查是否使用了代理
public class NetWorkProxy {
    private static final String TAG = "NetWorkProxy";
    private Context mContext;

    private static NetWorkProxy mNetWorkProxy;

    private NetWorkProxy(Context context) {
        this.mContext = context;
    }

    public static NetWorkProxy getInstance(Context context) {
        if (mNetWorkProxy == null) {
            synchronized (NetWorkProxy.class) {
                if (mNetWorkProxy == null) {
                    mNetWorkProxy = new NetWorkProxy(context);
                }
            }
        }
        return mNetWorkProxy;
    }


    /**
     * 是否使用代理(WiFi状态下的,避免被抓包)
     */
    public boolean isWifiProxy() {
        final boolean is_ics_or_later = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (is_ics_or_later) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portstr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portstr != null ? portstr : "-1"));
            System.out.println(proxyAddress + "~");
            System.out.println("port = " + proxyPort);
        } else {
            proxyAddress = android.net.Proxy.getHost(mContext);
            proxyPort = android.net.Proxy.getPort(mContext);
            Log.e("address = ", proxyAddress + "~");
            Log.e("port = ", proxyPort + "~");
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }


    /**
     * 是否正在使用VPN
     */

    public static boolean isVpnUsed() {
        try {
            Enumeration niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                ArrayList<NetworkInterface> list = Collections.list(niList);
                for (NetworkInterface intf : list) {
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    Log.d("-----", "isVpnUsed() NetworkInterface Name: " + intf.getName());
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())) {
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;

    }


}
