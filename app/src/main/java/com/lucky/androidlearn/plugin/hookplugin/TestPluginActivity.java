package com.lucky.androidlearn.plugin.hookplugin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

// Android类加载机制源码分析
// startActivity-> Instrument.execute->AMS->ActivityThread
// -> performlaunchActivity --> classLoader.loadClass

// PathClassLoader.loadClass ->BaseDexClassLoader -> classLoader.loadClass-findClass(空方法，让子类去实现)
// ->BaseDexClassLoader.findClass() -->oathList.findClass


// BaseDexClassLoader.findClass()->c为什么为null——>DexPathList.findClass->
// DexFile.loadClassBinaryName(name)

// for遍历 dexElements == Element[]
// 分析Element 是什么？为什么Element.dexFile == null?
// Android虚拟机 dex文件的 == Dex表现形式的
// 描述 Element --dexFile拥有可执行

// 为什么Element ==null?
// 就是因为类加载机制的是 --> 宿主的classes.dex-Elements 没有插件的Elements

// 解决方案：把插件的dexElements和宿主的dexElements 融为一体
// PathClassLoader 就是能加载到 插件/宿主 都可以
// 加载到了

// --- Android ClassLoader介绍
// 1. java中的 ClassLoader 和Android的classLoader 是不一样
// 2. Android 中的ClassLoader分为两类
// 系统提供的的 ClassLoader --> BootClassLoader PathClasseLoder DexClassLoder
// 自定义ClassLoader

// 给系统预加载使用的 BootClassLoader
// 给程序/系统程序/应用程序加载class的 PathClassLoader
// 加载 apk zip apk文件的 DexClassLoader

// 1. 内核启动
// 2. init第一个进程
// 3. zygote 进程
// 启动时很早就要启动的
// zygoteInit ---> BootClassLoader PathClassLoader
// 4. zygote进程孵化 SystemServer
// 5. SystemServer启动很多的服务 （AMS PMS）


//1. Hook式 插件化
// Toast.makeText(this, "", Toast.LENGTH_SHORT).show 不会报错
// 不会报错 this  当前运行宿主

//2. 占位/插桩 插件化 -->插件开发 宿主中的组件环境
// Toast.makeText(this, "", Toast.LENGTH_SHORT).show
// 会报错 this 没有运行环境

public class TestPluginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_plugin);
    }


}
