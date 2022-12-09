package com.example.communityapp.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.communityapp.R
import com.example.communityapp.comment.CommentLVAdapter
import com.example.communityapp.comment.CommentModel
import com.example.communityapp.databinding.ActivityBoardInsideBinding
import com.example.communityapp.utils.FBAuth
import com.example.communityapp.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.lang.Exception

class BoardInsideActivity : AppCompatActivity() {

    private val TAG = BoardInsideActivity::class.java.simpleName

    private lateinit var binding : ActivityBoardInsideBinding

    private lateinit var key:String

    private val commentDataList = mutableListOf<CommentModel>()

    private lateinit var commentLVAdapter: CommentLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)



        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }

        //두 번째 방법
        key = intent.getStringExtra("key").toString()

        getBoardData(key)
        getImageData(key)

        //글쓰기 버튼이 눌리면
        binding.commentBtn.setOnClickListener {
            insertComment(key)

        }

        commentLVAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = commentLVAdapter

        getCommentData(key)
    }

    //파이어베이스에서 댓글 정보 불러와서 앱에 출력하기
    fun getCommentData(key: String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentDataList.clear()
//
////                Log.e("ContentListActivity", dataSnapshot.toString())
                for (dataModel in dataSnapshot.children){

                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)

                }

                commentLVAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }

        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)

    }

    //파이어베이스에 댓글 데이터 보내기
    fun insertComment(key: String){
        // comment
        //    -BoardKey
        //         -ComentKey
        //              -CommentData
        //              -CommentData
        //              -CommentData

        FBRef.commentRef
            .child(key)
            .push()
            .setValue(CommentModel(binding.commentArea.text.toString(), FBAuth.getTime()))

        Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
        binding.commentArea.setText("") //editText니까 .setText로 하기
    }
    private fun showDialog(){

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener{
            Toast.makeText(this, "수정 버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("key",key)
            startActivity(intent)
        }

        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener{

            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this, "삭제완", Toast.LENGTH_SHORT).show()
            finish()

        }
    }

    private fun getImageData(key: String){

        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key+".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.getImageArea

        // 이 부분 다시 공부하기
        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful){

                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)

            }else{
                binding.getImageArea.isVisible = false
            }
        })
    }

    private fun getBoardData(key: String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    //만약 여기서 에러가 나면 cath문으로 빠져라
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    Log.d(TAG, dataModel!!.title)

                    binding.titleArea.text = dataModel!!.title
                    binding.contentArea.text = dataModel!!.content
                    binding.timeArea.text = dataModel!!.time

                    //내 uid
                    val myUid = FBAuth.getUid()

                    //글쓴이의 uid
                    val writerUid = dataModel.uid

                    if(myUid.equals(writerUid)){
                        Toast.makeText(baseContext, "내가 글쓴이임", Toast.LENGTH_SHORT).show()
                        binding.boardSettingIcon.isVisible = true
                    }else{
                        Toast.makeText(baseContext, "내가 글쓴이 아님", Toast.LENGTH_SHORT).show()
                    }

                }catch (e: Exception){

                    Log.d(TAG, "삭제완료")

                }



            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)

    }
}