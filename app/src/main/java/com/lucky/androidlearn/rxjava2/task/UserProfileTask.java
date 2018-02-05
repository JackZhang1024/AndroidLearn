package com.lucky.androidlearn.rxjava2.task;

import com.lucky.androidlearn.rxjava2.model.BaseParam;
import com.lucky.androidlearn.rxjava2.model.User;

import java.util.concurrent.Callable;

/**
 * @author zfz
 * Created by zfz on 2018/3/17.
 */

public class UserProfileTask implements Callable<User> {
    private BaseParam baseParam;

    public UserProfileTask(BaseParam baseParam) {
        this.baseParam = baseParam;
    }

    @Override
    public User call() throws Exception {
        Thread.sleep(3000);
        User user = new User("我是Jack12334", 23, "http://www.baidu.com");
        return user;
    }
}
