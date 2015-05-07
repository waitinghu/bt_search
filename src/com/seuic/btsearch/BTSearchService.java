package com.seuic.btsearch;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import com.seuic.bt_search.R;

public class BTSearchService extends Service {

	private BluetoothAdapter mBTAdapter;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mBTAdapter = BluetoothAdapter.getDefaultAdapter();

		if (mBTAdapter == null) {
			Toast.makeText(getApplicationContext(), R.string.unsupport_bt, Toast.LENGTH_LONG).show();
			return super.onStartCommand(intent, flags, startId);
		}

		if (intent.getBooleanExtra("isStopSearch", false)) {
			mBTAdapter.cancelDiscovery();
		} else {
			
			if (!mBTAdapter.isEnabled()) {
				Intent start = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				// 设置蓝牙可见性，最多300秒
				start.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
				start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				this.startActivity(start);
			}

			// creat a receiver to find bt device near around
			BTSearchBroadcastReceiver receiver = new BTSearchBroadcastReceiver();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
			intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
			intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
			intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
			this.registerReceiver(receiver, intentFilter);

			mBTAdapter.startDiscovery();
		}

		return super.onStartCommand(intent, flags, startId);
	}

}
