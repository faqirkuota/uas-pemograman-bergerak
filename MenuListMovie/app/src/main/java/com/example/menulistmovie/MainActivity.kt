package com.example.menulistmovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var txtNama : EditText
    private lateinit var btnCari : Button
    private lateinit var listMov : ListView
    private lateinit var ref : DatabaseReference
    private lateinit var movList : MutableList<Movies>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ref = FirebaseDatabase.getInstance().getReference("movieList")
        txtNama = findViewById(R.id.etnama)
        btnCari = findViewById(R.id.bSearch)
        listMov = findViewById(R.id.lvmov)
        btnCari.setOnClickListener(this)
        movList = mutableListOf()
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
    }

    override fun onClick(p0: View?) {
        simpanData()
    }
    private fun simpanData(){
        val nama : String = txtNama.text.toString().trim()
        if(nama.isEmpty()) {
            txtNama.error = "Masukkan judul"
            return
        }
        //  val ref = FirebaseDatabase.getInstance().getReference("dbMovies")
        val movId = ref.push().key

        val movies = Movies(movId!!, nama)
        if(movId != null){
            ref.child(movId).setValue(movies).addOnCompleteListener {
                Toast.makeText(applicationContext, "Data berhasil di simpan", Toast.LENGTH_LONG).show()
            }
        }
    }
}