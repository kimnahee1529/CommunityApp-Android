package com.example.communityapp.board

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.communityapp.R
import com.example.communityapp.utils.FBAuth

class BoardListLVAdapter(val boardList: MutableList<BoardModel>) : BaseAdapter(){
    override fun getCount(): Int {
        return boardList.size
    }

    override fun getItem(position: Int): Any {
        return boardList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

//        if(view == null){
        //아이템 재활용하는 부분에서 생기는 버그 수정
        view = LayoutInflater.from(parent?.context).inflate(R.layout.board_list_item, parent, false)
//        }

        val itemLinearLayoutView = view?.findViewById<LinearLayout>(R.id.itemView)
        var title = view?.findViewById<TextView>(R.id.titleArea)
        var content = view?.findViewById<TextView>(R.id.contentArea)
        var time = view?.findViewById<TextView>(R.id.timeArea)

        if(boardList[position].uid.equals(FBAuth.getUid())){
            itemLinearLayoutView?.setBackgroundColor(Color.parseColor("#ffa500"))
        }


        title!!.text = boardList[position].title
        content!!.text = boardList[position].content
        time!!.text = boardList[position].time

        return view!!
    }

}