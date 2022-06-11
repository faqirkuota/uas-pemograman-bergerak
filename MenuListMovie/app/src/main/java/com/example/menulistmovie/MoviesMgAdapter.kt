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


class MoviesMgAdapter (val mContext : Context, val layoutResId : Int, val movList: List<Movies>) : ArrayAdapter<Movies>(mContext,layoutResId,movList){
    var holder: Holder? = null
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mContext)
        val view : View = layoutInflater.inflate(layoutResId, null)
        val tvNamaMg : TextView = view.findViewById(R.id.tvNamaMg)
        val tvTahunMg : TextView = view.findViewById(R.id.tvTahunMg)
        val ivPosterMg : ImageView = view.findViewById(R.id.ivPosterMg)
//        val bEditMg : TextView = view.findViewById(R.id.bEditMg)
        val bDeleteMg : TextView = view.findViewById(R.id.bDeleteMg)
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
        holder = Holder()
        holder!!.buttonEditMg = view.findViewById(R.id.bEditMg) as TextView
        holder!!.buttonDeleteMg = view.findViewById(R.id.bDeleteMg) as TextView
        return view
    }

    class Holder {
        var buttonEditMg: TextView? = null
        var buttonDeleteMg: TextView? = null
    }

}