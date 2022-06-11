package com.example.menulistmovie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SearchActivity : AppCompatActivity() {
    private lateinit var otentikasi : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        otentikasi = FirebaseAuth.getInstance()
    }
}