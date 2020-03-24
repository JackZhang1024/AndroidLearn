package com.lucky.news.core.utils


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.lucky.news.core.NewsConstant


fun Fragment.startActivityKtx(clazz: Class<*>, bundle: Bundle= Bundle()){
    startActivity(Intent(this.activity, clazz).apply {
        bundle.putString(NewsConstant.INTENT_ARGUMENTS, "Nice to meet you")
        putExtras(bundle)
    })
}

