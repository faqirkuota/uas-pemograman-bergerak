package com.example.menulistmovie

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ManagementMoviesActivity : AppCompatActivity() {
    private lateinit var ref : DatabaseReference
    private lateinit var listMgMov : ListView
    private lateinit var movMgList : MutableList<Movies>
    private lateinit var logout : TextView
    private lateinit var otentikasi : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management_movies)
        /**
         * @desc Membuat initilisasi variabel pada layout
         */
        ref = FirebaseDatabase.getInstance().getReference("dbMovie")
        listMgMov = findViewById(R.id.lvmgmov)
        movMgList = mutableListOf()
        logout = findViewById(R.id.logoutmg)
        otentikasi = FirebaseAuth.getInstance()

        /**
         * @desc Code ini dioverride onDataChange agar menampilkan data dari firebase
         * dan diparsing menjadi layout movie_mg_item
         * Sedangkan methode onCancelled tidak terpakai
         */
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    movMgList.clear()
                    for(h : DataSnapshot in snapshot.children){
                        val movies : Movies? = h.getValue(Movies::class.java)
                        if(movies != null){
                            movMgList.add(movies)
                        }
                    }
                    val adapter = MoviesMgAdapter(this@ManagementMoviesActivity, R.layout.movie_mg_item, movMgList)
                    listMgMov.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        /**
         * @desc Code ini membuat on click listener pada tombol logout agar mensignout otentikasi
         * yang sudah ada
         */
        logout.setOnClickListener {
            otentikasi.signOut()
            Intent(this@ManagementMoviesActivity, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    }
}