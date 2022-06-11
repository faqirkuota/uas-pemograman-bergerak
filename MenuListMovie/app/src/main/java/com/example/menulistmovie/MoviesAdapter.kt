package com.example.menulistmovie

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class MoviesAdapter(val mContext : Context, val layoutResId : Int, val movList: List<Movies>) : ArrayAdapter<Movies>(mContext,layoutResId,movList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mContext)
        val view : View = layoutInflater.inflate(layoutResId, null)
        val tvNama : TextView = view.findViewById(R.id.tvnama)
        val tvEdit : TextView = view.findViewById(R.id.tvedit)

        val movies : Movies = movList[position]
        tvEdit.setOnClickListener {
            showSearchDialog(movies)
        }
        tvNama.text = movies.nama
        return view
    }

    private fun showSearchDialog(movies: Movies) {
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Edit Data")
        val  inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.search, null)
        val txtNama = view.findViewById<EditText>(R.id.etnama)

        txtNama.setText(movies.nama)

        builder.setView(view)
        builder.setPositiveButton("Update Data"){p0,p1 ->
            val dbMov = FirebaseDatabase.getInstance().getReference("movies")
            val nama = txtNama.text.toString().trim()
            if(nama.isEmpty()){
               txtNama.error = "Nama tidak boleh kosong"
               txtNama.requestFocus()
               return@setPositiveButton
            }
            val movies = Movies(movies.id, nama)
            dbMov.child(movies.id!!).setValue(movies)
            Toast.makeText(mContext,"Update data sukses", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No"){p0,p1 ->

        }
        val alert = builder.create()
        alert.show()
    }
}