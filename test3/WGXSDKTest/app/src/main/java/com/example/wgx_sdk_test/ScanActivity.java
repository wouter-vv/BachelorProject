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
    private int counter1 = 0;
    private int counter2 = 0;
    private int counter3 = 0;
    private int counter4 = 0;
	int amtAvgIterations = 10;
    private int med1[] = new int[amtAvgIterations];
    private int med2[] = new int[amtAvgIterations];
    private int med3[] = new int[amtAvgIterations];
    private int med4[] = new int[amtAvgIterations];
	private boolean outlier1 = false;
	private boolean outlier2 = false;
	private boolean outlier3 = false;
	private boolean outlier4 = false;
	private List<Integer> somelist1 = new ArrayList();
	private List<Integer> somelist2 = new ArrayList();
	private List<Integer> somelist3 = new ArrayList();
	private List<Integer> somelist4 = new ArrayList();
	private double avg1;
	private double avg2;
	private double avg3;
	private double avg4;



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

		if (rssi < 0 && rssi > -100) {
			if(counter1 <= amtAvgIterations && device.getName().equals("WXG1")) {
				somelist1.add(device.getRssi());
				avg1= calculateAverage(somelist1);
				if (counter1 == amtAvgIterations) {
					counter1 = 0;
				}
				if (counter1 != 0) {
					//check if the value isnt 10 higher of lower than the previous one to filter out random values
					if (!(med1[counter1] < (med1[counter1 - 1] -= 10) || med1[counter1] < (med1[counter1 - 1] += 10)) || outlier1 == true) {
						med1[counter1] = rssi;
						counter1++;
						int avg = 0;
						for (int val : med1) {
							avg += val;
						}
						avg /= amtAvgIterations;
						map.put("rssi", "rssi: " + calculateDistance(avg, device.getTxPower()));
						outlier1 = false;
					} else {
						// if there are 2 continious outliers, that means the distance really changed
						outlier1 = true;
					}
				}
				else {
					med1[counter1] = rssi;
					counter1++;
					int avg = 0;
					for (int val : med1) {
						avg += val;
					}
					avg /= amtAvgIterations;
					map.put("rssi", "rssi: " + calculateDistance(avg, device.getTxPower()));
					outlier1 = false;
				}
			}

			if(counter2 <= amtAvgIterations && device.getName().equals("WXG2")) {
				somelist2.add(device.getRssi());
				avg2= calculateAverage(somelist2);
				if (counter2 == amtAvgIterations) {
					counter2=0;
				}
				if(counter2!=0) {
					//check if the value isnt 10 higher of lower than the previous one to filter out random values
					if (!(med2[counter2] < (med2[counter2 - 1] -= 10) || med2[counter2] < (med2[counter2 - 1] += 10)) || outlier2 == true) {
						med2[counter2] = rssi;
						counter2++;
						int avg=0;
						for (int val : med2) {
							avg+=val;
						}
						avg/=amtAvgIterations;
						map.put("rssi", "rssi: " + calculateDistance(avg, device.getTxPower()));
						outlier2 = false;
					}
					else {
						// if there are 2 continious outliers, that means the distance really changed
						outlier2 = true;
					}
				}
				else {
					med2[counter2] = rssi;
					counter2++;
					int avg=0;
					for (int val : med2) {
						avg+=val;
					}
					avg/=amtAvgIterations;
					map.put("rssi", "rssi: " + calculateDistance(avg, device.getTxPower()));
					outlier2 = false;
				}
			}
			if(counter3 <= amtAvgIterations && device.getName().equals("WXG3")) {
				somelist3.add(device.getRssi());
				avg3 = calculateAverage(somelist3);
				if (counter3 == amtAvgIterations) {
					counter3=0;
				}
				if(counter3!=0) {
					//check if the value isnt 10 higher of lower than the previous one to filter out random values
					if (!(med3[counter3] < (med3[counter3 - 1] -= 10) || med3[counter3] < (med3[counter3 - 1] += 10)) || outlier3 == true) {
						med3[counter3] = rssi;
						counter3++;
						int avg=0;
						for (int val : med3) {
							avg+=val;
						}
						avg/=amtAvgIterations;
						map.put("rssi", "rssi: " + calculateDistance(avg, device.getTxPower()));
						outlier3 = false;
					}
					else {
						// if there are 2 continious outliers, that means the distance really changed
						outlier3 = true;
					}
				}
				else {
					med3[counter3] = rssi;
					counter3++;
					int avg=0;
					for (int val : med3) {
						avg+=val;
					}
					avg/=amtAvgIterations;
					map.put("rssi", "rssi: " + calculateDistance(avg, device.getTxPower()));
					outlier3 = false;
				}

			}
			if(counter4 <= amtAvgIterations && device.getName().equals("WXG4")) {
				somelist4.add(device.getRssi());
				avg4 = calculateAverage(somelist4);
				if (counter4 == amtAvgIterations) {
					counter4 = 0;
				}
				if (counter4 != 0) {
					//check if the value isnt 10 higher of lower than the previous one to filter out random values
					if (!(med4[counter4] < (med4[counter4 - 1] -= 10) || med4[counter4] < (med4[counter4 - 1] += 10)) || outlier4 == true) {
						med4[counter4] = rssi;
						counter4++;
						int avg = 0;
						for (int val : med4) {
							avg += val;
						}
						avg /= amtAvgIterations;
						map.put("rssi", "rssi: " + calculateDistance(avg, device.getTxPower()));
						outlier4 = false;
					} else {
						// if there are 2 continious outliers, that means the distance really changed
						outlier4 = true;
					}
				}
				else {
					med4[counter4] = rssi;
					counter4++;
					int avg = 0;
					for (int val : med4) {
						avg += val;
					}
					avg /= amtAvgIterations;
					map.put("rssi", "rssi: " + calculateDistance(avg, device.getTxPower()));
					outlier4 = false;
				}
			}
		}


		map.put("uuid", "uuid: " + device.getUuid());
			map.put("major", "major: " + device.getMajor());
		map.put("minor","minor: " + device.getMinor());

		map.put("acc", ("acc: x: " + device.getAccX() + "  y: " + device.getAccY() + "  z: " + device.getAccZ()));
		map.put("temp", "bat: " + device.getBattery());

		if (device.getBattery() > 0 && device.getBattery() <= 100) {
			map.put("battery", "battery: " + device.getRssi() + "%");

		} else {
			map.put("battery", "0%");
		}

		if (isConnected) {
			map.put("arrow", R.drawable.icon_arrow_right);
		}

		map.put("timeout", 0);

		return map;
	}
	private double calculateAverage(List <Integer> marks) {
		Integer sum = 0;
		if(!marks.isEmpty()) {
			for (Integer mark : marks) {
				sum += mark;
			}
			return sum.doubleValue() / marks.size();
		}
		return sum;
	}
	public double calculateDistance(int rssi, int nothing) {
		return Math.pow( 10,((-53 - rssi)/ (10*2)));
	}
	public double calculateDistance2(int rssi, int txPower) {
		txPower = -65; //hard coded power value. Usually ranges between -59 to -65

		if (rssi == 0) {
			return -1.0;
		}

		double ratio = rssi*1.0/txPower;
		if (ratio < 1.0) {
			return Math.pow(ratio,10);
		}
		else {
			double distance =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;
			return distance;
		}
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
