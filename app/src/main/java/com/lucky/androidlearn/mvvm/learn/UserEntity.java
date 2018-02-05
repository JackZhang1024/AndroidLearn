package com.lucky.androidlearn.mvvm.learn;

/**
 * Created by zfz on 2018/2/5.
 */

public class UserEntity {
    private String nickname;
    private String username;
    private int age;
    private boolean isAdult;

    public UserEntity() {
    }

    public UserEntity(String nickname, String username, int age) {
        this.nickname = nickname;
        this.username = username;
        this.age = age;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }
}
