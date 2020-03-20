/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static android.view.View.INVISIBLE;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity {


    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private TextView mConnectionState;
    private TextView mDataField;
    private TextView mTransmitterError;
    private TextView mSensorData;
    private Button mSendButton;


    private String mDeviceName;
    private String mDeviceAddress;
    private ExpandableListView mGattServicesList;

    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothGattCharacteristic mWriteCharacteristic;
    private UUID _charProp;

    private boolean stopflag;
    private boolean isReconnect = false;
    private boolean trmClick = false;
    private int SensorData;

    byte dataToSend[] = null;

    private  ImageView sensorImage;
    private  ImageView motorStopView, mCell1, mCell2; //motorun durduğunu göster
    private TextView motorStatus;
    private Spinner spinner1;

    public ArrayList<BluetoothGattCharacteristic> readable_chars;
    public BluetoothGattCharacteristic sensor_data_characteristic;
    public BluetoothGattCharacteristic trm_error_characteristic;
    public static BluetoothDevice device;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                if(isReconnect){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mConnected = true;
                updateConnectionState(R.string.connected);
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                //mConnected = false;
                //updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                //clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA),intent.getStringExtra(BluetoothLeService.EXTRA_DATA_UUID));


            }
        }
    };


    private void clearUI() {
        mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
        mTransmitterError.setText(R.string.no_data);
        mSensorData.setText(R.string.no_data);
        isReconnect = true; //disconnect yaparken çökmesin

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //BluetoothGatt bluetoothGatt = (BluetoothGatt) device.connectGatt(this, false, mGattCallback);
        setContentView(R.layout.gatt_services_characteristics);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        // Sets up UI references.
        mConnectionState = (TextView) findViewById(R.id.connection_state);
        mTransmitterError = (TextView) findViewById(R.id.mTransmitterError);
        mSensorData = (TextView) findViewById(R.id.mSensorData);

        sensorImage = (ImageView) findViewById(R.id.SensorView);


        motorStopView = (ImageView)findViewById(R.id.motorStopView);
        motorStatus = (TextView) findViewById(R.id.mMotorStatus);

        mCell1 = (ImageView) findViewById(R.id.imageView4);
        mCell2 = (ImageView) findViewById(R.id.CELL2);

        motorStopView.setImageResource(R.drawable.motor_stop);
        sensorImage.setVisibility(INVISIBLE);
        motorStatus.setTextColor(0xFFFF0000);
        motorStatus.setText("Motor has stopped!");




        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);



    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
//           -

        }
    }

    @Override
    protected void onPause() {
//        timerTask.cancel();
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        menu.findItem(R.id.menu_connect).setVisible(false);
        menu.findItem(R.id.menu_disconnect).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                //timerTask.cancel();
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mConnected){
                    mConnectionState.setTextColor(0xFF00FF00);
                }
                else{
                    mConnectionState.setTextColor(0xFFFF0000);
                }

                mConnectionState.setText(resourceId);
            }
        });
    }

    private void displayData(String data, String characteristic) {
        if (data != null && characteristic != null) {
           // data = data.substring(data.indexOf('\n')+1).trim();
            if (characteristic.equals( "aee4beeb-91d2-11e9-bc42-52a6f7764f64") ){
                mTransmitterError.setText(data);
            }
            else if (characteristic.equals("aee4beea-91d2-11e9-bc42-52a6f7764f64")){ // veriler karışmasın
                mSensorData.setText(data);
               SensorData = Integer.parseInt(data);
                if(SensorData<=729 && SensorData !=0)
                {

                    motorStopView.setImageResource(R.drawable.motor_stop);
                    sensorImage.setVisibility(INVISIBLE);
                    motorStatus.setTextColor(0xFFFF0000);
                    motorStatus.setText("Motor has stopped!");
                    stopflag=true;


                }
                else {


                    sensorImage.setVisibility(View.VISIBLE);
                    motorStatus.setTextColor(0xFF00FF00);
                    motorStatus.setText("Motor is running!");
                    stopflag=false;
                }

            }
        }
    }

    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
        boolean flag = false;
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            String ServiceName = SampleGattAttributes.lookup(uuid, unknownServiceString);
            if(ServiceName != unknownServiceString) {  //service name "unknown service" değil ise ekrana yazdır
                currentServiceData.put(
                        LIST_NAME, ServiceName);
                currentServiceData.put(LIST_UUID, uuid);
                gattServiceData.add(currentServiceData);

                ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                        new ArrayList<HashMap<String, String>>();
                List<BluetoothGattCharacteristic> gattCharacteristics =
                        gattService.getCharacteristics();
                ArrayList<BluetoothGattCharacteristic> charas =
                        new ArrayList<BluetoothGattCharacteristic>();

                // Loops through available Characteristics.
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    charas.add(gattCharacteristic);
                    HashMap<String, String> currentCharaData = new HashMap<String, String>();
                    uuid = gattCharacteristic.getUuid().toString();
                    currentCharaData.put(
                            LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                    currentCharaData.put(LIST_UUID, uuid);
                    gattCharacteristicGroupData.add(currentCharaData);
                }
                mGattCharacteristics.add(charas);
                readable_chars = mGattCharacteristics.get(0);
                sensor_data_characteristic = readable_chars.get(0);
                trm_error_characteristic = readable_chars.get(1);


                gattCharacteristicData.add(gattCharacteristicGroupData);

                mBluetoothLeService.setCharacteristicNotification(sensor_data_characteristic,true);
                mBluetoothLeService.setCharacteristicNotification(trm_error_characteristic,true);


            }
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private void setCharacteristicNotification() {

    }
}
