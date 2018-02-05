package com.lucky.androidlearn.rxjava2.model;

/**
 *
 * @author zfz
 * Created by zfz on 2018/3/17.
 */

public class UserParam {

    private String userName;
    private String password;

    public UserParam(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
