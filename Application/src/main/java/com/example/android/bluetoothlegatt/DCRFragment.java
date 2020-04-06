package com.example.android.bluetoothlegatt;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


import static com.example.android.bluetoothlegatt.CustomDesignTab.deviceConfigurationRegister;
import static com.example.android.bluetoothlegatt.CustomDesignTab.lightOneControlRegister;
import static com.example.android.bluetoothlegatt.CustomDesignTab.lightTwoControlRegister;
import static com.example.android.bluetoothlegatt.CustomDesignTab.motorOneControlRegister;
import static com.example.android.bluetoothlegatt.CustomDesignTab.motorTwoControlRegister;
import static com.example.android.bluetoothlegatt.CustomDesignTab.registerValue;


public class DCRFragment extends Fragment {

    static TextView mRegisterValue;
    static TextView mM1CON;
    static TextView mM2CON;
    static TextView mL1CON;
    static TextView mL2CON;
    static TextView mDCR;
    static  TextView mDelayValue;
    private Switch mLight_1, mLight_2, mMotor_2,mMotor_1;
    static Spinner mAlarm, mDelay;
    private CheckBox mRearPedal,mMotor1SS,mMotor2SS;
    private Button mSaveButton, mSendButton;
    private EditText mSaveName;

    private SeekBar mDelayBar;

    private double[] delay_values = {0,0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5, 5.5, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5, 10 };

    static boolean Light1Check, Light2Check, Motor2Check, Motor1Check;

    private double Delay;

    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

//    private BluetoothLeService mBluetoothLeService;
//    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
//            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
//
//
//    private String mDeviceName;
//    private String mDeviceAddress;
//
//    private boolean isReconnect = false;
//    private boolean mConnected = false;
//
//
    private static String FILE_NAME;
//
//    private final String LIST_NAME = "NAME";
//    private final String LIST_UUID = "UUID";





//    private final ServiceConnection mServiseConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
//            if (!mBluetoothLeService.initialize()) {
//                Log.e(TAG,"Unable to initialize Bluetooth");
//               getActivity().finish();
//            }
//            //Automatically connects to device upon succesful start-up initialization
//            mBluetoothLeService.connect(mDeviceAddress);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mBluetoothLeService = null;
//        }
//    };
//
//    // Handles various events fired by the Service.
//    // ACTION_GATT_CONNECTED: connected to a GATT server.
//    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
//    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
//    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
//    //                        or notification operations.
//    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            final String action = intent.getAction();
//            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
//                if(isReconnect){
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                mConnected = true;
//                updateConnectionState(R.string.connected);
//                getGattServices(mBluetoothLeService.getSupportedGattServices());
//                //invalidateOptionsMenu();
//            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
//                //mConnected = false;
//                //updateConnectionState(R.string.disconnected);
//                //invalidateOptionsMenu();
//                //clearUI();
//            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
//                // Show all the supported services and characteristics on the user interface.
//                getGattServices(mBluetoothLeService.getSupportedGattServices());
//            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//
//                //displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
//
//            }
//        }
//    };




    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_device_configuration, container, false);

        //UI Sets
        mRegisterValue = (TextView) view.findViewById(R.id.RegisterValue);
        mM1CON = (TextView) view.findViewById(R.id.M1CON);
        mM2CON = (TextView) view.findViewById(R.id.M2CON);
        mL1CON = (TextView) view.findViewById(R.id.L1CON);
        mL2CON = (TextView) view.findViewById(R.id.L2CON);
        mDCR   = (TextView) view.findViewById(R.id.DCR);
        mDelayValue = (TextView) view.findViewById(R.id.delayValue);

        mSaveButton = (Button) view.findViewById(R.id.saveButton);
        mSendButton = (Button) view.findViewById(R.id.sendButton);

        mSaveName = (EditText) view.findViewById(R.id.saveName);


        mRearPedal = (CheckBox) view.findViewById(R.id.fswitch);
        mMotor1SS = (CheckBox) view.findViewById(R.id.m1soft);
        mMotor2SS = (CheckBox) view.findViewById(R.id.m2soft);

        mLight_1 = (Switch) view.findViewById(R.id.light1);
        mLight_2 = (Switch) view.findViewById(R.id.light2);
        mMotor_1 = (Switch) view.findViewById(R.id.motor1);
        mMotor_2 = (Switch) view.findViewById(R.id.motor);

        mRegisterValue.setText("0");
        mM1CON.setText("0");
        mM2CON.setText("0");
        mL1CON .setText("0");
        mL2CON.setText("0");
        mDCR.setText("0");

//        mDeviceName = getActivity().getIntent().getStringExtra(EXTRAS_DEVICE_NAME);
//        mDeviceAddress = getActivity().getIntent().getStringExtra(EXTRAS_DEVICE_ADDRESS);


        mAlarm = (Spinner) view.findViewById(R.id.alarm);
        ArrayAdapter<String> mAlarmAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.alarm_options));
        mAlarm.setAdapter(mAlarmAdapter);

        mDelayBar = (SeekBar) view.findViewById(R.id.delaySeekBar);
        mDelayBar.setProgress(0);
        mDelayValue.setText("Delay(ms):" +Delay);
        mDelayBar.setMax(20); //10 yapınca 5 e kadar alıyor, 20 yapınca dizideki en son elemana kadar aldı

//        mGattCharacteristics = ((CustomDesignTab)getActivity()).getmGattCharacteristics();
//        mBluetoothLeService = ((CustomDesignTab) getActivity()).getmBluetoothLeService();

        mDelayBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Delay = delay_values[seekBar.getProgress()];
                    mDelayValue.setText("Delay(ms):" +Delay);

                    long DCRtemp = 0;
                    double value = Delay*2;
                    switch ((int)value) {
                        case 0: //0
                            deviceConfigurationRegister &= ~((1<<7)|((1<<8))|(1<<9)|(1<<10)|(1<<11));
                            break;
                        case 1: //0.5
                            deviceConfigurationRegister |= (1<<7);
                            deviceConfigurationRegister &= ~(((1<<8))|(1<<9)|(1<<10)|(1<<11));
                            break;
                        case 2: //1
                            deviceConfigurationRegister |= (1<<8);
                            deviceConfigurationRegister &= ~(((1<<7))|(1<<9)|(1<<10)|(1<<11));
                            break;
                        case 3: //1.5
                            deviceConfigurationRegister |= ((1<<8)|(1<<7));
                            deviceConfigurationRegister &= ~((1<<9)|(1<<10)|(1<<11));
                            break;
                        case 4: //2
                            deviceConfigurationRegister |= (1<<9);
                            deviceConfigurationRegister &= ~(((1<<7))|(1<<8)|(1<<10)|(1<<11));
                            break;
                        case 5: //2.5
                            deviceConfigurationRegister |= ((1<<9)|(1<<7));
                            deviceConfigurationRegister &= ~(((1<<8))|(1<<10)|(1<<11));
                            break;
                        case 6: //3
                            deviceConfigurationRegister |= ((1<<9)|(1<<8));
                            deviceConfigurationRegister &= ~(((1<<7))|(1<<10)|(1<<11));
                            break;
                        case 7: //3.5
                            deviceConfigurationRegister |= ((1<<9)|(1<<8)|(1<<7));
                            deviceConfigurationRegister &= ~((1<<10)|(1<<11));
                            break;
                        case 8: //4
                            deviceConfigurationRegister |= (1<<10);
                            deviceConfigurationRegister &= ~(((1<<7))|(1<<8)|(1<<11));
                            break;
                        case 9: //4.5
                            deviceConfigurationRegister |= ((1<<10)|(1<<7));
                            deviceConfigurationRegister &= ~(((1<<8))|(1<<11)|(1<<9));
                            break;
                        case 10: //5
                            deviceConfigurationRegister |= ((1<<10)|(1<<8));
                            deviceConfigurationRegister &= ~(((1<<7))|(1<<9)|(1<<11));
                            break;
                        case 11: //5.5
                            deviceConfigurationRegister |= ((1<<10)|(1<<8)|(1<<7));
                            deviceConfigurationRegister &= ~((1<<9)|(1<<11));
                            break;
                        case 12: //6
                            deviceConfigurationRegister |= ((1<<10)|(1<<9));
                            deviceConfigurationRegister &= ~(((1<<7))|(1<<8)|(1<<11));
                            break;
                        case 13: //6.5
                            deviceConfigurationRegister |= ((1<<10)|(1<<9)|(1<<7));
                            deviceConfigurationRegister &= ~((1<<8)|(1<<11));
                            break;
                        case 14: //7
                            deviceConfigurationRegister |= ((1<<10)|(1<<9)|(1<<8));
                            deviceConfigurationRegister &= ~((1<<7)|(1<<11));
                            break;
                        case 15: //7.5
                            deviceConfigurationRegister |= ((1<<10)|(1<<9)|(1<<8)|(1<<7));
                            deviceConfigurationRegister &= ~((1<<11));
                            break;
                        case 16: //8
                            deviceConfigurationRegister |= (1<<11);
                            deviceConfigurationRegister &= ~(((1<<8))|(1<<9)|(1<<10)|(1<<7));
                            break;
                        case 17: //8.5
                            deviceConfigurationRegister |= ((1<<7)|(1<<11));
                            deviceConfigurationRegister &= ~(((1<<8))|(1<<9)|(1<<10));
                            break;
                        case 18: //9
                            deviceConfigurationRegister |= ((1<<8)|(1<<11));
                            deviceConfigurationRegister &= ~(((1<<7))|(1<<9)|(1<<10));
                            break;
                        case 19: //9.5
                            deviceConfigurationRegister |= ((1<<7)|(1<<8)|(1<<11));
                            deviceConfigurationRegister &= ~((1<<9)|(1<<10));
                            break;
                        case 20: //10
                            deviceConfigurationRegister |= ((1<<9)|(1<<11));
                            deviceConfigurationRegister &= ~(((1<<7))|(1<<8)|(1<<10));
                            break;

                    }
                    mDCR.setText(""+deviceConfigurationRegister);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




//        mDelay = (Spinner) view.findViewById(R.id.delaySpinner);
//        ArrayAdapter<String> mDelayAdapter = new ArrayAdapter<String>(view.getContext(),
//                android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.delays));
//        mDelay.setAdapter(mDelayAdapter);



        //byte byteValue = (byte) registerValue;
        mM1CON.setText(""+motorOneControlRegister);
        //mRegisterValue.setText(""+registerValue);


        mMotor1SS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mMotor1SS.isChecked()) {
                    motorOneControlRegister |= (1<<1);
                }
                else {
                    motorOneControlRegister &= ~(1<<1);
                }
                mM1CON.setText(""+motorOneControlRegister);
            }
        });

        mMotor2SS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mMotor2SS.isChecked()) {
                    motorTwoControlRegister |= (1<<1);
                }
                else {
                    motorTwoControlRegister &= ~(1<<1);
                }
                mM2CON.setText(""+motorTwoControlRegister);
            }
        });

        mRearPedal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mRearPedal.isChecked()) {
                    deviceConfigurationRegister |= (1<<2);
                }
                else {
                    deviceConfigurationRegister &= ~(1<<2);
                }
                mDCR.setText(""+deviceConfigurationRegister);
            }
        });

        mLight_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mLight_1.isChecked()) {
                    Light1Check = true;
                    lightOneControlRegister |= (1<<0);
                }
                else {
                    Light1Check = false;
                    lightOneControlRegister &= ~(1<<0);
                }
                mL1CON.setText(""+lightOneControlRegister);
            }
        });

        mLight_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mLight_2.isChecked()) {
                    Light2Check = true;
                    lightTwoControlRegister |= (1<<0);
                }
                else {
                    Light2Check = false;
                    lightTwoControlRegister &= ~(1<<0);
                }
                mL2CON.setText(""+lightTwoControlRegister);
            }
        });

        mMotor_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mMotor_2.isChecked()) {
                    Motor2Check = true;
                    motorTwoControlRegister |= (1<<0);
                }
                else {
                    Motor2Check = false;
                    motorTwoControlRegister &= ~(1<<0);
                }
                mM2CON.setText(""+motorTwoControlRegister);
            }
        });

        mMotor_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mMotor_1.isChecked()){
                    Motor1Check = true;
                    motorOneControlRegister |= (1<<0);
                }
                else {
                    Motor1Check = false;
                    motorOneControlRegister &= ~(1<<0);
                }
                mM1CON.setText(""+motorOneControlRegister);

            }
        });


        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FILE_NAME = mSaveName.getText().toString();
//                registerValue = deviceVersionRegister + deviceConfigurationRegister +
//                        motorOneControlRegister + motorTwoControlRegister + lightOneControlRegister +
//                        lightTwoControlRegister;

                //registerValue[0] = deviceVersionRegister;
                registerValue[0] = deviceConfigurationRegister;
                registerValue[4] = motorTwoControlRegister;
                registerValue[3] = motorOneControlRegister;
                registerValue[2] = lightTwoControlRegister;
                registerValue[1] = lightOneControlRegister;
                try {
                    FileOutputStream fos = getActivity().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                    String str1 = Arrays.toString(registerValue);
                    fos.write(str1.getBytes());
                    //fos.write(registerValue);
                    fos.close();
                    mSaveName.getText().clear();
                    mRegisterValue.setText("" + str1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerValue[0] = deviceConfigurationRegister;
                registerValue[4] = motorTwoControlRegister;
                registerValue[3] = motorOneControlRegister;
                registerValue[2] = lightTwoControlRegister;
                registerValue[1] = lightOneControlRegister;

                //BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(2).get(0);
                byte[] dataToSend = int2byte(registerValue);
                mGattCharacteristics = ((CustomDesignTab)getActivity()).getmGattCharacteristics();
                mBluetoothLeService = ((CustomDesignTab)getActivity()).getmBluetoothLeService();

                try {
                    //karakteristiğe yazma kısmı buraya alındı. (debug modda çalılıyordu release de çalışmıyıordu)

                    BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(2).get(0);

                    characteristic.setValue(dataToSend);
                    mBluetoothLeService.writeCharacteristic(characteristic);


                } catch (IndexOutOfBoundsException e) {
                    Toast.makeText(getActivity(), "Index Hatası !", Toast.LENGTH_LONG).show();
                }
            }
        });



        return view;
    }

    public DCRFragment() {


    }

//    private void updateConnectionState(final int resourceId) {
////        runOnUiThread(new Runnable() {
////            @Override
////            public void run() {
////                if (mConnected){
////                    mConnectionState.setTextColor(0xFF00FF00);
////                }
////                else{
////                    mConnectionState.setTextColor(0xFFFF0000);
////                }
////
////                mConnectionState.setText(resourceId);
////            }
////        });
//    }
//
//    private void getGattServices(List<BluetoothGattService> gattServices) {
//        if (gattServices == null)
//            return;
//        String uuid = null;
//        String unknownServiceString = "Unknown service";
//        String unknownCharacteristicString = "Unknown characteristic";
//        ArrayList<HashMap<String,String>> gattServiceData = new ArrayList<HashMap<String, String>>();
//        ArrayList<ArrayList<HashMap<String,String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
//
//        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
//        boolean flag = false;
//        //Loops through available GATT Services.
//        for (BluetoothGattService gattService : gattServices) {
//            HashMap<String, String> currentServiceData = new HashMap<String, String>();
//            uuid = gattService.getUuid().toString();
//            String ServiceName = SampleGattAttributes.lookup(uuid, unknownServiceString);
//            if (ServiceName != unknownServiceString) { //servis adı uknown değilse
//                currentServiceData.put(LIST_NAME, ServiceName);
//                currentServiceData.put(LIST_UUID, uuid);
//                gattServiceData.add(currentServiceData);
//
//
//                ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
//                        new ArrayList<HashMap<String, String>>();
//                List<BluetoothGattCharacteristic> gattCharacteristics =
//                        gattService.getCharacteristics();
//                ArrayList<BluetoothGattCharacteristic> charas =
//                        new ArrayList<BluetoothGattCharacteristic>();
//
//                // Loops through available Characteristics.
//                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
//                    charas.add(gattCharacteristic);
//                    HashMap<String, String> currentCharaData = new HashMap<String, String>();
//                    uuid = gattCharacteristic.getUuid().toString();
//                    currentCharaData.put(LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharacteristicString));
//                    currentCharaData.put(LIST_UUID, uuid);
//                    gattCharacteristicGroupData.add(currentCharaData);
//                }
//                mGattCharacteristics.add(charas);
//                gattCharacteristicData.add(gattCharacteristicGroupData);
//            }
//        }
//    }
//
//    private static IntentFilter makeGattUpdateIntentFilter() {
//        final IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
//        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
//        return intentFilter;
//    }


    public void setmBluetoothLeService(BluetoothLeService mBluetoothLeService) {
        this.mBluetoothLeService = mBluetoothLeService;
    }

    public void setmGattCharacteristics(ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics) {
        this.mGattCharacteristics = mGattCharacteristics;
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
