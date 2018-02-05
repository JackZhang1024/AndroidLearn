package com.lucky.androidlearn.rxjava2.model;

import com.google.gson.Gson;

/**
 * @author zfz
 * Created by zfz on 2018/3/17.
 */

public class User {

    private String name;
    private int age;
    private String avatarImage;


    public User(String name, int age, String avatarImage) {
        this.name = name;
        this.age = age;
        this.avatarImage = avatarImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
