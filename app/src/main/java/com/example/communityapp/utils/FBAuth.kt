package com.example.communityapp.utils

import android.content.Intent
import android.os.Handler
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FBAuth {

    companion object {

        private lateinit var auth: FirebaseAuth

        fun getUid() : String {
            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.uid.toString()
        }

    }

}