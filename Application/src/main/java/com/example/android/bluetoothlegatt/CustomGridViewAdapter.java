package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomGridViewAdapter extends ArrayAdapter<Device_Class> {

    private LayoutInflater mInflater;
    private ArrayList<Device_Class> deviceList;
    private final Context context;
    private CustomGridViewAdapter.ViewHolder holder;

    public CustomGridViewAdapter(Context context, ArrayList<Device_Class> deviceList)  {
        super(context,0, deviceList);
        this.context = context;
        this.deviceList = deviceList;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Device_Class getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.device_map_grid_view_layout,null);
            holder = new ViewHolder();
            holder.deviceType = (TextView) convertView.findViewById(R.id.grid_text);
            //TextView deviceType = (TextView) grid.findViewById(R.id.grid_text);
            holder.devicePortAddress = (TextView) convertView.findViewById(R.id.grid_text_1);
            holder.imageView = (ImageView) convertView.findViewById(R.id.grid_image);
            convertView.setTag(holder);




        } else {
            //Get viewholder we already created
            holder = (ViewHolder)convertView.getTag();
        }

        Device_Class device = deviceList.get(position);
        if(device != null) {
            holder.imageView.setImageResource(device.getDeviceIMG());
            holder.devicePortAddress.setText(""+device.getPortAddress());
            holder.deviceType.setText(""+device.getDeviceType());
        }

        return convertView;
    }

    //View Holder Pattern for better performance
    private static class ViewHolder {
        TextView deviceType;
        TextView devicePortAddress;
        ImageView imageView;
    }

}


