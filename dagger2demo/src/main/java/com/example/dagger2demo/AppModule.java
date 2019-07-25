package com.example.dagger2demo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.dagger2demo.annotations.ApplicationContext;
import com.example.uilib.Toaster;
import com.example.uilib.impl.ToasterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private static final String TAG = AppModule.class.getSimpleName();
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
        Log.i(TAG, "provideSharedPrefs called");
        return mApplication.getSharedPreferences("default", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Toaster provideToaster(@ApplicationContext Context context) {
        Log.i(TAG, "provideToaster called");
        return new ToasterImpl(context);
    }
}
