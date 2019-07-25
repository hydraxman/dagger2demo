package com.example.mylibrary.impl;

import com.example.mylibrary.DataCallback;
import com.example.mylibrary.UserInfoController;
import com.example.mylibrary.entity.UserInfo;
import com.example.uilib.Toaster;


public class UserInfoControllerImpl implements UserInfoController {
    private final Toaster toaster;
    private final DeviceInfoManager deviceInfoController;

    public UserInfoControllerImpl(DeviceInfoManager deviceInfoController, Toaster toaster) {
        this.deviceInfoController = deviceInfoController;
        this.toaster = toaster;
    }

    public void fetchUserInfo(DataCallback<UserInfo> callback) {
        //....
        String serviceIdFromDevice = deviceInfoController.getServiceIdFromDevice();
        toaster.longToast(String.format("id is %s", serviceIdFromDevice));
        UserInfo userInfo = getUserInfo(serviceIdFromDevice);
        callback.onDataFetched(userInfo);
    }

    private UserInfo getUserInfo(String serviceIdFromDevice) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("James");
        userInfo.setAge(22);
        userInfo.setGender(1);
        userInfo.setServiceId(serviceIdFromDevice);
        return userInfo;
    }
}
