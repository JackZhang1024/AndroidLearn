package com.lucky.news.main.square

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jingewenku.abrahamcaijin.commonutil.PicassoUtils
import com.lucky.androidlearn.R
import com.lucky.news.main.square.data.ChannelItemModel
import com.squareup.picasso.Picasso

class SquareAdapter : RecyclerView.Adapter<SquareViewHolder>() {

    private var items: List<ChannelItemModel>? = null

    public fun setItemDataList(listData: List<ChannelItemModel>) {
        this.items = listData
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SquareViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.layout_square_item, parent, false)
        return SquareViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SquareViewHolder?, position: Int) {
        holder?.setItemData(items!![position])
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

}

class SquareViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setItemData(data: ChannelItemModel) {
        val tvDescription = itemView.findViewById(R.id.tv_description) as TextView
        tvDescription.text = data.description
        val imageDisplay = itemView.findViewById(R.id.iv_content) as ImageView
        imageDisplay.setOnClickListener {
            val context = itemView.context
            context.startActivity(Intent(context, data.clz))
        }
        Picasso.with(itemView.context).load(data.imageUrl).into(imageDisplay)
    }


}