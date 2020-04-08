package com.example.android.bluetoothlegatt;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import static com.example.android.bluetoothlegatt.CustomDesignTab.deviceConfigurationRegister;
import static com.example.android.bluetoothlegatt.CustomDesignTab.lightOneControlRegister;
import static com.example.android.bluetoothlegatt.CustomDesignTab.lightTwoControlRegister;
import static com.example.android.bluetoothlegatt.CustomDesignTab.motorTwoControlRegister;
import static com.example.android.bluetoothlegatt.DCRFragment.mAlarm;
import static com.example.android.bluetoothlegatt.DCRFragment.mDCR;
import static com.example.android.bluetoothlegatt.DCRFragment.mL1CON;
import static com.example.android.bluetoothlegatt.DCRFragment.mL2CON;
import static com.example.android.bluetoothlegatt.DCRFragment.mM2CON;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatementCFGFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatementCFGFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatementCFGFragment extends Fragment {
    View view;
    private Button mLight_1, mLight_2, mMotor_2;
    static Spinner mMotor2first, mMotor2second, mMotor2third,
            mLight1first, mLight1second, mLight1third, mLight2first,
            mLight2second, mLight2third,mLight2PowerOn, mLight1PowerOn, mMotor2PowerOn;

    public StatementCFGFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_statement_configuration, container, false);

        //UI Sets

        mLight_1 = (Button)view.findViewById(R.id.light1);
        mLight_2 = (Button) view.findViewById(R.id.light2);
        mMotor_2 = (Button) view.findViewById(R.id.motor2);




        /*Motor Dropdown List */
        mMotor2PowerOn = (Spinner) view.findViewById(R.id.motor2PowerOn);
        ArrayAdapter<String> mMotor2PowerOnAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.motor_options));
        mMotor2PowerOn.setAdapter(mMotor2PowerOnAdapter);

        mMotor2first = (Spinner) view.findViewById(R.id.motor2firstPress);
        ArrayAdapter<String> mMotor2FirstAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.motor_options));
        mMotor2first.setAdapter(mMotor2FirstAdapter);

        mMotor2second = (Spinner) view.findViewById(R.id.motor2secondPress);
        ArrayAdapter<String> mMotor2SecondAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.motor_options));
        mMotor2second.setAdapter(mMotor2SecondAdapter);

        mMotor2third = (Spinner) view.findViewById(R.id.motor2thirdPress);
        ArrayAdapter<String> mMotor2ThirdAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.motor_options));
        mMotor2third.setAdapter(mMotor2ThirdAdapter);


        /*Light 1 Dropdown List */

        mLight1PowerOn = (Spinner) view.findViewById(R.id.light1PowerOn);
        ArrayAdapter<String> mLight1PowerOnAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.light_options));
        mLight1PowerOn.setAdapter(mLight1PowerOnAdapter);

        mLight1first = (Spinner) view.findViewById(R.id.light1firstPress);
        ArrayAdapter<String> mLight1FirstAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.light_options));
        mLight1first.setAdapter(mLight1FirstAdapter);

        mLight1second = (Spinner) view.findViewById(R.id.light1secondPress);
        ArrayAdapter<String> mLight1SecondAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.light_options));
        mLight1second.setAdapter(mLight1SecondAdapter);

        mLight1third= (Spinner) view.findViewById(R.id.light1thirdPress);
        ArrayAdapter<String> mLight1ThirdAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.light_options));
        mLight1third.setAdapter(mLight1ThirdAdapter);


        /*Light 2 Dropdown List*/

        mLight2PowerOn = (Spinner) view.findViewById(R.id.light2PowerOn);
        ArrayAdapter<String> mLight2PowerOnAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.light_options));
        mLight2PowerOn.setAdapter(mLight2PowerOnAdapter);

        mLight2first = (Spinner) view.findViewById(R.id.light2firstPress);
        ArrayAdapter<String> mLight2FirstAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.light_options));
        mLight2first.setAdapter(mLight2FirstAdapter);

        mLight2second = (Spinner) view.findViewById(R.id.light2secondPress);
        ArrayAdapter<String> mLight2SecondAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.light_options));
        mLight2second.setAdapter(mLight2SecondAdapter);

        mLight2third= (Spinner) view.findViewById(R.id.light2thirdPress);
        ArrayAdapter<String> mLight2ThirdAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.light_options));
        mLight2third.setAdapter(mLight2ThirdAdapter);

        //Make unclikable the Spinners (Light1,Light2,Motor2)
        //CheckSwitches(Light1Check,Light2Check,Motor2Check);

        /* Setting register value*/
        mLight1PowerOn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0: //000
                        lightOneControlRegister &= ~((1<<4) | (1<<3) | (1<<2));

                        break;

                    case 1: //001
                        lightOneControlRegister |= (1<<2); //1 yap
                        lightOneControlRegister &= ~((1<<4)|(1<<3)); //0 yap
                        break;

                    case 2: //010
                        lightOneControlRegister |= (1<<3); //1 yap
                        lightOneControlRegister &= ~((1<<4)|(1<<2)); //0 yap
                        break;
                    case 3: //011
                        lightOneControlRegister |= ((1<<3) | (1<<2)); //1 yap
                        lightOneControlRegister &= ~(1<<4); //0 yap
                        break;
                }
//                mL1CON.setText(""+lightOneControlRegister);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mLight1first.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        lightOneControlRegister &= ~((1<<7) | (1<<6) | (1<<5));

                        break;

                    case 1:
                        lightOneControlRegister |= (1<<5); //1 yap
                        lightOneControlRegister &= ~((1<<6)|(1<<7)); //0 yap
                        break;

                    case 2:
                        lightOneControlRegister |= (1<<6); //1 yap
                        lightOneControlRegister &= ~((1<<7)|(1<<5)); //0 yap
                        break;

                    case 3:
                        lightOneControlRegister |= ((1<<6) | (1<<5)); //1 yap
                        lightOneControlRegister &= ~(1<<7); //0 yap
                        break;
                }
//                mL1CON.setText(""+lightOneControlRegister);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mLight1second.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        lightOneControlRegister &= ~((1<<10) | (1<<9) | (1<<8));

                        break;

                    case 1:
                        lightOneControlRegister |= (1<<8); //1 yap
                        lightOneControlRegister &= ~((1<<10)|(1<<9)); //0 yap
                        break;

                    case 2:
                        lightOneControlRegister |= (1<<9); //1 yap
                        lightOneControlRegister &= ~((1<<10)|(1<<8)); //0 yap
                        break;

                    case 3:
                        lightOneControlRegister |= ((1<<8) | (1<<9)); //1 yap
                        lightOneControlRegister &= ~(1<<10); //0 yap
                        break;
                }
 //               mL1CON.setText(""+lightOneControlRegister);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mLight1third.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        lightOneControlRegister &= ~((1<<13) | (1<<12) | (1<<11));

                        break;

                    case 1:
                        lightOneControlRegister |= (1<<11); //1 yap
                        lightOneControlRegister &= ~((1<<13)|(1<<12)); //0 yap
                        break;

                    case 2:
                        lightOneControlRegister |= (1<<12); //1 yap
                        lightOneControlRegister &= ~((1<<13)|(1<<11)); //0 yap
                        break;

                    case 3:
                        lightOneControlRegister |= ((1<<12) | (1<<11)); //1 yap
                        lightOneControlRegister &= ~(1<<13); //0 yap
                        break;
                }
 //               mL1CON.setText(""+lightOneControlRegister);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mLight2PowerOn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0: //000
                        lightTwoControlRegister &= ~((1<<4) | (1<<3) | (1<<2));

                        break;

                    case 1: //001
                        lightTwoControlRegister |= (1<<2); //1 yap
                        lightTwoControlRegister &= ~((1<<4)|(1<<3)); //0 yap
                        break;

                    case 2: //010
                        lightTwoControlRegister |= (1<<3); //1 yap
                        lightTwoControlRegister &= ~((1<<4)|(1<<2)); //0 yap
                        break;
                    case 3: //011
                        lightTwoControlRegister |= ((1<<3) | (1<<2)); //1 yap
                        lightTwoControlRegister &= ~(1<<4); //0 yap
                        break;
                }
  //              mL2CON.setText(""+lightTwoControlRegister);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mLight2first.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        lightTwoControlRegister &= ~((1<<7) | (1<<6) | (1<<5));

                        break;

                    case 1:
                        lightTwoControlRegister |= (1<<5); //1 yap
                        lightTwoControlRegister &= ~((1<<6)|(1<<7)); //0 yap
                        break;

                    case 2:
                        lightTwoControlRegister |= (1<<6); //1 yap
                        lightTwoControlRegister &= ~((1<<7)|(1<<5)); //0 yap
                        break;

                    case 3:
                        lightTwoControlRegister |= ((1<<6) | (1<<5)); //1 yap
                        lightTwoControlRegister &= ~(1<<7); //0 yap
                        break;
                }
 //               mL2CON.setText(""+lightTwoControlRegister);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mLight2second.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        lightTwoControlRegister &= ~((1<<10) | (1<<9) | (1<<8));

                        break;

                    case 1:
                        lightTwoControlRegister |= (1<<8); //1 yap
                        lightTwoControlRegister &= ~((1<<10)|(1<<9)); //0 yap
                        break;

                    case 2:
                        lightTwoControlRegister |= (1<<9); //1 yap
                        lightTwoControlRegister &= ~((1<<10)|(1<<8)); //0 yap
                        break;

                    case 3:
                        lightTwoControlRegister |= ((1<<8) | (1<<9)); //1 yap
                        lightTwoControlRegister &= ~(1<<10); //0 yap
                        break;
                }
//                mL2CON.setText(""+lightTwoControlRegister);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mLight2third.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        lightTwoControlRegister &= ~((1<<13) | (1<<12) | (1<<11));

                        break;

                    case 1:
                        lightTwoControlRegister |= (1<<11); //1 yap
                        lightTwoControlRegister &= ~((1<<13)|(1<<12)); //0 yap
                        break;

                    case 2:
                        lightTwoControlRegister |= (1<<12); //1 yap
                        lightTwoControlRegister &= ~((1<<13)|(1<<11)); //0 yap
                        break;

                    case 3:
                        lightTwoControlRegister |= ((1<<12) | (1<<11)); //1 yap
                        lightTwoControlRegister &= ~(1<<13); //0 yap
                        break;
                }
  //              mL2CON.setText(""+lightTwoControlRegister);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMotor2PowerOn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        motorTwoControlRegister &= ~((1<<4) | (1<<3) | (1<<2));
                        break;

                    case 1:
                        motorTwoControlRegister |= (1<<2);
                        motorTwoControlRegister &= ~((1<<4) | (1<<3));
                        break;

                    case 2:
                        motorTwoControlRegister |= (1<<3);
                        motorTwoControlRegister &= ~((1<<4) | (1<<2));
                        break;

                    case 3:
                        motorTwoControlRegister &= ~(1<<4);
                        motorTwoControlRegister |= ((1<<3) | (1<<2));
                        break;

                    case 4:
                        motorTwoControlRegister |= (1<<4);
                        motorTwoControlRegister &= ~((1<<3) | (1<<2));
                        break;

                    case 5:
                        motorTwoControlRegister &= ~(1<<3);
                        motorTwoControlRegister |= ((1<<4) | (1<<2));
                        break;
                }
//                mM2CON.setText(""+motorTwoControlRegister);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mMotor2first.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        motorTwoControlRegister &= ~((1<<7) | (1<<6) | (1<<5));
                        break;

                    case 1:
                        motorTwoControlRegister |= (1<<5);
                        motorTwoControlRegister &= ~((1<<7) | (1<<6));
                        break;

                    case 2:
                        motorTwoControlRegister |= (1<<6);
                        motorTwoControlRegister &= ~((1<<7) | (1<<5));
                        break;

                    case 3:
                        motorTwoControlRegister &= ~(1<<7);
                        motorTwoControlRegister |= ((1<<6) | (1<<5));
                        break;

                    case 4:
                        motorTwoControlRegister |= (1<<7);
                        motorTwoControlRegister &= ~((1<<6) | (1<<5));
                        break;

                    case 5:
                        motorTwoControlRegister &= ~(1<<6);
                        motorTwoControlRegister |= ((1<<7) | (1<<5));
                        break;
                }
 //               mM2CON.setText(""+motorTwoControlRegister);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mMotor2second.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        motorTwoControlRegister &= ~((1<<10) | (1<<9) | (1<<8));
                        break;

                    case 1:
                        motorTwoControlRegister |= (1<<8);
                        motorTwoControlRegister &= ~((1<<10) | (1<<9));
                        break;

                    case 2:
                        motorTwoControlRegister |= (1<<9);
                        motorTwoControlRegister &= ~((1<<10) | (1<<8));
                        break;

                    case 3:
                        motorTwoControlRegister &= ~(1<<10);
                        motorTwoControlRegister |= ((1<<9) | (1<<8));
                        break;

                    case 4:
                        motorTwoControlRegister |= (1<<10);
                        motorTwoControlRegister &= ~((1<<9) | (1<<8));
                        break;

                    case 5:
                        motorTwoControlRegister &= ~(1<<9);
                        motorTwoControlRegister |= ((1<<10) | (1<<8));
                        break;
                }
 //               mM2CON.setText(""+motorTwoControlRegister);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mMotor2third.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        motorTwoControlRegister &= ~((1<<13) | (1<<12) | (1<<11));
                        break;

                    case 1:
                        motorTwoControlRegister |= (1<<11);
                        motorTwoControlRegister &= ~((1<<13) | (1<<12));
                        break;

                    case 2:
                        motorTwoControlRegister |= (1<<12);
                        motorTwoControlRegister &= ~((1<<13) | (1<<11));
                        break;

                    case 3:
                        motorTwoControlRegister &= ~(1<<13);
                        motorTwoControlRegister |= ((1<<12) | (1<<11));
                        break;

                    case 4:
                        motorTwoControlRegister |= (1<<13);
                        motorTwoControlRegister &= ~((1<<12) | (1<<11));
                        break;

                    case 5:
                        motorTwoControlRegister &= ~(1<<12);
                        motorTwoControlRegister |= ((1<<13) | (1<<11));
                        break;
                }
   //             mM2CON.setText(""+motorTwoControlRegister);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAlarm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        deviceConfigurationRegister &= ~(1<<4);
                        deviceConfigurationRegister &= ~(1<<5);
                        break;
                    case 1:
                        deviceConfigurationRegister |= (1<<4);
                        deviceConfigurationRegister &= ~(1<<5);
                        break;
                    case 2:
                        deviceConfigurationRegister |= (1<<5);
                        deviceConfigurationRegister &= ~(1<<4);
                        break;
                    case 3:
                        deviceConfigurationRegister |= ((1<<5) | (1<<4));
                        break;

                }
//                mDCR.setText(""+deviceConfigurationRegister);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        return view;
    }

    private void CheckSwitches(boolean Light1Check,boolean Light2Check, boolean Motor2Check ) {
        if (Light1Check){
            mLight1PowerOn.setEnabled(true);
            mLight1first.setEnabled(true);
            mLight1second.setEnabled(true);
            mLight1third.setEnabled(true);
        }
        else {
            mLight1PowerOn.setEnabled(false);
            mLight1first.setEnabled(false);
            mLight1second.setEnabled(false);
            mLight1third.setEnabled(false);
        }

        if (Light2Check){
            mLight2PowerOn.setEnabled(true);
            mLight2first.setEnabled(true);
            mLight2second.setEnabled(true);
            mLight2third.setEnabled(true);
        }
        else {
            mLight2PowerOn.setEnabled(false);
            mLight2first.setEnabled(false);
            mLight2second.setEnabled(false);
            mLight2third.setEnabled(false);
        }

        if (Motor2Check) {
            mMotor2PowerOn.setEnabled(true);
            mMotor2first.setEnabled(true);
            mMotor2second.setEnabled(true);
            mMotor2third.setEnabled(true);
        }
        else {
            mMotor2PowerOn.setEnabled(false);
            mMotor2first.setEnabled(false);
            mMotor2second.setEnabled(false);
            mMotor2third.setEnabled(false);
        }
    }
}
