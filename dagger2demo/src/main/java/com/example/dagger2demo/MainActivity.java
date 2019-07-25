package com.example.dagger2demo;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mylibrary.DataCallback;
import com.example.mylibrary.UserInfoController;
import com.example.mylibrary.entity.UserInfo;
import com.sample.anno.BindView;
import com.sample.api.ButterKnife;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    @Inject
    UserInfoController userInfoController;
    @BindView(R.id.hello)
    TextView helloText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_main);
        App app = (App) getApplication();
        DaggerCoreComponent.builder().appComponent(app.getAppComponent())
                .userInfoModule(new UserInfoModule()).build().inject(this);
        userInfoController.fetchUserInfo(new DataCallback<UserInfo>() {
            @Override
            public void onDataFetched(UserInfo data) {
                // Refresh UI
                helloText.setText(String.format("%s - %s", data.getName(), data.getServiceId()));
            }
        });
    }
}
