package com.example.myconnections_android.core.structure.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Connections {

	public static boolean isWIFINetworkAvailable(Context context) {
		NetworkInfo info = getConnectivityManager(context).getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return info != null && info.getState() == NetworkInfo.State.CONNECTED;
    }
	
	public static boolean isWIMAXNetworkAvailable(Context context) {
		NetworkInfo info = getConnectivityManager(context).getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
        return info != null && info.getState() == NetworkInfo.State.CONNECTED;
    }
	
	public static boolean isMobileNetworkAvailable(Context context) {
		NetworkInfo info = getConnectivityManager(context).getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return info != null && info.getState() == NetworkInfo.State.CONNECTED;
    }
	
	public static boolean isInternetStreamingAvailable(Context context) {
        return /*!isCheckerInvalid() &&*/ (isWIMAXNetworkAvailable(context) || isWIFINetworkAvailable(context));
    }
	
	public static boolean isInternetAvailable(Context context) {
        return isMobileNetworkAvailable(context) || isInternetStreamingAvailable(context);
    }

/*    private static boolean isCheckerInvalid() {
        return RemoteHostReachableChecker.get() != null && RemoteHostReachableChecker.get().isListening() && !RemoteHostReachableChecker.get().isReachable();
    }*/

    private static ConnectivityManager getConnectivityManager(Context context){
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}
