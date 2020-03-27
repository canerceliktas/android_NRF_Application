package com.example.android.bluetoothlegatt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


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

        CurrentBarChart = (HorizontalBarChart) view.findViewById(R.id.current_bar_chart);
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0,12.5f));
        entries.add(new BarEntry(1,11.6f));
        entries.add(new BarEntry(2,15.7f));

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Current");
        labels.add("Avr. Current");
        labels.add("Peak Current");

        BarDataSet dataSet = new BarDataSet(entries,"Current");
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);
        CurrentBarChart.setData(data);
        CurrentBarChart.setFitBars(true);
        CurrentBarChart.invalidate();



//        description.setText("Current Consumption");
        CurrentBarChart.setDescription(description);




        return view;
    }
}
