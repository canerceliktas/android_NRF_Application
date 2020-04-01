package com.example.android.bluetoothlegatt;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import static com.example.android.bluetoothlegatt.ControllerTab.InstantCurrent;
import static com.example.android.bluetoothlegatt.ControllerTab.AvarageCurrent;
import static com.example.android.bluetoothlegatt.ControllerTab.PeakCurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MotorController#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MotorController extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private HorizontalBarChart CurrentBarChart;
    private Byte[]  Current = {125};
    private Description description;
    private String label = "Current";
    float I1,I2,I3;

    ArrayList<BarEntry> InstCurrent = new ArrayList<>();    //Veriyi tutan array listler, 3 akım 3 arraylist
    ArrayList<BarEntry> AvrgCurrent = new ArrayList<>();
    ArrayList<BarEntry> PkCurrent = new ArrayList<>();

    BarDataSet set1,set2,set3;

    private Switch clockwise;
    Timer timer = new Timer();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public MotorController() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MotorController.
     */
    // TODO: Rename and change types and number of parameters
    public static MotorController newInstance(String param1, String param2) {
        MotorController fragment = new MotorController();
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
        view = inflater.inflate(R.layout.fragment_motor_controller, container, false);

//        CurrentBarChart = (HorizontalBarChart) view.findViewById(R.id.current_bar_chart);
//        description = CurrentBarChart.getDescription();
//        description.setText("Current (A)");
//        CurrentBarChart.setDescription(description);

        clockwise = (Switch) view.findViewById(R.id.switch1);
        clockwise.setTextColor(Color.RED);

        clockwise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (clockwise.isChecked()){
                    clockwise.setTextColor(Color.GREEN);
                }else{
                    clockwise.setTextColor(Color.RED);
                }
            }
        });


        I1 = InstantCurrent;
        I2 = AvarageCurrent;
        I3 = PeakCurrent;

//        setGraphData(I3,I2,I1);

//        //Set the schedule function
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                I3=I3+0.5f;
//                I2=I2+0.3f;
//                I1=I1+0.7f;
//
//                setGraphData(I3,I2,I1);
//            }
//        },500,2000);

        return view;
    }


//    private void setGraphData(float PeakCurrent, float AvarageCurrent, float InstantCurrent) {
//
//        YAxis rightAxis = CurrentBarChart.getAxisRight();       //grafiğin alttaki y ekseni
//        XAxis xAxis = CurrentBarChart.getXAxis();               //grafiğin x ekseni
//        CurrentBarChart.setTouchEnabled(false);                 //Interaction ı engellemek için dokunma devredışı
//
////        ArrayList<BarEntry> InstCurrent = new ArrayList<>();    //Veriyi tutan array listler, 3 akım 3 arraylist
////        ArrayList<BarEntry> AvrgCurrent = new ArrayList<>();
////        ArrayList<BarEntry> PkCurrent = new ArrayList<>();
//
//        InstCurrent.add(new BarEntry(0,InstantCurrent));
//        AvrgCurrent.add(new BarEntry(0,AvarageCurrent));
//        PkCurrent.add(new BarEntry(0,PeakCurrent));
//
//        set1 = new BarDataSet(InstCurrent,"Instant Current");    //Data Set e veriyi ekle
//        set1.setColor(Color.BLUE);
//        set2 = new BarDataSet(AvrgCurrent,"Avarage Current");
//        set2.setColor(Color.GREEN);
//        set3 = new BarDataSet(PkCurrent,"Peak Current");
//        set3.setColor(Color.RED);
//
//        float groupSpace = 0.05f;
//        float barSpace = 0.015f;
//        float barWidth = 0.045f;
//
//        BarData data = new BarData(set1,set2,set3);
//        data.setBarWidth(barWidth);
//        CurrentBarChart.setData(data);
//        CurrentBarChart.groupBars(0,groupSpace,barSpace);
//
//        CurrentBarChart.setVisibleXRange(0f,1f);
//        CurrentBarChart.setVisibleYRange(0f,50f, YAxis.AxisDependency.LEFT);
//
//        xAxis.setDrawAxisLine(false);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setEnabled(false);
//        xAxis.setDrawGridLines(false);
//        xAxis.setLabelCount(1);
//        xAxis.setAxisMinimum(0f);
//        xAxis.setAxisMaximum(0.2f);
//        rightAxis.setEnabled(false);
//
//        CurrentBarChart.invalidate();
//    }
//
//    private void changeGraphData(float PeakCurrent, float AvarageCurrent, float InstantCurrent){
//
//        InstCurrent.add(new BarEntry(0,InstantCurrent));
//        AvrgCurrent.add(new BarEntry(0,AvarageCurrent));
//        PkCurrent.add(new BarEntry(0,PeakCurrent));
//
//
//    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }
}
