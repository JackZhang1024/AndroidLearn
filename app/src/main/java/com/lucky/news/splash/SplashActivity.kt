package com.lucky.news.splash

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

import com.lucky.androidlearn.R
import com.lucky.androidlearn.jindong.JingdongActivity
import com.lucky.androidlearn.presentation.ui.activities.MainActivity
import com.lucky.kotlin.KotlinLearnMainActivity
import com.lucky.news.main.NewsMainActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private var alphaAnimation: AlphaAnimation? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immerseStatusBar()
        setContentView(R.layout.activity_splash)
        alphaAnimation = AlphaAnimation(1.0f, 0.0f);
        alphaAnimation?.run {
            duration = 4000
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation?) {
                    goToMainActivity()
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationStart(animation: Animation?) {

                }
            })
        }
        splash_root.startAnimation(alphaAnimation);
    }

    fun goToMainActivity() {
        startActivity(Intent(this, NewsMainActivity::class.java));
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun immerseStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }



}
