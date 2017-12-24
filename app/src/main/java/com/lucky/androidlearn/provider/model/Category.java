package com.lucky.androidlearn.provider.model;

/**
 * 书籍种类
 * Created by zfz on 2017/12/31.
 */

public class Category {
    private int id;
    private String name;
    private String kind;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
