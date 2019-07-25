package com.example.mylibrary;

import com.example.mylibrary.impl.DeviceInfoManager;
import com.example.mylibrary.impl.UserInfoControllerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UserInfoModule {
    @Singleton
    @Provides
    UserInfoController provideUserInfoController(DeviceInfoManager manager) {
        return new UserInfoControllerImpl(manager);
    }
}
