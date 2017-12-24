package com.lucky.androidlearn.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import com.lucky.androidlearn.core.util.contactutils.MailSenderInfo;
import com.lucky.androidlearn.core.util.contactutils.SimpleMailSender;

/**
 *
 *
 */
public class CrashHandler implements UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";
    private UncaughtExceptionHandler mDefaultHandler;// 系统默认的UncaughtException处理类
    private static CrashHandler INSTANCE = new CrashHandler();// CrashHandler实例
    private Context mContext;// 程序的Context对象
    private Map<String, String> info = new HashMap<String, String>();// 用来存储设备信息和异常信息
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MenNiuCrash" + "/";
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");// 用于格式化日期,作为日志文件名的一部分

    private String errorInfoToSendMail;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {

    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
        Thread.setDefaultUncaughtExceptionHandler(this);// 设置该CrashHandler为程序的默认处理器
    }

    /**
     * 当UncaughtException发生时会转入该重写的方法来处理
     */
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果自定义的没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            // 自定义处理完成之后可以在此处操作 三秒后程序自动退出
            try {
                Thread.currentThread();
                Thread.sleep(3 * 1000);
                Process.killProcess(Process.myPid());
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public boolean handleException(Throwable ex) {
        if (ex == null)
            return false;
        // 收集设备参数信息
        // collectDeviceInfo(mContext);
        // 保存日志文件
        // saveCrashInfo2File(ex);//暂时先注释掉
        saveAndSendErrorByMail(ex);
        return true;
    }

    public void saveAndSendErrorByMail(final Throwable ex) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                // 收集设备参数信息
                collectDeviceInfo(mContext);
                // 保存日志文件
                saveCrashInfo2File(ex);// 暂时先注释掉
                sendErrorInfo();
            }
        }).start();
    }

    public void sendErrorInfo() {
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost("smtp.qq.com");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUserName("1120335370@qq.com"); // 你的邮箱地址
        mailInfo.setPassword("zxcasdqwe123");// 您的邮箱密码
        mailInfo.setFromAddress("1120335370@qq.com");
        mailInfo.setToAddress("zhunixingfu365@sina.cn");
        mailInfo.setSubject("蒙牛报错信息" + format.format(new Date()));
        mailInfo.setContent(errorInfoToSendMail);
        // 这个类主要来发送邮件
        SimpleMailSender sms = new SimpleMailSender();
        sms.sendTextMail(mailInfo);// 发送文体格式
        // sms.sendHtmlMail(mailInfo);//发送html格式
        errorInfoToSendMail = null;
    }

    /**
     * 收集设备参数信息
     *
     * @param context
     */
    public void collectDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();// 获得包管理器
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                info.put("versionName", versionName);
                info.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        Field[] fields = Build.class.getDeclaredFields();// 反射机制
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                info.put(field.getName(), field.get("").toString());
                Log.d(TAG, field.getName() + ":" + field.get(""));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : info.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\r\n");
        }
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        // 循环着把所有的异常信息写入writer中
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();// 记得关闭
        String result = writer.toString();
        sb.append(result);
        if (sb != null && !"".equals(sb.toString())) {
            errorInfoToSendMail = sb.toString();
        }
        // 保存文件
        String time = format.format(new Date());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                String fileName = time + "_log" + ".txt";
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    return "SDCard没有挂载..";
                }
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File f = new File(path + fileName);
                if (!f.exists()) {
                    f.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(f, true);
                fos.write(sb.toString().getBytes());
                fos.close();
                return fileName;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}