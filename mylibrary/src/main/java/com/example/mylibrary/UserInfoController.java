package com.example.mylibrary;

import com.example.mylibrary.entity.UserInfo;

public interface UserInfoController {
    void fetchUserInfo(DataCallback<UserInfo> callback);

    void destroy();
}
