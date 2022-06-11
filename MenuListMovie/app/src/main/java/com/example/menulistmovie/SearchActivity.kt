package com.example.menulistmovie

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject
import java.util.concurrent.Executors

class SearchActivity : AppCompatActivity() {
    private lateinit var viewLogout: TextView
    private lateinit var otentikasi: FirebaseAuth
    private lateinit var judul: TextView
    private lateinit var tahun: TextView
    private lateinit var plot: TextView
    private lateinit var btnCari: Button
    private lateinit var btnManagement: Button
    private lateinit var btnTambah: Button
    private lateinit var txtNama: EditText
    private lateinit var poster: ImageView
    private lateinit var posterUrl: String
    private lateinit var ref : DatabaseReference
    private lateinit var idImdbFilm: String
    var image: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        ref = FirebaseDatabase.getInstance().getReference("movieList")
        otentikasi = FirebaseAuth.getInstance()
        judul = findViewById(R.id.judul)
        tahun = findViewById(R.id.tahun)
        plot = findViewById(R.id.plot)
        txtNama = findViewById(R.id.etnamaSearch)
        btnCari = findViewById(R.id.bSearchAdmin)
        btnManagement = findViewById(R.id.bManagementAdmin)
        btnTambah = findViewById(R.id.bAddFilm)
        poster = findViewById(R.id.poster)
        posterUrl = "";
        idImdbFilm = "";
        viewLogout =  findViewById(R.id.logout)
        viewLogout.setOnClickListener {
            otentikasi.signOut()
            Intent(this@SearchActivity, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
        btnCari.setOnClickListener {
            tampilData(txtNama.text.toString())
        }

        btnTambah.setOnClickListener {
            simpanData(idImdbFilm, judul.text.toString(), tahun.text.toString(), plot.text.toString(), posterUrl)
        }
        btnManagement.setOnClickListener {
            Intent(this@SearchActivity, ManagementMoviesActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }

    }

    private fun tampilData(judulFilm: String) {
        val url = "http://www.omdbapi.com/?apikey=ac9be4e3&t=$judulFilm"
        val queue = Volley.newRequestQueue(this)
        // Declaring executor to parse the URL
        val executor = Executors.newSingleThreadExecutor()

        // Once the executor parses the URL
        // and receives the image, handler will load it
        // in the ImageView
        val handler = Handler(Looper.getMainLooper())
        // Request a string response from the provided URL.
        val stringReq = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->

                var strResp = response.toString()
                val jsonObj: JSONObject = JSONObject(strResp)
                val titleResponse: String = jsonObj.getString("Title")
                val yearResponse: String = jsonObj.getString("Year")
                val plotResponse: String = jsonObj.getString("Plot")
                val posterResponse: String = jsonObj.getString("Poster")
                val idImdbResponse: String = jsonObj.getString("imdbID")

                executor.execute {

                    // Tries to get the image and post it in the ImageView
                    // with the help of Handler
                    try {
                        val `in` = java.net.URL(posterResponse).openStream()
                        image = BitmapFactory.decodeStream(`in`)

                        // Only for making changes in UI
                        handler.post {
                            poster.setImageBitmap(image)
                        }
                    }

                    // If the URL doesnot point to
                    // image or any other kind of failure
                    catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                judul.text = titleResponse
                tahun.text = yearResponse
                plot.text = plotResponse
                idImdbFilm = idImdbResponse
                posterUrl = posterResponse

            },
            Response.ErrorListener { error -> error.printStackTrace() })
        queue.add(stringReq)
    }

    private fun simpanData(idImdbFilm:String, judulFilm: String, yearFilm:String, plotFilm:String, posterFilmUrl:String){
        val idImdbFilm = ref.push().key

        val mhs = Movies(idImdbFilm!!, judulFilm, posterFilmUrl, plotFilm, yearFilm)
        ref.child(idImdbFilm).setValue(mhs).addOnCompleteListener {
            Toast.makeText(applicationContext, "Data berhasil di simpan", Toast.LENGTH_LONG).show()
        }
    }
}