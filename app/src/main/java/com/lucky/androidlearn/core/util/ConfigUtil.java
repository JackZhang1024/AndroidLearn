package com.lucky.androidlearn.core.util;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 要求此类的加载的文件时xxx.properties
 * 例如：db.properties 此类适合配置一些系统固定的参数 如IP地址 端口号
 *
 * @author administor
 */
public class ConfigUtil {
    public static Properties properties = new Properties();
    public static ConfigUtil config = new ConfigUtil();

    //db.properties以键值对的形式存在于文件中
    //ip=192.168.1.123
    //...
    private ConfigUtil() {
        loadProperties("db.properties");
    }

    /**
     * 加载配置
     * @param filename 所要加载的配置的文件名
     */
    private static void loadProperties(String filename) {
        InputStream in = null;
        try {
            in = new FileInputStream(filename);//项目的根目录路径下
            //in=new FileInputStream("src/"+filename);//src目录下
            //in=Config.class.getClassLoader().getResourceAsStream(filename);//src目录下
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param key 查询值所需要的键
     * @return
     */
    public static String getValueBykey(String key) {
        return properties.getProperty(key);
    }
}
