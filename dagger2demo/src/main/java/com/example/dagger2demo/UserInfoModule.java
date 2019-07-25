package com.example.dagger2demo;

import com.example.mylibrary.UserInfoController;
import com.example.mylibrary.impl.DeviceInfoManager;
import com.example.mylibrary.impl.UserInfoControllerImpl;
import com.example.uilib.Toaster;

import dagger.Module;
import dagger.Provides;

@Module
public class UserInfoModule {
    @Provides
    UserInfoController provideUserInfoController(DeviceInfoManager manager, Toaster toaster) {
        return new UserInfoControllerImpl(manager, toaster);
    }
}
