package com.example.menulistmovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class SearchActivity : AppCompatActivity() {
    private lateinit var viewLogout : TextView
    private lateinit var otentikasi : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        otentikasi = FirebaseAuth.getInstance()
        viewLogout =  findViewById(R.id.vLogout)
        viewLogout.setOnClickListener {
            otentikasi.signOut()
            Intent(this@SearchActivity, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    }
}