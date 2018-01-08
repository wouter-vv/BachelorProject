package com.example.wgx_sdk_test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wellcore.ble.ConnectedDeviceEntity;
import com.wellcore.ibeacon.BeaconApplication;
import com.wellcore.ibeacon.BeaconController;
import com.wellcore.ibeacon.BeaconStateListener;
import com.wellcore.ibeacon.ScanDataEntiy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScanActivity extends Activity{
	public static final String TAG = "Activity";
	public static final int TIMER_1S = 1000;

	private Handler mHandler;
	private Runnable runnable;
	private ListAdapter mAdapter;
	private List<Map<String, Object>> mList;
	private ListView mListView;

	private ProgressDialog progressDialog;
	private BeaconStateListener mBeaconStateListener;
	private boolean isConnected = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_scan);
			
		BeaconApplication.getInstance().addActivity(this);

		InitView();
		
		// set a timer 1s
		mHandler = new Handler();
		runnable = new Runnable() {
			@Override
			public void run() {
				timerRunBackgouround();
				mHandler.postDelayed(this, TIMER_1S);
			}
		};

		mBeaconStateListener = new BeaconStateListener() {

			@Override
			public void onOperate(int opCode) {
				
			}

			@Override
			public void onOperateError(int opCode, String errCode) {
				
			}

			@Override
			public void onGetObject(int opCode, Object data) {
				if (opCode == BeaconController.OP_GET_DEVICE_SCAN_DATA) {
					ScanDataEntiy device = (ScanDataEntiy) data;
					Log.i(TAG,"Addr:" + device.getAddress() + " Rssi:" + device.getRssi());
					Log.i(TAG,"UUID:" + device.getUuid());
					Log.i(TAG,"Major:" + device.getMajor() + " Minor:" + device.getMinor());
					Log.i(TAG,"Tmep:" + device.getTemprature());

					int modfiyIndex = -1;

					for (int i = 0; i < mList.size(); i++) {
						if (mList.get(i).get("addr").equals(device.getAddress())) {
							modfiyIndex = i;
							break;
						}
					}

					if (modfiyIndex == -1) {
						mList.add(getScanMap(device));
						mapSort(mList);
						((BaseAdapter) mAdapter).notifyDataSetChanged();
						//mTitle.setText("Devices Nums: " + mList.size());
					} else {
						mList.set(modfiyIndex, getScanMap(device));
						((BaseAdapter) mAdapter).notifyDataSetChanged();

					}
				}
			}
		};

		BeaconController.getInstance().setBeaconStateListener(mBeaconStateListener);

	}

	
	@Override
	protected void onResume() {
		super.onResume();
		mHandler.postDelayed(runnable, TIMER_1S);

		BeaconController.getInstance().setBeaconStateListener(mBeaconStateListener);
		BeaconController.getInstance().startScanDevice();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mHandler.removeCallbacks(runnable);
		BeaconController.getInstance().pauseScanDevice();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BeaconController.getInstance().destroyService();
	}

	private void InitView() {

		mListView = (ListView) findViewById(R.id.list);
		mList = new ArrayList<Map<String, Object>>();

		mAdapter = new SimpleAdapter(this, mList, R.layout.list_scan, new String[] { "addr", "name", "rssi",
				"uuid", "major", "minor", "temp", "battery", "acc", "arrow" },
				new int[] { R.id.addr, R.id.device_name, R.id.rssi, R.id.uuid, R.id.major, R.id.minor,
						R.id.temprature, R.id.battery, R.id.acc,
						R.id.iamge_arrow_connected });

		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
				Log.i(TAG,"Click listview item to connect in scan mode.");
				
				if(isConnected){								
					String addr = (String) mList.get(index).get("addr");
					BeaconController.getInstance().connect(new ConnectedDeviceEntity(addr));
				}

			}
		});

	}

	private HashMap<String, Object> getScanMap(ScanDataEntiy device) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("addr", device.getAddress());
		if (device.getName() == null) {
			map.put("name", "name: Unknown");
		} else {
			map.put("name", "name: " + device.getName());
		}

		int rssi = device.getRssi();
		if (rssi < 0) {
			map.put("rssi", "rssi: " + rssi);
		} else {
			map.put("rssi", "rssi: " + ((70 - rssi) < 0 ? (70 - rssi) : (rssi - 70)));
		}

		map.put("uuid", "uuid: " + device.getUuid());
		map.put("major", "major: " + device.getMajor());
		map.put("minor","minor: " + device.getMinor());

		map.put("acc", ("acc: x: " + device.getAccX() + "  y: " + device.getAccY() + "  z: " + device.getAccZ()));
		map.put("temp", "temp: " + device.getTemprature() + "℃");

		if (device.getBattery() > 0 && device.getBattery() <= 100) {
			map.put("battery", "battery: " + device.getBattery() + "%");

		} else {
			map.put("battery", "0%");
		}

		if (isConnected) {
			map.put("arrow", R.drawable.icon_arrow_right);
		}

		map.put("timeout", 0);

		return map;
	}

	private void mapSort(List<Map<String, Object>> list) {
		if (!list.isEmpty()) {
			Collections.sort(list, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> obj1, Map<String, Object> obj2) {

					int ret = obj1.get("uuid").toString().compareTo(obj2.get("uuid").toString());

					if (ret == 0) {
						ret = Integer.parseInt((obj1.get("major").toString().substring(7)), 16)
								- Integer.parseInt((obj2.get("major").toString().substring(7)), 16);

						if (ret == 0) {
							ret = Integer.parseInt((obj1.get("minor").toString().substring(7)), 16)
									- Integer.parseInt((obj2.get("minor").toString().substring(7)), 16);

							if (ret == 0) {
								ret = obj1.get("name").toString().compareTo(obj2.get("name").toString());
							}
						}
					}

					return ret;
				}
			});
		}
	}

	private void timerRunBackgouround() {
		
		// for scan beacon filter
		for (int i = 0; i < mList.size(); i++) {
		
			Map<String, Object> map = mList.get(i);

			if (!isConnecting()) {
				// 10s timeout.
				int timeout = (Integer) map.get("timeout");
				if (timeout >= 10) {			
					mList.remove(i);
					i--;
				} else {
					timeout++;
					mList.get(i).put("timeout", timeout);
				}

			}

			((BaseAdapter) mAdapter).notifyDataSetChanged();
		
		}

	}

	protected void showProgress(boolean state, int id) {
		if (state) {
			if (progressDialog == null || !progressDialog.isShowing()) {
				progressDialog = ProgressDialog.show(this, null, getResources().getString(id));
			}
		} else {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		}
	}
	
	

	private boolean isConnecting() {
		return progressDialog != null;
	}


}
