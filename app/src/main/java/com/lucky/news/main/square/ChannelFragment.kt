package com.lucky.news.main.square

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jingewenku.abrahamcaijin.commonutil.DensityUtils
import com.lucky.androidlearn.R
import com.lucky.androidlearn.presentation.ui.activities.MainActivity
import com.lucky.news.core.NewsConstant
import com.lucky.news.core.adapter.GridDividerItemDecoration
import com.lucky.news.core.utils.startActivityKtx
import com.lucky.news.main.square.data.ChannelItemModel
import com.scwang.smartrefresh.header.waveswipe.DisplayUtil
//import com.lucky.news.core.utils.*


// 频道
class ChannelFragment: Fragment(){

    private var mChannel: String? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater?.inflate(R.layout.fragment_channel, container, false)
        return view
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mChannel = arguments.getString(NewsConstant.CHANNEL_ARGUMENTS)
        setRecycleAdapter(view)
//        startActivityKtx()
    }

    private fun setRecycleAdapter(view: View?) {
        val recyclerView = view?.findViewById(R.id.recycler) as RecyclerView
        val adapter = SquareAdapter()
        adapter.setItemDataList(loadData())
//        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        val dividerWidth = DensityUtils.dp2px(context, 12.0f);
        val firstLastSpace = DensityUtils.dp2px(context, 0.0f);
        recyclerView.addItemDecoration(GridDividerItemDecoration(context, dividerWidth, 0,true))
        recyclerView.adapter = adapter
    }

    private fun loadData(): List<ChannelItemModel> {
        val models = arrayListOf<ChannelItemModel>()
        for (data in EnumChannel.values()) {
            val channelType = data.channelType()
            if (channelType == mChannel) {
                val description = data.desc
                val channelName = data.channelName()
                val imageUrl = data.imageUrl
                val clz = data.clazz
                val like = data.like
                models.add(ChannelItemModel(description, imageUrl, like, 10, clz))
            }
        }
        return models
    }



}