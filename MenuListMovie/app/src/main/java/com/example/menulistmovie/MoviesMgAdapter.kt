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
import android.widget.*
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.Executors

/**
 * @desc Karena untuk menjebatani data dengan tampilan listview maka kelompok kami memutuskan
 * untuk menggunakan adapter
 */
class MoviesMgAdapter (val mContext : Context, val layoutResId : Int, val movList: List<Movies>) : ArrayAdapter<Movies>(mContext,layoutResId,movList){
    /**
     * @desc Inisiasi variabel global
     */
    val executor = Executors.newSingleThreadExecutor()
    var image: Bitmap? = null
    val handler = Handler(Looper.getMainLooper())

    /**
     * @desc Methode ini dibuat untuk memparsing tampilan dengan data yang ada
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mContext)
        val view : View = layoutInflater.inflate(layoutResId, null)
        val tvNamaMg : TextView = view.findViewById(R.id.tvNamaMg)
        val tvTahunMg : TextView = view.findViewById(R.id.tvTahunMg)
        val ivPosterMg : ImageView = view.findViewById(R.id.ivPosterMg)
        val bEditMg : TextView = view.findViewById(R.id.bEditMg)
        val movies : Movies = movList[position]

        /**
         * @desc Code ini dibuat untuk mengenerate image dari url
         */
        executor.execute {
            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {
                val `in` = java.net.URL(movies.poster).openStream()
                image = BitmapFactory.decodeStream(`in`)

                // Only for making changes in UI
                handler.post {
                    ivPosterMg.setImageBitmap(image)
                }
            }

            // If the URL does not point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        tvNamaMg.text = movies.title
        tvTahunMg.text = movies.year
        val movie : Movies = movList[position]
        bEditMg.setOnClickListener {
            showUpdateDialog(movies)
        }
        return view
    }

    /**
     * @desc Methode ini dibuat untuk menampilkan layar popup dari layout activity_edit_movie
     */
    private fun showUpdateDialog(movie: Movies) {
        /**
         * @desc Membuat initilisasi variabel pada layout
         */
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Edit Data Movie")
        val  inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.activity_edit_movie, null)
        val etNamaJudul = view.findViewById<EditText>(R.id.etNamaJudul)
        val etTahun = view.findViewById<EditText>(R.id.etTahun)
        val etKeterangan = view.findViewById<EditText>(R.id.etKeterangan)
        val etPoster = view.findViewById<ImageView>(R.id.etPoster)

        /**
         * @desc Code untuk menampilkan data ke layar popup
         */
        etNamaJudul.setText(movie.title)
        etTahun.setText(movie.year)
        etKeterangan.setText(movie.plot)
        executor.execute {
            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {
                val `in` = java.net.URL(movie.poster).openStream()
                image = BitmapFactory.decodeStream(`in`)

                // Only for making changes in UI
                handler.post {
                    etPoster.setImageBitmap(image)
                }
            }
            // If the URL does not point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        builder.setView(view)
        /**
         * @desc Code untuk mengupdate value ke firebase
         */
        builder.setPositiveButton("Update"){p0,p1 ->
            val movieList = FirebaseDatabase.getInstance().getReference("movieList")
            val namaJudul = etNamaJudul.text.toString().trim()
            val tahun = etTahun.text.toString().trim()
            val keterangan = etKeterangan.text.toString().trim()

            if(namaJudul.isEmpty()){
                etNamaJudul.error = "Nama Film tidak boleh kosong"
                etNamaJudul.requestFocus()
                return@setPositiveButton
            }
            if(tahun.isEmpty()){
                etTahun.error = "Tahun Film tidak boleh kosong"
                etTahun.requestFocus()
                return@setPositiveButton
            }
            val movie = Movies(movie.id, namaJudul,movie.poster,keterangan, tahun)
            movieList.child(movie.id!!).setValue(movie)
            Toast.makeText(mContext,"Update data sukses", Toast.LENGTH_LONG).show()
        }
        /**
         * @desc Code untuk menutup layar popup
         */
        builder.setNeutralButton("No"){p0,p1 ->
        }

        /**
         * @desc Code untuk menghapus value ke firebase
         */
        builder.setNegativeButton("Delete"){p0,p1 ->
            val delMhs = FirebaseDatabase.getInstance().getReference("movieList").child(movie.id)
            delMhs.removeValue()
            Toast.makeText(mContext,"Data Sudah dihapus", Toast.LENGTH_LONG).show()
        }

        val alert = builder.create()
        alert.show()
    }


}