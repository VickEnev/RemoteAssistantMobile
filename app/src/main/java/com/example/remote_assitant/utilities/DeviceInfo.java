package com.example.remote_assitant.utilities;

import android.os.Build;

public class DeviceInfo {

    private String mDeviceModel;
    private String mDeviceName;

    ///<summary>
    /// Генерира информация за у-вото (DEVICE_NAME, DEVICE_MODEL)
    ///</summary>
    public DeviceInfo() {
        mDeviceModel = Build.MODEL;
        mDeviceName = Build.DEVICE;
    }

    public String getDeviceModel() {
        return mDeviceModel;
    }

    public String getDeviceName() {
        return mDeviceName;
    }
}
