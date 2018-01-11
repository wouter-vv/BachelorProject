package com.example.wgx_sdk_test;


import com.wellcore.ibeacon.BeaconConfig;
import com.wellcore.ibeacon.BeaconController;
import com.wellcore.ibeacon.BeaconStateListener;
import com.wellcore.ibeacon.ScanDataEntiy;
import com.zmz.util.ToastUtil;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends FragmentActivity {
	private static final String TAG = "Activity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i(TAG,"MainActivity onCreate");
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		// step1 : Bind this activity
		BeaconController.getInstance().bindActiviyPage(this);

		// step2 : Basic setting
		BeaconController.getInstance().setChipType(BeaconController.CHIP_NRF51822);//default 'CHIP_NRF51822'
		BeaconController.getInstance().setDebugPrint(true); //default 'false'

		// step3 : Register beacon state listener
		BeaconController.getInstance().setBeaconStateListener(
				new BeaconStateListener() {
					
					@Override
					public void onGetObject(int opCode, Object data) {		
						Log.i(TAG,"opCode=" + opCode);
						 if (opCode == BeaconController.OP_GET_DEVICE_SCAN_DATA) {
								ScanDataEntiy device = (ScanDataEntiy) data;
								Log.i(TAG,"Name:" + device.getName());
								Log.i(TAG,"Addr:" + device.getAddress() + " Rssi:" + device.getRssi());
								Log.i(TAG,"UUID:" + device.getUuid());
								Log.i(TAG,"Major:" + device.getMajor() + " Minor:" + device.getMinor());
								Log.i(TAG,"Temperature:" + device.getTemprature()+"C");
								Log.i(TAG,"X:" + device.getAccX()+" Y:" + device.getAccY()+" Z:" + device.getAccZ());
						 }
					}

					@Override
					public void onOperate(int opCode) {
						Log.i(TAG,"opCode=" + opCode);
						if (opCode == BeaconController.OP_BEACON_WRITE_START) {
							ToastUtil.showShort(MainActivity.this, "write start");
							showProgress();
						} else if (opCode == BeaconController.OP_BEACON_WRITE_SUCCESS) {
							ToastUtil.showShort(MainActivity.this, "write success");
							closeProgress();
						} else if (opCode == BeaconController.OP_BEACON_RESET_SUCCESS) {
							ToastUtil.showShort(MainActivity.this, "reset success");
							closeProgress();
						}					
						
					}

					@Override
					public void onOperateError(int opCode, String reason) {	
						Log.i(TAG,"opCode=" + opCode);
						if (opCode == BeaconController.OP_BEACON_WRITE_ERROR
								||opCode == BeaconController.OP_SCAN_TIMEOUT){
							ToastUtil.showShort(MainActivity.this, "write fail:" + reason);
							closeProgress();
						}
					
					}});
	
	}
	
	// step4 : Set your uuid,major,minor and other beacon parms here.
	public void doWriteConfigClick(View v) {
		BeaconConfig config = new BeaconConfig();
		config.setmCurrentUUID1("e2c56db5dffb48d2b060d0f5a71096e1");
		config.setmDevicePwd("140611"); //default pwd
		config.setmMajor1(12);
		config.setmMinor1(34);
		BeaconController.getInstance().doWriteConfig(config);
	}
	
	// step5 : Modify the beacon password.
	public void doResetPwdClick(View v) {
		BeaconConfig config = new BeaconConfig();
		config.setmDevicePwd("140612"); //current password
		config.setmDeviceNewPwd("140611"); // new passowrd
		BeaconController.getInstance().modifyDevicePwd(config);
	}
	
	// step6 : Reset beacon to default config.
	public void doResetConfigClick(View v) {
		BeaconConfig config = new BeaconConfig();
		config.setmDevicePwd("140611");
		BeaconController.getInstance().resetBeaconConfig(config);
		
	}
	
	// step7: start scan beacon param.
	public void doScanBeaconParam(View v){
		//BeaconController.getInstance().startScanDevice();
		Intent intent = new Intent(this, ScanActivity.class);  	
		startActivity(intent);  
        //this.finish();  
	}
		
	// step8 : unbind this activity
	@Override
	protected void onDestroy() {
		super.onDestroy();
		BeaconController.getInstance().destroyService();
		BeaconController.getInstance().unbindActiviyPage();
	}
	
	private ProgressDialog progressDialog;

	protected void showProgress() {
		if (progressDialog == null || !progressDialog.isShowing()) {
			progressDialog = ProgressDialog.show(this, null, "write start");
		}
	}

	protected void closeProgress() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
