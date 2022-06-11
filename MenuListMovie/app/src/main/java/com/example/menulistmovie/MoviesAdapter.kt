package com.example.menulistmovie

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.concurrent.Executors

/**
 * @desc Karena untuk menjebatani data dengan tampilan listview maka kelompok kami memutuskan
 * untuk menggunakan adapter
 */
class MoviesAdapter(val mContext : Context, val layoutResId : Int, val movList: List<Movies>) : ArrayAdapter<Movies>(mContext,layoutResId,movList){
    /**
     * @desc Methode ini dibuat untuk memparsing tampilan dengan data yang ada
     */
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

        /**
         * @desc Code untuk menampilkan data ke layar popup
         */
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
}