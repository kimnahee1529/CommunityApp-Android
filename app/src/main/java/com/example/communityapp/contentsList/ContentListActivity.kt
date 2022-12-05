package com.example.communityapp.contentsList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.communityapp.R
import com.example.communityapp.utils.FBAuth
import com.example.communityapp.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ContentListActivity : AppCompatActivity() {

    lateinit var myRef : DatabaseReference

    //북마크 id를 담을 리스트
    val bookmarkIdList = mutableListOf<String>()

    lateinit var rvAdapter: ContentRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)

        val items = ArrayList<ContentModel>()

        val itemKeyList = ArrayList<String>()

        rvAdapter = ContentRVAdapter(baseContext, items, itemKeyList, bookmarkIdList)

        //데이터 베이스에 쓰기
        val database = Firebase.database

        val category = intent.getStringExtra("category")


        if(category == "category1"){

            myRef = database.getReference("contents")

        }else if(category == "category2"){

            myRef = database.getReference("contents2")

        }

        //데이터 읽기
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

//                Log.e("ContentListActivity", dataSnapshot.toString())
                for (dataModel in dataSnapshot.children){
                    Log.d("ContentListActivity", dataModel.toString())
//                    Log.e("ContentListActivity", dataModel.key.toString())
                    val item = dataModel.getValue(ContentModel::class.java)
                    items.add(item!!)
                    itemKeyList.add(dataModel.key.toString())
                }

                //동기화하는 부분
                rvAdapter.notifyDataSetChanged()
                Log.w("ContentListActivity", items.toString())

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef.addValueEventListener(postListener)





        val rv : RecyclerView = findViewById(R.id.rv)


        //ContentRVAdapter의 파라미터 안에는 ArrayList<String> 데이터들이 들어가야 함
        rv.adapter = rvAdapter

        rv.layoutManager = GridLayoutManager(this, 2)

        getBookmarkData()

    }
    private fun getBookmarkData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //중복해서 쌓이는 것을 막기 위한 코드
                bookmarkIdList.clear()

                for (dataModel in dataSnapshot.children){
//                    Log.d("getBookmarkData", dataModel.key.toString())
                    bookmarkIdList.add(dataModel.key.toString())
//                    Log.d("getBookmarkData", dataModel.toString())
                }
                Log.e("Bookmark", bookmarkIdList.toString())
                rvAdapter.notifyDataSetChanged()


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)

    }

}

