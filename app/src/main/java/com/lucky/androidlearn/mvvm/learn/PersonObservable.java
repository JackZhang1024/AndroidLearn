package com.lucky.androidlearn.mvvm.learn;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.lucky.androidlearn.BR;

/**
 *
 * notifyPropertyChanged(BR.参数名)通知更新这一个参数，
 * 需要与@Bindable注解配合使用。notifyChange()通知更新所有参数，可以不用和@Bindable注解配合使用
 * Created by zfz on 2018/2/5.
 */

public class PersonObservable extends BaseObservable{
    private String name;
    private String nickname;
    private int age;
    private String avatar;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        notifyPropertyChanged(BR.nickname);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
