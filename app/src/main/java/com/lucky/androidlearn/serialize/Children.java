package com.lucky.androidlearn.serialize;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zfz on 2018/1/2.
 */

public class Children implements Parcelable {
    private String name;
    private String gender;
    private int age;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeInt(age);
        out.writeString(gender);
    }

    public static final Creator<Children> CREATOR = new Creator<Children>() {

        @Override
        public Children createFromParcel(Parcel source) {
            return new Children(source);
        }

        @Override
        public Children[] newArray(int size) {
            return new Children[size];
        }
    };

    public Children(Parcel in) {
        name = in.readString();
        age = in.readInt();
        gender = in.readString();
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

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }
}
