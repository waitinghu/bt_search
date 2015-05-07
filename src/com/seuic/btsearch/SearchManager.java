package com.seuic.btsearch;

import java.util.ArrayList;

import com.seuic.bt_search.R;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler.Callback;
import android.os.Message;
import android.widget.Toast;

public class SearchManager implements Callback {
	
	
	
	public interface OnNewDeviceAttactListener {
		void onNewDeviceAttacted(BluetoothDevice device);
	}

	private static SearchManager manager;
	private Context mContext;
	private BluetoothAdapter mBTAdapter;
	private ArrayList<BluetoothDevice> devices;
	private OnNewDeviceAttactListener listener;

	private SearchManager(Context context) {
		this.mContext = context;
		initManager();
	}

	public static SearchManager getInstance(Context context) {

		if (manager == null) {
			manager = new SearchManager(context);
		}
		return manager;
	}

	private void initManager() {
	}
	
	
	public void setNewDiviceListener(OnNewDeviceAttactListener listener) {
		this.listener = listener;
	}

	public Boolean connectBT(BluetoothDevice device) {

		return false;
	}

	public boolean disconnectBt() {

		
		return false;
	}
	
	public boolean isDeviceinList(BluetoothDevice device) {
		
		return false;
	}
	
	public ArrayList<BluetoothDevice> getDevices() {
		return devices;
	}
	public boolean isDeviceConnect(BluetoothDevice device) {
		return false;
	}
	@Override
	public boolean handleMessage(Message msg) {
		
		int what = msg.what;
		if (what == BTSearchBroadcastReceiver.HEW_BT_DEVICE) {
			BluetoothDevice device = (BluetoothDevice) msg.obj;
			devices.add(device);
			listener.onNewDeviceAttacted(device);
		}
		return false;
	}

	public void startSearch() {
		Intent service = new Intent(mContext, BTSearchService.class);
		mContext.startService(service);
	}

}
