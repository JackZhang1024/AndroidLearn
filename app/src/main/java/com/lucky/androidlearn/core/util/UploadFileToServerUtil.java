package com.lucky.androidlearn.core.util;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import android.util.Log;

/**
 * 这个类的目的是为了上传文件到服务器上
 * @author administor
 */
public class UploadFileToServerUtil {

    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 1000;   //超时时间
    private static final String CHARSET = "utf-8"; //设置编码
    private static String result = null;
    private static String BOUNDARY = null;
    private static String PREFIX = "--";
    private static String LINE_END = "\r\n";
    private static String CONTENT_TYPE = "multipart/form-data";   //内容类型
    private static HttpURLConnection conn = null;

    /**
     * android上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param RequestURL 请求的url
     * @return 返回响应的内容
     */
    public static String uploadFile(final File file, String RequestURL) {
        BOUNDARY = UUID.randomUUID().toString();  //边界标识   随机生成
        try {
            URL url = new URL(RequestURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    if (file != null) {
                        try {
                            result = UploadFileToServer(file, BOUNDARY, PREFIX, LINE_END, conn);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 这个方法只有在本类中被uploadFile()方法调用
     * @param file
     * @param BOUNDARY
     * @param PREFIX
     * @param LINE_END
     * @param conn
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
    private static String UploadFileToServer(File file, String BOUNDARY,
                                             String PREFIX, String LINE_END, HttpURLConnection conn)
            throws IOException, FileNotFoundException {
        String result = null;
        /**
         * 当文件不为空，把文件包装并且上传
         */
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        StringBuffer sb = new StringBuffer();
        sb.append(PREFIX);
        sb.append(BOUNDARY);
        sb.append(LINE_END);
        /**
         * 这里重点注意：
         * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
         * filename是文件的名字，包含后缀名的   比如:abc.png
         */
        sb.append("Content-Disposition: form-data; name=\"img\"; filename=\"" + file.getName() + "\"" + LINE_END);
        sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
        sb.append(LINE_END);
        dos.write(sb.toString().getBytes());
        InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = is.read(bytes)) != -1) {
            dos.write(bytes, 0, len);
        }
        is.close();
        dos.write(LINE_END.getBytes());
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
        dos.write(end_data);
        dos.flush();
        /**
         * 获取响应码  200=成功
         * 当响应成功，获取响应的流
         */
        int res = conn.getResponseCode();
        Log.e(TAG, "response code:" + res);
        if (res == 200) {
            Log.e(TAG, "request success");
            InputStream input = conn.getInputStream();
            StringBuffer sb1 = new StringBuffer();
            int ss;
            while ((ss = input.read()) != -1) {
                sb1.append((char) ss);
            }
            result = sb1.toString();
            System.out.println("result------->" + result);
            Log.e(TAG, "result : " + result);
        } else {
            Log.e(TAG, "request error");
        }
        return result;
    }


}
