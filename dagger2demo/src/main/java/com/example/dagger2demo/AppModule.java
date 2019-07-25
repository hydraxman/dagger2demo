package com.example.dagger2demo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.dagger2demo.annotations.ApplicationContext;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Application mApplication;

    public AppModule(Application app) {
        mApplication = app;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    SharedPreferences provideSharedPrefs() {
        return mApplication.getSharedPreferences("default", Context.MODE_PRIVATE);
    }
}
