package com.example.communityapp.contentsList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.communityapp.R

class ContentRVAdapter(val context : Context, val items: ArrayList<ContentModel>) : RecyclerView.Adapter<ContentRVAdapter.Viewholder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }
    var itemClick: ItemClick? = null

    //아이템들을 가져와 하나하나씩 넣어주는 역할
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentRVAdapter.Viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_rv_item, parent, false)
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: ContentRVAdapter.Viewholder, position: Int) {

        if(itemClick != null){
            holder.itemView.setOnClickListener { v->
                itemClick?.onClick(v, position)
            }
        }
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    //item.xml에 데이터들을 하나하나씩 넣어주는 역할
    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){

        //여기는 ContentListActivity에서 넘어온 모델의 데이터가 있음
        fun bindItems(item : ContentModel){

            val contentTitle = itemView.findViewById<TextView>(R.id.textArea)
            val imageViewArea = itemView.findViewById<ImageView>(R.id.imageArea)

            contentTitle.text = item.title
            Glide.with(context)
                .load(item.imagUrl)
                .into(imageViewArea)

        }
    }
}