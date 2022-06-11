package com.example.menulistmovie

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var txtNama : EditText
    private lateinit var btnCari : Button
    private lateinit var listMov : ListView
    private lateinit var ref : DatabaseReference
    private lateinit var movList : MutableList<Movies>
    private lateinit var logout : TextView
    private lateinit var otentikasi : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ref = FirebaseDatabase.getInstance().getReference("movieList")
        txtNama = findViewById(R.id.etnama)
        btnCari = findViewById(R.id.bSearch)
        listMov = findViewById(R.id.lvmov)
        btnCari.setOnClickListener(this)
        movList = mutableListOf()
        logout = findViewById(R.id.logout)
        otentikasi = FirebaseAuth.getInstance()
        ref.addValueEventListener(object : ValueEventListener {

    override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    movList.clear()
                    for(h : DataSnapshot in snapshot.children){
                        val movies : Movies? = h.getValue(Movies::class.java)
                        if(movies != null){
                            movList.add(movies)
                        }
                    }
                    val adapter = MoviesAdapter(this@MainActivity, R.layout.movie_item, movList)
                    listMov.adapter = adapter
                }
            }

    override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        logout.setOnClickListener {
            otentikasi.signOut()
            Intent(this@MainActivity, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }

        btnCari.setOnClickListener {
            tampilData(txtNama.text.toString())
        }
    }

    override fun onClick(p0: View?) {

    }


    private fun tampilData(judul:String){
        ref.child(judul).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    movList.clear()
                    for(h : DataSnapshot in snapshot.children){
                        val movies : Movies? = h.getValue(Movies::class.java)
                        if(movies != null){
                            movList.add(movies)
                        }
                    }
                    val adapter = MoviesAdapter(this@MainActivity, R.layout.movie_item, movList)
                    listMov.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}