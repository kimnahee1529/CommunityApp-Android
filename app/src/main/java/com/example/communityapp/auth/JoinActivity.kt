package com.example.communityapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.communityapp.MainActivity
import com.example.communityapp.R
import com.example.communityapp.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {

    private lateinit var binding : ActivityJoinBinding
    //회원 가입 창에서 인증 기능을 사용해야 하기 때문
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_join)

        //FirebaseAuth 인스턴스 초기화
        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        binding.joinBtn.setOnClickListener {

            var isGoToJoin = true

            val email = binding.emailArea.text.toString()
            val password1 = binding.passwordArea1.text.toString()
            val password2 = binding.passwordArea2.text.toString()

            //값이 비어있는지 확인
            if(email.isEmpty()){
                Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }
            if(password1.isEmpty()){
                Toast.makeText(this, "Password1을 입력해주세요", Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }
            if(password2.isEmpty()){
                Toast.makeText(this, "Password2을 입력해주세요", Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }

            if(!password1.equals(password2)){
                Toast.makeText(this, "비밀번호를 똑같이 입력해주세요", Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }

            if(password1.length < 6){
                Toast.makeText(this, "비밀번호를 6자리 이상으로 입력해주세요", Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }

            if(isGoToJoin == true){
                auth.createUserWithEmailAndPassword(email, password1)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)

                            //회원가입 성공하고 MainActivity가 나왔을 때 뒤로가기 누르면
                            //다시 회원가입 창이 나오지 말고 아예 꺼지게 구현하는 구문
                            //강의에서는 CLEAR_TOP으로 했는데 안돼서 CLEAR_TASK로 하니까 됨
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        } else {

                            Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()

                        }
                    }

            }
        }
    }
}