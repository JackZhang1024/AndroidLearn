package com.lucky.androidlearn.j2v8;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.debug.V8DebugServer;
import com.lucky.androidlearn.R;


//https://github.com/buggerjs/bugger-v8-client/blob/master/PROTOCOL.md
//https://github.com/eclipsesource/J2V8/tree/master/src
//pc和mobile之间的通信 adb forward
public class JSDebuggerActivity extends AppCompatActivity {
    private final static String TAG = JSDebuggerActivity.class.getSimpleName();

    private V8 runtime;
    private Button mBtnDebugJS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_debugger);
        mBtnDebugJS = findViewById(R.id.btn_debug_js);
        V8DebugServer.configureV8ForDebugging();
        int port = 6868;
        boolean waitForConnection = true;
        runtime = V8.createV8Runtime();
        V8DebugServer server = new V8DebugServer(runtime, port, waitForConnection);
        server.setTraceCommunication(true);
        Log.i(TAG, "V8 Debug Server listening on port " + server.getPort());
        server.start();
        Console console = new Console();
        V8Object v8Console = new V8Object(runtime);
        runtime.add("console", v8Console);
        v8Console.registerJavaMethod(console, "log", "log", new Class<?>[]{String.class});
        v8Console.registerJavaMethod(console, "error", "error", new Class<?>[]{String.class});
        mBtnDebugJS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exeScript(v);
            }
        });
    }

    public void exeScript(View view) {
        int i = 1;
        String script =
                "console.log('line" + (i++) + "'); //1  \n"
                        + "console.log('line" + (i++) + "');// 2  \n"
                        + "console.log('line" + (i++) + "');// 3  \n"
                        + "console.log('line" + (i++) + "');// 4  \n"
                        + "console.log('line" + (i++) + "');// 8  \n";
        runtime.executeScript(script, "testDebug.js", 0);
    }


    class Console {
        public void log(final String message) {
            Log.i(TAG, "[INFO] " + message);
        }

        public void error(final String message) {
            Log.e(TAG, "[ERROR] " + message);
        }
    }
}
