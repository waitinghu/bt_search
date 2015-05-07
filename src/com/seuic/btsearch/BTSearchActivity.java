package com.seuic.btsearch;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.seuic.bt_search.R;
import com.seuic.btsearch.SearchManager.OnNewDeviceAttactListener;

public class BTSearchActivity extends Activity implements OnClickListener ,OnNewDeviceAttactListener{

	private static final String TAG = "BTSearchActivity";
	private Button mBTsearch;
	private Button mStop;
	private SearchManager manager;
	private ListView mDeviceList;
	private BTDeviceAdapt adapt;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		manager = SearchManager.getInstance(this);
		manager.setOnNewDeviceAttactListener(this);
		mBTsearch = (Button) findViewById(R.id.btn_search);
		mStop = (Button) findViewById(R.id.btn_stop);
		mStop.setOnClickListener(this);
		mBTsearch.setOnClickListener(this);
		mDeviceList = (ListView) findViewById(R.id.bt_device_list);
		adapt = new BTDeviceAdapt();
		mDeviceList.setAdapter(adapt);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == mBTsearch.getId()) {
			manager.startSearch();
		}
		if(v.getId() == mStop.getId()) {
			manager.stopSearch();
		}
	}
	
	public class BTDeviceAdapt extends BaseAdapter {

		@Override
		public int getCount() {
			return manager.getDevices().size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			final BluetoothDevice device = manager.getDevices().get(position);
			
			LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		    View view = inflater.inflate(R.layout.device_list, null);
		    TextView tv = (TextView) view.findViewById(R.id.device_name);
		    tv.setText(device.getName());
		    TextView tv2 = (TextView) view.findViewById(R.id.device_mac_address);
		    tv2.setText(device.getAddress());
		    
		    Button bt = (Button) view.findViewById(R.id.bt_conn);
		    
		    if(!manager.isDevicesPaired(device)) {
		    	bt.setText(R.string.disconnect);
		    }else {
		    	bt.setText(R.string.connect);
		    }
		    bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					manager.doClick(device);
				}
			});
		    
			return view;
		}

	}
	
	@Override
	public void onNewDeviceAttacted(BluetoothDevice device) {
		Log.d(TAG, device.toString());
		if (adapt != null) {
			adapt.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
