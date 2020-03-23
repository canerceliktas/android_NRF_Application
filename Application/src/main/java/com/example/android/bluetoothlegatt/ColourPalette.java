package com.example.android.bluetoothlegatt;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

import static android.content.ContentValues.TAG;

public class ColourPalette extends AppCompatActivity {
    int mDefaultColor = 0;
    int color;
    String htmlColor;
    byte[] datatoSendbyte ={0x00,0x00,0x00};
    int[] colorRGB;
    int RedValue, GreenValue, BlueValue;
    ColorPickerView mColorPickerView;

    private EditText mRedValue, mGreenValue, mBlueValue;
    private TextView mFinalValue;
    private Button mSendButton;

    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

    private String mDeviceName;
    private String mDeviceAddress;

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
                //updateConnectionState(R.string.connected);
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
        setContentView(R.layout.activity_colour_palette);
        mColorPickerView = (ColorPickerView) findViewById(R.id.colorPickerView);

        mRedValue = (EditText) findViewById(R.id.Red_Value);
        mRedValue.setFilters(new InputFilter[]{new MinMaxFilter("0","255")});//Alabileceği değer 0-255 arası olsun diye filter ekledik

        mGreenValue = (EditText) findViewById(R.id.Green_Value);
        mGreenValue.setFilters(new InputFilter[]{new MinMaxFilter("0","255")});

        mBlueValue = (EditText) findViewById(R.id.Blue_Value);
        mBlueValue.setFilters(new InputFilter[]{new MinMaxFilter("0","255")});


        mFinalValue = (TextView) findViewById(R.id.finalColorValue);
        mSendButton = (Button) findViewById(R.id.Color_Send_Button);
        //openColorPicker();
        final Intent intent = getIntent();
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiseConnection, BIND_AUTO_CREATE);

        RedValue = 0;
        datatoSendbyte[0] = (byte) RedValue;
        GreenValue = 0;
        datatoSendbyte[1] = (byte) GreenValue;
        BlueValue = 0;
        datatoSendbyte[2] = (byte) BlueValue;

        mColorPickerView.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope) {
                color = mColorPickerView.getSavedColor(Color.WHITE);
                htmlColor = mColorPickerView.getSavedColorHtml(Color.WHITE);
                colorRGB = mColorPickerView.getSavedColorRGB(Color.WHITE);
                colorRGB = colorEnvelope.getColorRGB();
                htmlColor = colorEnvelope.getColorHtml();
                color = colorEnvelope.getColor();

                mRedValue.setText("" +colorRGB[0]);
                String change = mRedValue.getText().toString(); //NumberFormatException hatası için. Edittext boş olunca bu hataya düşüyor
                if (change.equals("")){ // detect an empty string and set it to "0" instead
                    change = "0";
                }
                RedValue = Integer.parseInt(change);
               // RedValue = Integer.parseInt(mRedValue.getText().toString());

                mGreenValue.setText(""+colorRGB[1]);
                change = mGreenValue.getText().toString();//NumberFormatException hatası için. Edittext boş olunca bu hataya düşüyor
                if (change.equals("")){ // detect an empty string and set it to "0" instead
                    change = "0";
                }
                GreenValue = Integer.parseInt(change);
                //GreenValue = Integer.parseInt(mGreenValue.getText().toString());

                mBlueValue.setText(""+colorRGB[2]);
                change = mBlueValue.getText().toString();//NumberFormatException hatası için. Edittext boş olunca bu hataya düşüyor
                if (change.equals("")){ // detect an empty string and set it to "0" instead
                    change = "0";
                }
                BlueValue = Integer.parseInt(change);
                //BlueValue = Integer.parseInt(mBlueValue.getText().toString());

                mFinalValue.setText(""+htmlColor);
                mFinalValue.setBackgroundColor(color);


            }
        });

        mRedValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String change = mRedValue.getText().toString(); //NumberFormatException hatası için. Edittext boş olunca bu hataya düşüyor
                if (change.equals("")){ // detect an empty string and set it to "0" instead
                    change = "0";
                }
                RedValue = Integer.parseInt(change);
                datatoSendbyte[0] = (byte) RedValue;

//                color = (byte) (65535 * RedValue + 255 * GreenValue + BlueValue);
//                mFinalValue.setBackgroundColor(color);
            }
        });

        mGreenValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String change = mGreenValue.getText().toString();//NumberFormatException hatası için. Edittext boş olunca bu hataya düşüyor
                if (change.equals("")){ // detect an empty string and set it to "0" instead
                    change = "0";
                }
                GreenValue = Integer.parseInt(change);
                datatoSendbyte[1] = (byte) GreenValue;

//                color = 0xFFFF * RedValue + 0xFF * GreenValue + BlueValue;
//                mFinalValue.setBackgroundColor(color);
            }
        });

        mBlueValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String change = mBlueValue.getText().toString();//NumberFormatException hatası için. Edittext boş olunca bu hataya düşüyor
                if (change.equals("")){ // detect an empty string and set it to "0" instead
                    change = "0";
                }
                BlueValue = Integer.parseInt(change);
                datatoSendbyte[2] = (byte) BlueValue;

//                color = 0xFFFF * RedValue + 0xFF * GreenValue + BlueValue;
//                mFinalValue.setBackgroundColor(color);
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //karakteristiğe yazma kısmı buraya alındı. (debug modda çalılıyordu release de çalışmıyıordu)
                    BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(2).get(2);

                    characteristic.setValue(datatoSendbyte);
                    mBluetoothLeService.writeCharacteristic(characteristic);


                } catch (IndexOutOfBoundsException e) {
                    Toast.makeText(ColourPalette.this, "Index Hatası !", Toast.LENGTH_LONG).show();
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

                }
                else{

                }


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
