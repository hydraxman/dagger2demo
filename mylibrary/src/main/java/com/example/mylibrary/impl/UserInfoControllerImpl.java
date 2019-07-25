package com.example.mylibrary.impl;

import com.example.mylibrary.DataCallback;
import com.example.mylibrary.UserInfoController;
import com.example.mylibrary.entity.UserInfo;


public class UserInfoControllerImpl implements UserInfoController {
    private DeviceInfoManager deviceInfoController;

    public UserInfoControllerImpl(DeviceInfoManager deviceInfoController) {
        this.deviceInfoController = deviceInfoController;
    }

    public void fetchUserInfo(DataCallback<UserInfo> callback) {
        //....
        String serviceIdFromDevice = deviceInfoController.getServiceIdFromDevice();
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
