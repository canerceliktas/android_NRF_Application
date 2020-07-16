package com.example.android.bluetoothlegatt;

public class Device_Class {

    private int deviceIMG;
    private int portAddress;
    private String deviceType;

    public Device_Class(int deviceIMG, int portAddress, String deviceType) {
        this.deviceIMG = deviceIMG;
        this.portAddress = portAddress;
        this.deviceType = deviceType;
    }

    public int getDeviceIMG() {
        return deviceIMG;
    }

    public void setDeviceIMG(int deviceIMG) {
        this.deviceIMG = deviceIMG;
    }

    public int getPortAddress() {
        return portAddress;
    }

    public void setPortAddress(int portAddress) {
        this.portAddress = portAddress;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
