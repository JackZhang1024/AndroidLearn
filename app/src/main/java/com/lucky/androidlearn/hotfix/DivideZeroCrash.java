package com.lucky.androidlearn.hotfix;


import android.content.Context;
import android.widget.Toast;

// 制造一个错误类
public class DivideZeroCrash {

    public void createCrash(Context context) {
        int a = 10;
        int b = 0;
        Toast.makeText(context, "计算结果是"+a/b, Toast.LENGTH_LONG).show();
    }


}
