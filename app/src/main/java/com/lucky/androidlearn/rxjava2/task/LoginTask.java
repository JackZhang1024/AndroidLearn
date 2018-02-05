package com.lucky.androidlearn.rxjava2.task;

import com.lucky.androidlearn.rxjava2.model.BaseParam;
import com.lucky.androidlearn.rxjava2.model.UserParam;

import java.util.concurrent.Callable;

/**
 * @author zfz
 * Created by zfz on 2018/3/17.
 */

public class LoginTask implements Callable<BaseParam> {

    private UserParam userParam;

    public LoginTask(UserParam userParam) {
        this.userParam = userParam;
    }

    @Override
    public BaseParam call() throws Exception {
        if ("jack".equals(userParam.getUserName()) && "123456".equals(userParam.getPassword())) {
            Thread.sleep(3000);
            BaseParam param = new BaseParam();
            param.setToken("dahagjuajegj");
            return param;
        }
        return null;
    }
}
