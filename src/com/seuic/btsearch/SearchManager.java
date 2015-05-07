package com.seuic.btsearch;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import com.seuic.bt_search.R;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler.Callback;
import android.os.Message;
import android.widget.Toast;

public class SearchManager implements Callback {

	private static SearchManager manager;
	private Context mContext;
	private ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
	private OnNewDeviceAttactListener listener;

	public interface OnNewDeviceAttactListener {
		void onNewDeviceAttacted(BluetoothDevice device);
	}

	public void setOnNewDeviceAttactListener(OnNewDeviceAttactListener listener) {
		this.listener = listener;
	}

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

	public boolean isDeviceInList(BluetoothDevice device) {

		if(!devices.isEmpty()) {
			for (BluetoothDevice dev : devices) {
				if (dev.getAddress().equals(device.getAddress())) {
					return true;
				}
			}
		}
		return false;
	}

	public ArrayList<BluetoothDevice> getDevices() {
		return devices;
	}

	@Override
	public boolean handleMessage(Message msg) {
		int what = msg.what;
		if (what == BTSearchBroadcastReceiver.HEW_BT_DEVICE) {
			BluetoothDevice device = (BluetoothDevice) msg.obj;
			if (!isDeviceInList(device)) {
				devices.add(device);
				listener.onNewDeviceAttacted(device);
			}
		}
		return false;
	}

	public void startSearch() {
		devices.removeAll(devices);
		Intent service = new Intent(mContext, BTSearchService.class);
		mContext.startService(service);
	}

	public void stopSearch() {
		Intent service = new Intent(mContext, BTSearchService.class);
		service.putExtra("isStopSearch", true);
		mContext.startService(service);
	}

	public boolean isDevicesPaired(BluetoothDevice device) {
		BluetoothAdapter mBTAdapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> dev = mBTAdapter.getBondedDevices();
		return dev.contains(device);
	}

	public void doClick(BluetoothDevice device) {

		if (!isDevicesPaired(device)) {
			int connectState = device.getBondState();
			switch (connectState) {
			// 未配对
			case BluetoothDevice.BOND_NONE:
				// 配对
				try {
					Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
					createBondMethod.invoke(device);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			// 已配对
			case BluetoothDevice.BOND_BONDED:
				try {
					// 连接
					connect(device);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}

		}

	}
	
	private void connect(BluetoothDevice device) throws IOException {    
	    // 固定的UUID      
	    final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";    
	    UUID uuid = UUID.fromString(SPP_UUID);    
	    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);    
	    socket.connect();    
	} 

}
