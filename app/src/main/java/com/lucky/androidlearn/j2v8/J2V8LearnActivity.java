package com.lucky.androidlearn.j2v8;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.eclipsesource.v8.JavaVoidCallback;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.utils.V8ObjectUtils;
import com.lucky.androidlearn.R;

import java.util.Map;

// 在Java中执行JavaScript
public class J2V8LearnActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "J2V8LearnActivity";
    private Button mBtnExecuteJS;
    private Button mBtnExecuteJSObject;
    private Button mBtnExecuteCreateV8Object;
    private Button mBtnV8Map;
    private Button mBtnCallJSFunction;
    private Button mBtnCallJavaFunction;
    private Button mBtnCallJavaCallBackFunction;
    private Button mBtnShowAlertDialog;
    private Button mBtnJSDebugger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_j2v8_learn);
        mBtnExecuteJS = findViewById(R.id.btn_execute_js);
        mBtnExecuteJS.setOnClickListener(this);
        mBtnExecuteJSObject = findViewById(R.id.btn_execute_jsobject);
        mBtnExecuteJSObject.setOnClickListener(this);
        mBtnExecuteCreateV8Object = findViewById(R.id.btn_create_v8object);
        mBtnExecuteCreateV8Object.setOnClickListener(this);
        mBtnV8Map = findViewById(R.id.btn_v8_map);
        mBtnV8Map.setOnClickListener(this);
        mBtnCallJSFunction = findViewById(R.id.btn_call_js_function);
        mBtnCallJSFunction.setOnClickListener(this);
        mBtnCallJavaFunction = findViewById(R.id.btn_call_java_function);
        mBtnCallJavaFunction.setOnClickListener(this);
        mBtnCallJavaCallBackFunction = findViewById(R.id.btn_call_java_callback_function);
        mBtnCallJavaCallBackFunction.setOnClickListener(this);
        mBtnShowAlertDialog = findViewById(R.id.btn_call_js_show_alert);
        mBtnShowAlertDialog.setOnClickListener(this);
        mBtnJSDebugger = findViewById(R.id.btn_js_debugger);
        mBtnJSDebugger.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_execute_js:
                executeJS();
                break;
            case R.id.btn_execute_jsobject:
                executeJSObject();
                break;
            case R.id.btn_create_v8object:
                executeCreateV8Object();
                break;
            case R.id.btn_v8_map:
                executeMap();
                break;
            case R.id.btn_call_js_function:
                executeCallJSFunction();
                break;
            case R.id.btn_call_java_function:
                executeCallJavaMethod();
                break;
            case R.id.btn_call_java_callback_function:
                executeCallJavaMethodCallBack();
                break;
            case R.id.btn_call_js_show_alert:
                executeCallAlertDialogFunction();
                break;
            case R.id.btn_js_debugger:
                startActivity(new Intent(this, JSDebuggerActivity.class));
                break;
        }
    }

    // 运行JS脚本
    private void executeJS() {
        V8 runtime = V8.createV8Runtime();
        int result = runtime.executeIntegerScript("1+1");
        System.out.println("result " + result);
        runtime.release();
    }

    //btn_execute_jsobject
    private void executeJSObject() {
        V8 runtime = V8.createV8Runtime();
        String js = "var me = {'First':'Tomas', 'Middle':'Rubin', 'Last':'Jason', 'Age':19 };";
        V8Object v8Object = runtime.executeObjectScript(js + "me;");
        String firstName = v8Object.getString("First");
        String lastName = v8Object.getString("Last");
        String middleName = v8Object.getString("Middle");
        int age = v8Object.getInteger("Age");
        System.out.println("First " + firstName + " last " + lastName + " Age " + age);
        v8Object.release();
        runtime.release();
    }

    private void executeCreateV8Object() {
        V8 runtime = V8.createV8Runtime();
        V8Object me = new V8Object(runtime)
                .add("First", "Tomas")
                .add("Middle", "Rubin")
                .add("Last", "Jason")
                .add("Age", 19);
        runtime.add("me", me);
        runtime.executeVoidScript("");
        me.release();
        runtime.release();
    }

    private void executeMap() {
        V8 runtime = V8.createV8Runtime();
        V8Object me = new V8Object(runtime)
                .add("id", "Tomas")
                .add("First", "Tomas")
                .add("Middle", "Rubin")
                .add("Last", "Jason")
                .add("Age", 19);
        Map<String, Object> map = V8ObjectUtils.toMap(me);
        String firstName = (String) map.get("First");
        String middleName = (String) map.get("Middle");
        int age = (int) map.get("Age");
        System.out.println("FirstName " + firstName);
        me.release();
        runtime.release();
    }

    // 调用
    private void executeCallJSFunction() {
        V8 runtime = V8.createV8Runtime();
        runtime.executeVoidScript("var foo=function(x){return 10+x; }");
        V8Array parameters = new V8Array(runtime).push(10);
        int result = runtime.executeIntegerFunction("foo", parameters);
        System.out.println("result " + result);
        parameters.release();
        runtime.release();
    }


    private void executeCallJavaMethod() {
        V8 runtime = V8.createV8Runtime();
        runtime.registerJavaMethod(new Printer(), "print", "print", new Class<?>[]{String.class});
        runtime.executeVoidScript("print('你好，中国');");
        runtime.release();
    }

    // 执行Java回调方法
    private void executeCallJavaMethodCallBack() {
        V8 runtime = V8.createV8Runtime();
        runtime.registerJavaMethod(new JavaVoidCallback() {
            @Override
            public void invoke(V8Object var1, V8Array parameters) {
                if (parameters.length() > 0) {
                    Object object = parameters.get(0);
                    Log.e(TAG, "invoke: " + object);
                }

            }
        }, "print");
        runtime.executeVoidScript("print('Hello World!');");
        runtime.release();
    }


    private static class Printer {
        public void print(String string) {
            System.out.println(string);
        }
    }

    private void executeCallAlertDialogFunction() {
        V8 runtime = V8.createV8Runtime();
        runtime.registerJavaMethod(new Alert(this), "alert", "alert", new Class<?>[]{String.class, String.class});
        runtime.executeVoidScript("var foo=function(x){ alert('标题', '中国'); return 10+x; }");
        V8Array parameters = new V8Array(runtime).push(10);
        int result = runtime.executeIntegerFunction("foo", parameters);
        System.out.println("result " + result);
        parameters.release();
        runtime.release();
    }


    private static class Alert {
        private Context context;

        public Alert(Context context) {
            this.context = context;
        }

        public void alert(String title, String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("确定", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
    }


}
