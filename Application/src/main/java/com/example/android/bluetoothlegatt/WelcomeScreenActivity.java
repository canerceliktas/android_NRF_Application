package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.android.bluetoothlegatt.BluetoothLeService.ACTION_GATT_CONNECTED;

public class WelcomeScreenActivity extends Activity {

    private final static String TAG = DeviceControlActivity.class.getSimpleName();


    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private ProgressDialog scanDialog;

    TextView mConnectedDevice;
    TextView mDeviceAdress;
    TextView mStatus;

    private boolean isReconnect = false;
    private boolean mConnected = false;


    Button mCustomDesign;
    Button mProgramSelect;
    Button mProgramLoad;
    Button mMaintance;
    Button mColorPicker;
    ImageButton mConnect, mDisconnect;
    Button mExit;
    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;

    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();



    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();




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
            if (ACTION_GATT_CONNECTED.equals(action)) {
                if(isReconnect){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mConnected = true;
                mCustomDesign.setEnabled(true);
                mProgramSelect.setEnabled(true);
                mMaintance.setEnabled(true);
                mProgramLoad.setEnabled(true);
                mColorPicker.setEnabled(true);
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                mCustomDesign.setEnabled(false);
                mProgramSelect.setEnabled(false);
                mMaintance.setEnabled(false);
                mProgramLoad.setEnabled(false);
                mColorPicker.setEnabled(false);
                invalidateOptionsMenu();
                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
               // displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));

            }
        }
    };


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        scanDialog = new ProgressDialog(this);

        mConnectedDevice = (TextView) findViewById(R.id.ConnectedDevice_2);
        mDeviceAdress = (TextView) findViewById(R.id.DeviceAddress);
        mStatus = (TextView) findViewById(R.id.ConnectionStatus);

        mCustomDesign = (Button) findViewById(R.id.CustomDesign);
        mConnect = (ImageButton) findViewById(R.id.ConnectButton);
        mDisconnect = (ImageButton) findViewById(R.id.DisconnectButton);

        mProgramSelect = (Button) findViewById(R.id.ProgramSelect);
        mMaintance = (Button) findViewById(R.id.Maintenance);
        mProgramLoad = (Button) findViewById(R.id.LoadCustomProgram);
        mColorPicker = (Button) findViewById(R.id.ColorPicker);
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        scanDialog.setTitle(""+ mDeviceName + " cihazına bağlanıyor.");
        scanDialog.setMessage("Lütfen bekleyin...");
        scanDialog.setCanceledOnTouchOutside(false);
        scanDialog.show();

        mConnectedDevice.setText("Connected Device : " + mDeviceName);
        mDeviceAdress.setText("Device Address : " + mDeviceAddress);


//        if (mConnected){
//            mConnect.setVisibility(View.INVISIBLE);
//            mDisconnect.setVisibility(View.VISIBLE);
//        } else {
//            mConnect.setVisibility(View.VISIBLE);
//            mDisconnect.setVisibility(View.INVISIBLE);
//        }

        mMaintance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Butona basıldığında bunlar çalışacak
                final Intent intent = new Intent(WelcomeScreenActivity.this, DeviceControlActivity.class);
                intent.putExtra(WelcomeScreenActivity.EXTRAS_DEVICE_ADDRESS, mDeviceAddress);
                intent.putExtra(WelcomeScreenActivity.EXTRAS_DEVICE_NAME, mDeviceName);

                String intent_Action = ACTION_GATT_CONNECTED;

                startActivity(intent);
            }
        });

        mCustomDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent_1 = new Intent(WelcomeScreenActivity.this, CustomDesignTab.class);
                intent_1.putExtra(WelcomeScreenActivity.EXTRAS_DEVICE_NAME, mDeviceName);
                intent_1.putExtra(WelcomeScreenActivity.EXTRAS_DEVICE_ADDRESS, mDeviceAddress);
                startActivity(intent_1);
            }
        });

        mProgramLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final Intent intent_2 = new Intent(WelcomeScreenActivity.this, CustomLoadActivity.class);
                intent_2.putExtra(WelcomeScreenActivity.EXTRAS_DEVICE_NAME, mDeviceName);
                intent_2.putExtra(WelcomeScreenActivity.EXTRAS_DEVICE_ADDRESS, mDeviceAddress);

                startActivity(intent_2);
            }
        });

        mColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  Intent intent_3 = new Intent(WelcomeScreenActivity.this, ControllerTab.class);
                intent_3.putExtra(WelcomeScreenActivity.EXTRAS_DEVICE_NAME, mDeviceName);
                intent_3.putExtra(WelcomeScreenActivity.EXTRAS_DEVICE_ADDRESS, mDeviceAddress);

                startActivity(intent_3);
            }
        });

        mConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothLeService.connect(mDeviceAddress);
                scanDialog.setTitle(""+ mDeviceName + " cihazına bağlanıyor.");
                scanDialog.setMessage("Lütfen bekleyin...");
                scanDialog.setCanceledOnTouchOutside(false);
                scanDialog.show();
            }
        });

        mDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothLeService.disconnect();
            }
        });




    }



    private void clearUI() {
//        mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
        isReconnect = true;

    }



    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
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
        mBluetoothLeService.disconnect();
    }

    @Override
    public void onBackPressed() {
        mBluetoothLeService.disconnect();
        super.onBackPressed();

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                scanDialog.setTitle(""+ mDeviceName + " cihazına bağlanıyor.");
                scanDialog.setMessage("Lütfen bekleyin...");
                scanDialog.setCanceledOnTouchOutside(false);
                scanDialog.show();
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
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
                    mStatus.setTextColor(0xFF00FF00);
                    scanDialog.dismiss();
                    mDisconnect.setVisibility(View.VISIBLE);
                    mConnect.setVisibility(View.INVISIBLE);
                }
                else{
                    mConnect.setVisibility(View.VISIBLE);
                    mDisconnect.setVisibility(View.INVISIBLE);

                    mStatus.setTextColor(0xFFFF0000);
                }

                mStatus.setText(resourceId);
            }
        });
    }




    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
