package com.example.android.bluetoothlegatt;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerView;

import yuku.ambilwarna.AmbilWarnaDialog;

public class ColourPalette extends AppCompatActivity {
    int mDefaultColor = 0;
    int color;
    String htmlColor;
    int[] colorRGB;
    ColorPickerView mColorPickerView;

    private TextView mRedValue, mGreenValue, mBlueValue,mFinalValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colour_palette);
        mColorPickerView = (ColorPickerView) findViewById(R.id.colorPickerView);
        mRedValue = (TextView) findViewById(R.id.Red_Value);
        mGreenValue = (TextView) findViewById(R.id.Green_Value);
        mBlueValue = (TextView) findViewById(R.id.Blue_Value);
        mFinalValue = (TextView) findViewById(R.id.finalColorValue);
        //openColorPicker();
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

                mGreenValue.setText(""+colorRGB[1]);

                mBlueValue.setText(""+colorRGB[2]);

                mFinalValue.setText(""+htmlColor);
                mFinalValue.setBackgroundColor(color);


            }
        });





    }

//    public void openColorPicker() {
//        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
//            @Override
//            public void onCancel(AmbilWarnaDialog dialog) {
//
//            }
//
//            @Override
//            public void onOk(AmbilWarnaDialog dialog, int color) {
//                mDefaultColor = color;
//            }
//
//        });
//    }
}
