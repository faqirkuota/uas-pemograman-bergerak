package com.example.menulistmovie

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import java.util.concurrent.Executors


class MoviesAdapter(val mContext : Context, val layoutResId : Int, val movList: List<Movies>) : ArrayAdapter<Movies>(mContext,layoutResId,movList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mContext)
        val view : View = layoutInflater.inflate(layoutResId, null)
        val tvNama : TextView = view.findViewById(R.id.tvnama)
        val tvNamaKet : TextView = view.findViewById(R.id.tvnamaKet)
        val tahun : TextView = view.findViewById(R.id.tvnamayear)
        val poster : ImageView = view.findViewById(R.id.poster)
        val movies : Movies = movList[position]

        var image: Bitmap? = null
        val executor = Executors.newSingleThreadExecutor()
        // Once the executor parses the URL
        // and receives the image, handler will load it
        // in the ImageView
        val handler = Handler(Looper.getMainLooper())

        executor.execute {

            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {
                val `in` = java.net.URL(movies.poster).openStream()
                image = BitmapFactory.decodeStream(`in`)

                // Only for making changes in UI
                handler.post {
                    poster.setImageBitmap(image)
                }
            }

            // If the URL does not point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        tvNama.text = movies.title
        tvNamaKet.text = movies.plot
        tahun.text = movies.year

        return view

    }

    private fun showSearchDialog(movies: Movies) {
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Edit Data")
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.search, null)
        val txtNama = view.findViewById<EditText>(R.id.etnama)

        txtNama.setText(movies.title)

        builder.setView(view)
//        builder.setPositiveButton("Update Data"){p0,p1 ->
//            val dbMov = FirebaseDatabase.getInstance().getReference("movies")
//            val nama = txtNama.text.toString().trim()
//            if(nama.isEmpty()){
//               txtNama.error = "Nama tidak boleh kosong"
//               txtNama.requestFocus()
//               return@setPositiveButton
//            }
//            val movies = Movies(movies.id, nama)
//            dbMov.child(movies.id!!).setValue(movies)
//            Toast.makeText(mContext,"Update data sukses", Toast.LENGTH_LONG).show()
//        }
        builder.setNegativeButton("No") { p0, p1 ->

        }
        val alert = builder.create()
        alert.show()
    }
}