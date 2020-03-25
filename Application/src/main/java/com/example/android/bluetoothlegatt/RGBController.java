package com.example.android.bluetoothlegatt;

import android.bluetooth.BluetoothGattCharacteristic;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RGBController#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RGBController extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    int mDefaultColor = 0;
    int color;
    String htmlColor;
    byte[] datatoSendbyte ={0x00,0x00,0x00};
    int[] colorRGB;
    int RedValue, GreenValue, BlueValue;
    ColorPickerView mColorPickerView;

    private EditText mRedValue, mGreenValue, mBlueValue;
    private TextView mFinalValue, mColorDecimal;
    private Button mSendButton;
    private GradientDrawable mFinalValueBG;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RGBController() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RGBController.
     */
    // TODO: Rename and change types and number of parameters
    public static RGBController newInstance(String param1, String param2) {
        RGBController fragment = new RGBController();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_colour_palette, container, false);

        mColorPickerView = (ColorPickerView) view.findViewById(R.id.colorPickerView);

                mRedValue = (EditText) view.findViewById(R.id.Red_Value);
        mRedValue.setFilters(new InputFilter[]{new MinMaxFilter("0","255")});//Alabileceği değer 0-255 arası olsun diye filter ekledik

        mGreenValue = (EditText) view.findViewById(R.id.Green_Value);
        mGreenValue.setFilters(new InputFilter[]{new MinMaxFilter("0","255")});

        mBlueValue = (EditText) view.findViewById(R.id.Blue_Value);
        mBlueValue.setFilters(new InputFilter[]{new MinMaxFilter("0","255")});

        mColorDecimal = (TextView) view.findViewById(R.id.color_decimal);
        mFinalValue = (TextView) view.findViewById(R.id.finalColorValue);
        mSendButton = (Button) view.findViewById(R.id.Color_Send_Button);

        mFinalValueBG = (GradientDrawable) mFinalValue.getBackground();

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
               // mFinalValue.setBackgroundColor(color);
                mFinalValueBG.setColor(color);
                mFinalValue.setBackgroundResource(R.drawable.textview_border);

                mColorDecimal.setText(""+mColorPickerView.getSelectedPoint()); //kaldırılacak


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

                color = (255 & 0xff) << 24 | (RedValue & 0xff) << 16 | (GreenValue & 0xff) << 8 | (BlueValue & 0xff);
                mFinalValueBG.setColor(color);

                color = color ^ 0xFF000000;

                String hexColor = Integer.toHexString(color);
                mFinalValue.setText(""+hexColor);

                mFinalValue.setBackgroundResource(R.drawable.textview_border);
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

                color = (255 & 0xff) << 24 | (RedValue & 0xff) << 16 | (GreenValue & 0xff) << 8 | (BlueValue & 0xff);
                mFinalValueBG.setColor(color);

                color = color ^ 0xFF000000;

                String hexColor = Integer.toHexString(color);
                mFinalValue.setText(""+hexColor);

                mFinalValue.setBackgroundResource(R.drawable.textview_border);
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

                color = (255 & 0xff) << 24 | (RedValue & 0xff) << 16 | (GreenValue & 0xff) << 8 | (BlueValue & 0xff);
                mFinalValue.setBackgroundColor(color);

                color = color ^ 0xFF000000;

                String hexColor = Integer.toHexString(color);
                mFinalValue.setText(""+hexColor);

                mFinalValue.setBackgroundResource(R.drawable.textview_border);
            }
        });


        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mGattCharacteristics = ((ControllerTab)getActivity()).getmGattCharacteristics();
                mBluetoothLeService = ((ControllerTab)getActivity()).getmBluetoothLeService();

                try {
                   // mColorPickerView.setSelectorPoint(220,470);
                    //karakteristiğe yazma kısmı buraya alındı. (debug modda çalılıyordu release de çalışmıyıordu)
                    BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(2).get(2);

                    characteristic.setValue(datatoSendbyte);
                    mBluetoothLeService.writeCharacteristic(characteristic);


                } catch (IndexOutOfBoundsException e) {
                    Toast.makeText(getActivity(), "Index Hatası !", Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;

    }
}
