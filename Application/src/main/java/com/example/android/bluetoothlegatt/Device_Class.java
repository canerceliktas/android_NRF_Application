package com.example.android.bluetoothlegatt;

public class Device_Class {

    private int deviceIMG;
    private int portAddress;
    private int deviceType;

    public Device_Class(int deviceIMG, int portAddress, int deviceType) {
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

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
}
