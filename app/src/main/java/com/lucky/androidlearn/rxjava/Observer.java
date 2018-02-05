package com.lucky.androidlearn.rxjava;

/**
 * @author zfz
 * Created by zfz on 2018/3/13.
 */

public interface Observer {

    /**
     * 更新状态
     * @param state 更新状态
     */
    void update(String state);

    /**
     * 获取Observer的名称
     *
     * @return  Observer的姓名
     */
    String getName();

}
