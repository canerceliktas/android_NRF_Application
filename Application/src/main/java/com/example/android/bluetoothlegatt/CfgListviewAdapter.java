package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CfgListviewAdapter extends BaseAdapter {
    private LayoutInflater userInflater;
    private String[] savedFiles;


    public CfgListviewAdapter(Activity activity, String[] savedFiles) {
        userInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.savedFiles = savedFiles;
    }



    @Override
    public int getCount() {
        return savedFiles.length;
    }

    @Override
    public Object getItem(int position) {
        return savedFiles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View lineView;

        lineView = userInflater.inflate(R.layout.saved_cfg_listview_layout, null);
        TextView mFileName = (TextView) lineView.findViewById(R.id.configName);

        String file = savedFiles[position];
        mFileName.setText(file);

        return lineView;
    }
}
