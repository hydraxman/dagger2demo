package com.example.dagger2demo;

import com.example.mylibrary.UserInfoModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, UserInfoModule.class})
public interface CoreComponent {
    void inject(MainActivity mainActivity);
}
