package com.lucky.androidlearn.exception.toast;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lucky.androidlearn.R;
import java.lang.reflect.Field;
import com.jingewenku.abrahamcaijin.commonutil.DensityUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.security.auth.login.LoginException;

/**
 * 问题分析
 * https://blog.csdn.net/joye123/article/details/80738113
 *
 */
public class ToastUtil {
    private static final String TAG = "ToastUtil";
    private Context mContext;
    private Toast mToast;

    public ToastUtil(Context context) {
        this.mContext = context;
    }


    public synchronized void info(String strText) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setBackgroundColor(Color.parseColor("#00000000"));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        int leftRightPadding = DensityUtils.dip2px(mContext, 25);
        int upBottomPadding = DensityUtils.dip2px(mContext, 10);
        TextView textView = new TextView(mContext);
        textView.setPadding(leftRightPadding, upBottomPadding, leftRightPadding, upBottomPadding);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(15);
        textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        textView.setBackgroundResource(R.drawable.toast_background);
        linearLayout.addView(textView);
        textView.setText(strText);
        Toast toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(linearLayout);
        handToastShow(toast);
        mToast = toast;
    }

    private boolean isReflectedHandler;

    // 处理7.x手机上可能出现的BadTokenException
    private void handToastShow(Toast toast) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.N && sdkInt < Build.VERSION_CODES.O && !isReflectedHandler) {
            reflectTNHandler(toast);
            //这里为了避免多次反射，使用一个标识来限制
            isReflectedHandler = true;
        }
        toast.show();
    }

    private static void reflectTNHandler(Toast toast) {
        try {
            Field tNField = toast.getClass().getDeclaredField("mTN");
            if (tNField == null) {
                return;
            }
            tNField.setAccessible(true);
            Object TN = tNField.get(toast);
            if (TN == null) {
                return;
            }
            Field handlerField = TN.getClass().getDeclaredField("mHandler");
            if (handlerField == null) {
                return;
            }
            Log.d("TN class is %s.", TN.getClass().getName());
            handlerField.setAccessible(true);
            handlerField.set(TN, new ProxyTNHandler(TN));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //Toast$TN持有的Handler变量
    private static class ProxyTNHandler extends Handler {
        private Object tnObject;
        private Method handleShowMethod;
        private Method handleHideMethod;

        ProxyTNHandler(Object tnObject) {
            this.tnObject = tnObject;
            try {
                this.handleShowMethod = tnObject.getClass().getDeclaredMethod("handleShow", IBinder.class);
                this.handleShowMethod.setAccessible(true);
                Log.d("handleShow method is %s", handleShowMethod.getName());
                this.handleHideMethod = tnObject.getClass().getDeclaredMethod("handleHide");
                this.handleHideMethod.setAccessible(true);
                Log.d("handleHide method is %s", handleHideMethod.getName());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    //SHOW
                    IBinder token = (IBinder) msg.obj;
                    Log.d("token is %s", token.getClass().getName());
                    if (handleShowMethod != null) {
                        try {
                            handleShowMethod.invoke(tnObject, token);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            Log.e(TAG, "handleMessage: IllegalAccessException "+e.getMessage());
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                            Log.e(TAG, "handleMessage: InvocationTargetException "+e.getMessage());
                        } catch (WindowManager.BadTokenException e) {
                            //显示Toast时添加BadTokenException异常捕获
                            e.printStackTrace();
                            Log.e(TAG, "handleMessage: BadTokenException "+e.getMessage());
                        }
                    }
                    break;
                }

                case 1: {
                    //HIDE
                    Log.d("HIDE","handleMessage(): hide");
                    if (handleHideMethod != null) {
                        try {
                            handleHideMethod.invoke(tnObject);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                case 2: {
                    //CANCEL
                    Log.d("SHOW", "handleMessage(): cancel");
                    if (handleHideMethod != null) {
                        try {
                            handleHideMethod.invoke(tnObject);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }

            }
            super.handleMessage(msg);
        }
    }

}
