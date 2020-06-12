package com.lucky.androidlearn.hotfix;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashSet;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class FixDexUtil {


    private static HashSet<File> loadedDex = new HashSet<>();

    static {
        loadedDex.clear();
    }

    public static void loadFixedDex(Context context) {
        if (context == null) {
            return;
        }
        // 1. 找到所有的修复dex文件 并添加到集合中
        File dir = context.getDir("odex", Context.MODE_PRIVATE);
        File[] listFiles = dir.listFiles();
        for (File file : listFiles) {
            if (file.getName().startsWith("classes") && file.getName().endsWith(".dex")) {
                loadedDex.add(file);
            }
        }
        // 2 和之前apk里面的dex合并
        doDexInject(context, dir, loadedDex);
    }

    private static void doDexInject(Context context, File dir, HashSet<File> loadedDex) {
        // optimizeDir 保存修复后的dex目录
        String optimizeDir = dir.getAbsolutePath() + File.separator + "opt_dex";
        File fopt = new File(optimizeDir);
        if (!fopt.exists()) {
            fopt.mkdirs();
        }
        // 1. 加载应用程序的dex
        // 1) 拿到系统的dex 什么是系统的dex 这里说的是被修复前包里面所有的dex文件
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
        // 2) 拿到自己的dex  -- 自己的dex 就是修复的dex
        // String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent
        try {
            for (File dex : loadedDex) {
                DexClassLoader dexClassLoader = new DexClassLoader(
                        dex.getAbsolutePath(),
                        fopt.getAbsolutePath(),
                        null,
                        pathClassLoader
                );
                // 合并
                // BaseDexClassLoader ----> DexPathList ---> Element[] dexElements
                // 把 dexElements进行修改
                Object pathObj = getPathList(pathClassLoader);  // pathClassLoader 对应的 DexPathList
                Object dexObj = getPathList(dexClassLoader);  // DexClassLoader 对应的 DexPathList

                Object pathElements = getDexElements(pathObj);
                Object dexElements = getDexElements(dexObj);

                // 合并
                Object dexElement = combineArrayNew(dexElements, pathElements);
                // DexPathList 中的 dexElements的没有修改
                // 需要重写赋值给 dexElements
                Object pathList = getPathList(pathClassLoader);
                setField(pathList, pathList.getClass(), "dexElements", dexElement);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object getDexElements(Object dexObj) throws NoSuchFieldException, IllegalAccessException {
        return getField(dexObj, dexObj.getClass(), "dexElements");

    }

    // BaseDexClassLoader是 PathClassLoader 和 DexClassLoader的父类 而 BaseDexClassLoader 中有属性 DexPathList pathList
    private static Object getPathList(Object baseDexClassLoader) throws Exception {
        return getField(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    private static Object getField(Object obj, Class<?> c1, String field) throws NoSuchFieldException, IllegalAccessException {
        // 获取到baseDexClassLoader里面的名字叫field的成员
        Field localField = c1.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    private static void setField(Object obj, Class<?> c1, String field, Object value) throws NoSuchFieldException, IllegalAccessException {
        // 获取到baseDexClassLoader里面的名字叫field的成员
        Field localField = c1.getDeclaredField(field);
        localField.setAccessible(true);
        localField.set(obj, value);
    }

    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        // 获取到数组的字节码对象
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);

        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

    private static Object combineArrayNew(Object arrayLhs, Object arrayRhs) {
        // 获取到数组的字节码对象
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs); // 补丁数组
        int j = Array.getLength(arrayRhs); // 原dex数组长度
        int k = i + j;
        Object result = Array.newInstance(localClass, k);
        System.arraycopy(arrayLhs,0 , result, 0, i);
        System.arraycopy(arrayRhs,0 , result, i, j);
        return result;
    }

     // 宿主引入插件中的资源文件和以及现有apk的资源文件
      
}
