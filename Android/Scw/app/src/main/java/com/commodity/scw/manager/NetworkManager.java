package com.commodity.scw.manager;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.commodity.scw.MainApplication;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Function: 网络管理类
 * 
 * Date: 2016年3月30日 下午3:03:57
 * 
 * @author liyushen
 */
public final class NetworkManager {

	private final MainApplication context;

	private static NetworkManager instance;
	/**
	 * GSM 3G
	 */
	private static final int NETWORK_TYPE_UMTS = 3;

	/**
	 * CDMA 3G
	 */
	private static final int NETWORK_TYPE_EVDO_B = 12;

	private static final int TYPE_WIMAX = 6;

	public synchronized static void create(MainApplication context) {
		if (instance != null) {
			return;
		}
		instance = new NetworkManager(context);
	}

	public static NetworkManager instance() {
		if (instance == null) {
			throw new RuntimeException("NetworkManager not created");
		}
		return instance;
	}

	private NetworkManager(MainApplication context) {
		this.context = context;
	}

	public boolean isDataUp() {
		return isDataWIFIUp() || isDataMobileUp();
		// return (isDataWIFIUp() != isDataMobileUp()) || isDataWiMAXUp();
	}

	public boolean isDataMobileUp() {
		ConnectivityManager connectivityManager = getConnectivityManager();
		NetworkInfo networkinfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return networkinfo != null && networkinfo.isAvailable() && networkinfo.isConnected();
	}

	public boolean isData3GUp() {
		ConnectivityManager connectivityManager = getConnectivityManager();
		NetworkInfo networkinfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return networkinfo != null && networkinfo.isAvailable() && (networkinfo.getSubtype() == NETWORK_TYPE_UMTS || networkinfo.getSubtype() == NETWORK_TYPE_EVDO_B) && networkinfo.isConnected();
	}

	public boolean isDataWIFIUp() {
		ConnectivityManager connectivityManager = getConnectivityManager();
		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
	}

	public boolean isDataWiMAXUp() {
		ConnectivityManager connectivityManager = getConnectivityManager();
		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(TYPE_WIMAX);
		return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
	}

	/**
	 * current active network interface
	 * 
	 * @return NetworkInterface
	 */
	public NetworkInterface getNetworkInterface() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();

				Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress address = addresses.nextElement();
					if (!address.isLoopbackAddress()) {
						return networkInterface;
					}
				}
			}
			return null;
		} catch (Throwable e) {
			return null;
		}
	}

	private WifiManager getWifiManager() {
		return (WifiManager) context.getSystemService(Application.WIFI_SERVICE);
	}

	public String getMacAddress() {
		return getWifiManager().getConnectionInfo().getMacAddress();
	}

	private ConnectivityManager getConnectivityManager() {
		return (ConnectivityManager) context.getSystemService(Application.CONNECTIVITY_SERVICE);
	}
}
