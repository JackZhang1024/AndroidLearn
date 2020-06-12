package com.lucky.androidlearn.proxy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.lucky.androidlearn.R;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// 动态代理
// 在不需要修改原有代码的基础上对原有的逻辑进行hook
public class DynamicProxyActivity extends AppCompatActivity {
    private static final String TAG = "DynamicProxyActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy_hook);
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DynamicProxyActivity.this, ((Button) v).getText(), Toast.LENGTH_SHORT).show();
            }
        });
        try {
            hook(btn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void hook(View btn) throws Exception {
        // 代理掉原有的OnClickListener
        // getListenerInfo().mOnClickListener = l;

        // 获取到View的getListenerInfo对象 然后将其中的mOnClickListener对象替换成我们的自己的Listener
        // 就可以对点击事件进行Hook 添加我们自己的逻辑

        // 相当于对已经设置过一遍的Listener进行重置

        // 2. 将动态代理赋值给mOnClickListener
        // - 1. 需要找到mOnClickListener
        Class mListenerInfoClass = Class.forName("android.view.View$ListenerInfo");
        Field mClickListenerField = mListenerInfoClass.getDeclaredField("mOnClickListener");
        //mClickListenerField.set(listenerInfo, mOnClickListenerProxy);
        // - 2. 需要获取到listenerInfo对象 如何获取
        // ListenerInfo getListenerInfo() {} 有这个方法可以获取到ListenerInfo对象
        Class mViewClass = Class.forName("android.view.View");
        Method mListenerInfoMethod = mViewClass.getDeclaredMethod("getListenerInfo");
        mListenerInfoMethod.setAccessible(true);
        Object mListenerInfo = mListenerInfoMethod.invoke(btn);
        mClickListenerField.setAccessible(true);
        // -3 获取原有的View.OnClickListener的对象
        Object mOriginClickListener = mClickListenerField.get(mListenerInfo);

        // 1. 创建动态代理
        Object mOnClickListenerProxy = Proxy.newProxyInstance(getClassLoader(), new Class[]{View.OnClickListener.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Log.e(TAG, "invoke: " + method.getName());
                Button button = new Button(DynamicProxyActivity.this);
                button.setText("我是动态代理");
                // 这里的args是onClick(View view)中的参数
                // 我们需要原有的ViewClickListener对象 执行原有的onClick事件
                return method.invoke(mOriginClickListener, button);
            }
        });

        // -4 替换系统原有的ClickListener，换成我们自己的动态代理 mOnClickListenerProxy
        // 相当于给代理Proxy执行onClick事件
        mClickListenerField.set(mListenerInfo, mOnClickListenerProxy);
    }

}
