package com.example.mylibrary.impl;

import android.content.SharedPreferences;

import java.util.UUID;

import javax.inject.Inject;

public class DeviceInfoManager {
    private static final String serviceIdKey = "service_id";
    private SharedPreferences sharedPreferences;

    @Inject
    public DeviceInfoManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public String getServiceIdFromDevice() {
        if (sharedPreferences == null) {
            return null;
        }
        String serviceId = sharedPreferences.getString(serviceIdKey, null);
        if (serviceId == null) {
            serviceId = UUID.randomUUID().toString();
            sharedPreferences.edit().putString(serviceIdKey, serviceId).apply();
        }
        return serviceId;
    }

    public void destroy() {
        sharedPreferences = null;
    }
}
