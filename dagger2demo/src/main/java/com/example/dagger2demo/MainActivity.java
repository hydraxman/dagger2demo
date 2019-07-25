package com.example.dagger2demo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mylibrary.DataCallback;
import com.example.mylibrary.UserInfoController;
import com.example.mylibrary.UserInfoModule;
import com.example.mylibrary.entity.UserInfo;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity {
    @Inject
    UserInfoController userInfoController;
    TextView helloText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaggerCoreComponent.builder().appModule(new AppModule(getApplication()))
                .userInfoModule(new UserInfoModule()).build().inject(this);
        helloText = findViewById(R.id.hello);
        userInfoController.fetchUserInfo(new DataCallback<UserInfo>() {
            @Override
            public void onDataFetched(UserInfo data) {
                // Refresh UI
                helloText.setText(data.getName() + ": " + data.getServiceId());
            }
        });
    }
}
