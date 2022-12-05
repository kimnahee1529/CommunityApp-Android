package com.example.communityapp.contentsList

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.communityapp.R

class BookmarkRVAdapter(val context : Context,
                        val items: ArrayList<ContentModel>,
                        val keyList : ArrayList<String>,
                        val bookmarkIdList: MutableList<String>)
    : RecyclerView.Adapter<BookmarkRVAdapter.Viewholder>() {

    //아이템들을 가져와 하나하나씩 넣어주는 역할
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkRVAdapter.Viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_rv_item, parent, false)
        Log.d("BookmarkRVAdapter", keyList.toString())
        Log.d("BookmarkRVAdapter", bookmarkIdList.toString())
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: BookmarkRVAdapter.Viewholder, position: Int) {
        holder.bindItems(items[position], keyList[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    //item.xml에 데이터들을 하나하나씩 넣어주는 역할
    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){

        //여기는 ContentListActivity에서 넘어온 모델의 데이터가 있음
        fun bindItems(item : ContentModel, key: String){

            itemView.setOnClickListener{
                //이 부분은 이해가 잘 안되네
                Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
                val intent = Intent(context, ContentShowActivity::class.java)
                intent.putExtra("url", item.webUrl)
                itemView.context.startActivity(intent)
            }

            val contentTitle = itemView.findViewById<TextView>(R.id.textArea)
            val imageViewArea = itemView.findViewById<ImageView>(R.id.imageArea)
            val bookmarkArea = itemView.findViewById<ImageView>(R.id.bookmarkArea)

            //keyList들이 bookmarkIdList들에 포함이 되어있는지
            if(bookmarkIdList.contains(key)){
                bookmarkArea.setImageResource(R.drawable.bookmark_color)
//                Log.d("북마크 추가", "북마크 추가")
            }else{
                bookmarkArea.setImageResource(R.drawable.bookmark_white)
//                Log.d("북마크 삭제", "북마크 삭제")
            }

            contentTitle.text = item.title
            Glide.with(context)
                .load(item.imagUrl)
                .into(imageViewArea)

        }
    }

}