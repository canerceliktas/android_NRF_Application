package com.example.android.bluetoothlegatt;

import android.app.Activity;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CustomLoadActivity extends Activity {

    private Button mLoadButton, mDeleteButton;
    private ListView mSavedCFGListView;
    private TextView mConnectedDevice;
    private TextView mConnectionState;
    public String[] savedFiles;

    private String mDeviceName;
    private String mDeviceAddress;
    private String saveId;

    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

    byte dataToSend[] = null;

    private View viewOld;

    ArrayAdapter<String> savedFilesAdapter;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private boolean isReconnect = false;
    private boolean mConnected = false;


    private final ServiceConnection mServiseConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG,"Unable to initialize Bluetooth");
                finish();
            }
            //Automatically connects to device upon succesful start-up initialization
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
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
                getGattServices(mBluetoothLeService.getSupportedGattServices());
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                //mConnected = false;
                //updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                //clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                getGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

                //displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_load);

        final Intent intent = getIntent();
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);



        //SET UI
        mLoadButton = (Button) findViewById(R.id.loadButton_2);
        mDeleteButton = (Button) findViewById(R.id.deleteButton);

        mConnectedDevice = (TextView) findViewById(R.id.ConnectedDevice_2);
        mConnectionState = (TextView) findViewById(R.id.ConnectedStatus);
        mSavedCFGListView = (ListView) findViewById(R.id.savedCfgListView);

        savedFiles = getApplicationContext().fileList(); //kayıtlı dosyaları al

        savedFilesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, savedFiles);

        mSavedCFGListView.setAdapter(savedFilesAdapter);
        mSavedCFGListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //mSavedCFGListView.setSelector(R.drawable.state_selector);


        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiseConnection, BIND_AUTO_CREATE);


        mConnectedDevice.setText("Connected Device : " + mDeviceName);

        mSavedCFGListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                if (viewOld != null) {
//                    viewOld.setBackgroundColor(Color.WHITE);
//                }
                saveId = parent.getItemAtPosition(position).toString();
//                view.setBackgroundColor(Color.LTGRAY);
//                viewOld = view;
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveId != null)
                    getApplicationContext().deleteFile(saveId);
                getApplicationContext().deleteFile("instant-run");
                //savedFilesAdapter.notifyDataSetChanged(); //herhangi bir değişiklikte listview refresh ediyor
                reloadAllData();

            }
        });

        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = load(saveId);
                String index_0 = data.substring(1,data.indexOf("]"));
                String[] index_1 = index_0.split(", ");
                byte datatoSendbyte[];
                int datatoSend[] = new int[5];
                int mDCR = Integer.parseInt(index_1[0]);
                int mL1CON = Integer.parseInt(index_1[1]);
                int mL2CON = Integer.parseInt(index_1[2]);
                int mM1CON = Integer.parseInt(index_1[3]);
                int mM2CON = Integer.parseInt(index_1[4]);


                byte[] dataDCR = longToBytes(mDCR);
//                byte[] dataL1CON = longToBytes(mL1CON);
//                byte[] dataL2CON = longToBytes(mL2CON);
//                byte[] dataM1CON = longToBytes(mM1CON);
//                byte[] dataM2CON = longToBytes(mM2CON);

                datatoSend[0] =  mDCR;
                datatoSend[1] =  mL1CON;
                datatoSend[2] =  mL2CON;
                datatoSend[3] =  mM1CON;
                datatoSend[4] =  mM2CON;
                datatoSendbyte = int2byte(datatoSend);
                mDCR = mDCR;

                try {
                    //karakteristiğe yazma kısmı buraya alındı. (debug modda çalılıyordu release de çalışmıyıordu)

                    BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(2).get(0);

                    characteristic.setValue(datatoSendbyte);
                    mBluetoothLeService.writeCharacteristic(characteristic);


                } catch (IndexOutOfBoundsException e) {
                    Toast.makeText(CustomLoadActivity.this, "Index Hatası !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService !=null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG,"Connect request result =" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiseConnection);
        mBluetoothLeService = null;
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

    private void getGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        String uuid = null;
        String unknownServiceString = "Unknown service";
        String unknownCharacteristicString = "Unknown characteristic";
        ArrayList<HashMap<String,String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String,String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();

        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
        boolean flag = false;
        //Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            String ServiceName = SampleGattAttributes.lookup(uuid, unknownServiceString);
            if (ServiceName != unknownServiceString) { //servis adı uknown değilse
                currentServiceData.put(LIST_NAME, ServiceName);
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
                    currentCharaData.put(LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharacteristicString));
                    currentCharaData.put(LIST_UUID, uuid);
                    gattCharacteristicGroupData.add(currentCharaData);
                }
                mGattCharacteristics.add(charas);
                gattCharacteristicData.add(gattCharacteristicGroupData);
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

    private void reloadAllData(){
        savedFiles = getApplicationContext().fileList(); //kayıtlı dosyaları al

        savedFilesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, savedFiles);

        mSavedCFGListView.setAdapter(savedFilesAdapter);
        mSavedCFGListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }

    private String load(String file_name) {
        String temp = null;
        try {
            FileInputStream fis = openFileInput(file_name); //file ı aç
            temp="";
            int i;
            while((i=fis.read())!= -1) {
                temp = temp + Character.toString((char)i); //file da bulunan datayı temp stringine at
            }
            //registerValue = Integer.parseInt(sbuffer.toString());
            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public byte[] longToBytes(int x) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(x);
        return buffer.array();
    }

    public static byte[] int2byte (int[]src) {
        int srcLength = src.length;
        byte[] dst = new byte[srcLength<<2];

        for (int i=0; i<srcLength; i++) {
            int x = src[i];
            int j = i << 2;
            dst[j++] = (byte) ((x >>> 0) & 0xff);
            dst[j++] = (byte) ((x >>> 8) & 0xff);
            dst[j++] = (byte) ((x >>> 16) & 0xff);
            dst[j++] = (byte) ((x >>> 24) & 0xff);
        }
        return dst;
    }
}
