package com.seuic.btsearch;

import java.util.ArrayList;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Handler;
import android.os.Message;

public class BTSearchBroadcastReceiver extends BroadcastReceiver {

	Handler handler;
	public static int HEW_BT_DEVICE;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		SearchManager manager = SearchManager.getInstance(context);
		if (handler == null) {
			handler = new Handler(manager);
		}
		if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			if (device != null) {
				Message msg = new Message();
				msg.obj = device;
				msg.what = HEW_BT_DEVICE;
				handler.sendMessage(msg);
			}
		}

	}

}
