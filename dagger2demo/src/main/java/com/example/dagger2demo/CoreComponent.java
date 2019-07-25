package com.example.dagger2demo;

import com.example.dagger2demo.annotations.PerActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = {AppComponent.class}, modules = {UserInfoModule.class})
public interface CoreComponent {
    /**
     *
     * @param activity the specific instance that needs injection
     */
    void inject(MainActivity activity);
}
