package com.example.vadim.muscloud.Entities;

import java.io.Serializable;

/**
 * Created by Vadim on 30.04.2018.
 */

public class User implements Serializable {
    private String mLogin;
    private String mPassword;
    private boolean mHasSuccessLogin;

    public User(String login, String password) {
        mLogin = login;
        mPassword = password;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        mLogin = login;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public boolean hasSuccessLogin() {
        return mHasSuccessLogin;
    }

    public void setHasSuccessLogin(boolean hasSuccessLogin) {
        mHasSuccessLogin = hasSuccessLogin;
    }
}
