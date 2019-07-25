package com.example.dagger2demo;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mylibrary.DataCallback;
import com.example.mylibrary.UserInfoController;
import com.example.mylibrary.entity.UserInfo;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    @Inject
    UserInfoController userInfoController;
    TextView helloText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App app = (App) getApplication();
        DaggerCoreComponent.builder().appComponent(app.getAppComponent())
                .userInfoModule(new UserInfoModule()).build().inject(this);
        helloText = findViewById(R.id.hello);
        userInfoController.fetchUserInfo(new DataCallback<UserInfo>() {
            @Override
            public void onDataFetched(UserInfo data) {
                // Refresh UI
                helloText.setText(String.format("%s - %s", data.getName(), data.getServiceId()));
            }
        });
    }
}
