package com.lucky.news.main.square

import com.lucky.androidlearn.animation.AnimationMainActivity
import com.lucky.androidlearn.annotation.AnnotationActivity
import com.lucky.androidlearn.aspectj.AspectJActivity
import com.lucky.androidlearn.dagger2learn.DaggerLearnMainActivity
import com.lucky.androidlearn.encrypt.EncryptDecryptActivity
import com.lucky.androidlearn.eventbus.EventBusActivity
import com.lucky.androidlearn.exception.ExceptionSummaryActivity
import com.lucky.androidlearn.filelearn.FileProcessLearnActivity
import com.lucky.androidlearn.fragmentation.FragmentationActivity
import com.lucky.androidlearn.fragmentation.FragmentationViewPagerActivity
import com.lucky.androidlearn.handler.HandlerLearnActivity
import com.lucky.androidlearn.hotfix.HotFixActivity
import com.lucky.androidlearn.hybrid.WebViewSummaryActivity
import com.lucky.androidlearn.ipc.IPCLearnActivity
import com.lucky.androidlearn.j2v8.J2V8LearnActivity
import com.lucky.androidlearn.jindong.JingdongActivity
import com.lucky.androidlearn.json.JsonLearnActivity
import com.lucky.androidlearn.launchmode.LaunchModeActivity
import com.lucky.androidlearn.lru.LRUActivity
import com.lucky.androidlearn.markdown.MarkdownActivity
import com.lucky.androidlearn.math.MathActivity
import com.lucky.androidlearn.media.MediaActivity
import com.lucky.androidlearn.multithread.MultiThreadActivity
import com.lucky.androidlearn.mvc.MVCMainActivity
import com.lucky.androidlearn.mvp.MVPMainActivity
import com.lucky.androidlearn.mvvm.MVVMMainActivity
import com.lucky.androidlearn.plugin.hookplugin.HookPluginMainActivity
import com.lucky.androidlearn.proxy.DynamicProxyActivity
import com.lucky.androidlearn.rxjava2.RxJavaActivity
import com.lucky.androidlearn.service.ServiceActivity
import com.lucky.androidlearn.widget.common.CommonWidgetActivity
import com.lucky.androidlearn.window.WindowManagerActivity
import com.lucky.kotlin.KotlinLearnMainActivity
import com.lucky.news.core.NewsConstant

enum class EnumChannel(var desc: String, var imageUrl: String, var clazz: Class<*>, var like: Boolean) {

    MULTI_THREAD("多线程", NewsConstant.IMG_LOVELY, MultiThreadActivity::class.java, true) {
        override fun channelName(): String = "multi_thread"

        override fun channelType(): String = "java"
    },
    ANNOTATION("注解", NewsConstant.IMG_LOVELY, AnnotationActivity::class.java, true) {
        override fun channelName(): String = "annotation"

        override fun channelType() = "java"
    },
    JSON("JSON实战", NewsConstant.IMG_LOVELY, JsonLearnActivity::class.java, true) {
        override fun channelName(): String = "json"

        override fun channelType() = "java"
    },
    LRU("LRU算法", NewsConstant.IMG_LOVELY, LRUActivity::class.java, true) {
        override fun channelName(): String = "lru"

        override fun channelType() = "java"
    },
    MATH("数学", NewsConstant.IMG_LOVELY, MathActivity::class.java, true) {
        override fun channelName(): String = "math"

        override fun channelType() = "java"
    },
    ENC_DEC("加密和解密", NewsConstant.IMG_LOVELY, EncryptDecryptActivity::class.java, true) {
        override fun channelName(): String = "enc_dec"

        override fun channelType() = "java"
    },
    IPC("IPC跨进程", NewsConstant.IMG_LOVELY, IPCLearnActivity::class.java, true) {
        override fun channelName(): String = "ipc"

        override fun channelType() = "android"
    },
    ANIMATION("Animation动画", NewsConstant.IMG_LOVELY, AnimationMainActivity::class.java, true) {
        override fun channelName(): String = "animation"

        override fun channelType() = "android"
    },
    COMMON_WIDGET("常见控件", NewsConstant.IMG_LOVELY, CommonWidgetActivity::class.java, true) {
        override fun channelName(): String = "widget"

        override fun channelType() = "android"
    },
    WINDOW_MANAGER("窗口管理", NewsConstant.IMG_LOVELY, WindowManagerActivity::class.java, true) {
        override fun channelName(): String = "window"

        override fun channelType() = "android"
    },
    ASPECTJ("AspectJ", NewsConstant.IMG_LOVELY, AspectJActivity::class.java, true) {
        override fun channelName(): String = "aspectJ"

        override fun channelType() = "android"
    },
    FRAGMENTATION("Fragmentation", NewsConstant.IMG_LOVELY, FragmentationActivity::class.java, true) {
        override fun channelName(): String = "fragmentation"

        override fun channelType() = "android"
    },
    DAGGER_LEARN("DaggerLearn", NewsConstant.IMG_LOVELY, DaggerLearnMainActivity::class.java, true) {
        override fun channelName(): String = "dagger2learn"

        override fun channelType() = "android"
    },
    EVENT_BUS("EventBus", NewsConstant.IMG_LOVELY, EventBusActivity::class.java, true) {
        override fun channelName(): String = "eventbus"

        override fun channelType() = "android"
    },
    EXCEPTION("Exception", NewsConstant.IMG_LOVELY, ExceptionSummaryActivity::class.java, true) {
        override fun channelName(): String = "exception"

        override fun channelType() = "android"
    },
    MARK_DOWN("Markdown", NewsConstant.IMG_LOVELY, MarkdownActivity::class.java, true) {
        override fun channelName(): String = "markdown"

        override fun channelType() = "android"
    },
    FILE_LEARN("文件相关", NewsConstant.IMG_LOVELY, FileProcessLearnActivity::class.java, true) {
        override fun channelName(): String = "file_learn"

        override fun channelType() = "android"
    },
    HANDLER("Handler学习", NewsConstant.IMG_LOVELY, HandlerLearnActivity::class.java, true) {
        override fun channelName(): String = "handler"

        override fun channelType() = "android"
    },
    SERVICE("Service学习", NewsConstant.IMG_LOVELY, ServiceActivity::class.java, true) {
        override fun channelName(): String = "service"

        override fun channelType() = "android"
    },
    HOT_FIX("HotFix学习", NewsConstant.IMG_LOVELY, HotFixActivity::class.java, true) {
        override fun channelName(): String = "hotfix"

        override fun channelType() = "android"
    },
    HYBIRD("Hybird学习", NewsConstant.IMG_LOVELY, WebViewSummaryActivity::class.java, true) {
        override fun channelName(): String = "hybird"

        override fun channelType() = "android"
    },
    J2V8("J2V8知识", NewsConstant.IMG_LOVELY, J2V8LearnActivity::class.java, true) {
        override fun channelName(): String = "j2v8"

        override fun channelType() = "android"
    },
    JINGDONG("京东首页", NewsConstant.IMG_LOVELY, JingdongActivity::class.java, true) {
        override fun channelName(): String = "jingdong"

        override fun channelType() = "android"
    },
    DYNAMIC("动态Hook", NewsConstant.IMG_LOVELY, DynamicProxyActivity::class.java, true) {
        override fun channelName(): String = "动态Hook"

        override fun channelType() = "android"
    },

    HOOK_PLUGIN("Hook方式启动插件", NewsConstant.IMG_LOVELY, HookPluginMainActivity::class.java, true) {
        override fun channelName(): String = "Hook方式启动插件"

        override fun channelType() = "android"
    },

    LAUNCH_MODE("Activity启动模式", NewsConstant.IMG_LOVELY, LaunchModeActivity::class.java, true) {
        override fun channelName(): String = "launch_mode"

        override fun channelType() = "android"
    },
    MEDIA("多媒体相关", NewsConstant.IMG_LOVELY, MediaActivity::class.java, true) {
        override fun channelName(): String = "media"

        override fun channelType() = "android"
    },
    MVC("MVC框架", NewsConstant.IMG_LOVELY, MVCMainActivity::class.java, true) {
        override fun channelName(): String = "mvc_frame"

        override fun channelType() = "frame"
    },
    MVP("MVP框架", NewsConstant.IMG_LOVELY, MVPMainActivity::class.java, true) {
        override fun channelName(): String = "mvp_frame"

        override fun channelType() = "frame"
    },
    MVVM("MVVM框架", NewsConstant.IMG_LOVELY, MVVMMainActivity::class.java, true) {
        override fun channelName(): String = "mvp_frame"

        override fun channelType() = "frame"
    },
    RXJava("RxJava", NewsConstant.IMG_LOVELY, RxJavaActivity::class.java, true) {
        override fun channelName(): String = "rxjava"

        override fun channelType() = "rxjava"
    },
    KOTLIN("Kotlin", NewsConstant.IMG_LOVELY, KotlinLearnMainActivity::class.java, true) {
        override fun channelName(): String = "mvp_frame"

        override fun channelType() = "kotlin"
    };

    abstract fun channelName(): String

    abstract fun channelType(): String

}