package com.skharimah.beaconsreader;

import android.bluetooth.BluetoothAdapter;

import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanRecord;

import java.util.List;
import java.util.Vector;

import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconType;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconUtils;
import uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconDevice;

public class BluetoothActivity extends AppCompatActivity {

    // Get user information from login page
    protected Intent intent;
    private String userEmailAddress;
    public static final String EXTRA_MESSAGE = "user_email";
    // Get an instance of device's BT adapter & LE scanner
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothScanner;
    // RSSI Threshold value to determine which beacon is closest to user
    private int rssiThreshold = -60;
    // False if iBeacon meeting the rssi threshold hasn't been found
    private boolean foundIBeacon = false;
    // Vector to store iBeacon device
    Vector<IBeaconDevice> iBeaconDevices = new Vector();
    /* Debugging purposes */
    public static final String TAG = BluetoothActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        // Get an instance of device's BT adapter & scanner
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothScanner = mBluetoothAdapter.getBluetoothLeScanner();
        // Get user email address from previous activity
        intent = getIntent();
        userEmailAddress = intent.getStringExtra(EXTRA_MESSAGE);
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
        Log.i(TAG, userEmailAddress); // prints out user email address from previous activity
    }
    */

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
        if(!foundIBeacon) {
            // Start scanning BLE devices
            Log.i(TAG, "Starting scan");
            mBluetoothScanner.startScan(mScanCallback);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBluetoothScanner.stopScan(mScanCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBluetoothScanner.stopScan(mScanCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothScanner.stopScan(mScanCallback);
    }

    @Override
    public void onBackPressed() {
        // Disable back button
    }

    protected String getClosestIBeaconUUID (IBeaconDevice device) {
        return device.getUUID();
    }

    protected String getClosestIBeaconMajor (IBeaconDevice device) {
        return Integer.toString(device.getMajor());
    }

    protected String getClosestIBeaconMinor (IBeaconDevice device) {
        return Integer.toString(device.getMinor());
    }

    protected ScanCallback mScanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            ScanRecord mScanRecord = result.getScanRecord();
            // Prints out devices name, MAC address, RSSI value
            int rssi = result.getRssi();
            // Create instance of BLE device
            final BluetoothLeDevice bleDevice = new BluetoothLeDevice(result.getDevice(), rssi, mScanRecord.getBytes(), System.currentTimeMillis());
//            Log.i("Device name        ", bleDevice.getName());
//            Log.i("Device MAC Address ", bleDevice.getAddress());
//            Log.i("Device RSSI value  ", Integer.toString(rssi));
            // Check if BLE device scanned is an IBEACON
            // using Bluetooth LE Library for Android
            if(BeaconUtils.getBeaconType(bleDevice) == BeaconType.IBEACON) {
                final IBeaconDevice iBeacon = new IBeaconDevice(bleDevice);
                if(rssi > rssiThreshold) {
                    // If the device meets the rssi threshold
                    foundIBeacon = true;
                    // Pass user email & closest beacon info to next activity
                    Intent newIntent = new Intent(BluetoothActivity.this, ApiCommunication.class);
                    newIntent.putExtra(ApiCommunication.USER_EMAIL, userEmailAddress);
                    newIntent.putExtra(ApiCommunication.IBEACON_UUID, getClosestIBeaconUUID(iBeacon));
                    newIntent.putExtra(ApiCommunication.IBEACON_MAJOR, getClosestIBeaconMajor(iBeacon));
                    newIntent.putExtra(ApiCommunication.IBEACON_MINOR, getClosestIBeaconMinor(iBeacon));
                    startActivity(newIntent);
                }
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };
}
