package com.example.dagger2demo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.dagger2demo.annotations.ApplicationContext;
import com.example.uilib.Toaster;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(Application application);

    @ApplicationContext
    Context provideContext();

    Application provideApplication();

    SharedPreferences provideSharedPrefs();

    Toaster provideToaster();
}
