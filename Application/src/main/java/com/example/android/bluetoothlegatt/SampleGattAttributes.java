/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.bluetoothlegatt;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String HEART_RATE_MEASUREMENT = "aee4beea-91d2-11e9-bc42-52a6f7764f64";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String MOTOR_DELAY_CHARACTERISTIC = "aee4beec-91d2-11e9-bc42-52a6f7764f64";
    public static String CUSTOM_SETTING_SERVICE ="aee4f00f-91d2-11e9-bc42-52a6f7764f64";
    public static String TRANSMITTER_ERROR = "aee4beeb-91d2-11e9-bc42-52a6f7764f64";
    public static String CUSTOM_SETTINGS_CHARACTERISTIC = "aee4beed-91d2-11e9-bc42-52a6f7764f64";

    static {
        // Sample Services.
        attributes.put("aee4f00d-91d2-11e9-bc42-52a6f7764f64", "Sensor Service");
        attributes.put("aee4f00e-91d2-11e9-bc42-52a6f7764f64", "Transmitter Service");
        attributes.put(CUSTOM_SETTING_SERVICE, "Custom Setting Service");
        // Sample Characteristics.
        attributes.put(HEART_RATE_MEASUREMENT, "Sensor Data");
        attributes.put("aee4beeb-91d2-11e9-bc42-52a6f7764f64", "Transmitter Error");
        attributes.put(MOTOR_DELAY_CHARACTERISTIC, "Motor Delay");
        attributes.put(CUSTOM_SETTINGS_CHARACTERISTIC, "Custom Settings");
    }

    public static String lookup(String uuid, String defaultName) { //gelen uuid i değerini tanımlılar arasında kontrol ediyor, tanımlı değilse defaultName e ne gönderirse onu device name olarak gösteriyor
        String name = attributes.get(uuid);
        return name == null ? defaultName : name; //null değilse name
    }
}
