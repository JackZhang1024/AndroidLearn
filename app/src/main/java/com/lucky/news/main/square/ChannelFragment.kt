package com.lucky.news.main.square

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lucky.androidlearn.R

// 频道
class ChannelFragment:Fragment(){

    lateinit var mChannel:String;

    fun setChannel(channel:String){
        this.mChannel = channel
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View? = inflater?.inflate(R.layout.fragment_channel, container, false)
        return view
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }



}