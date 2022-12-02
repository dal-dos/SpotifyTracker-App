package com.example.spotifytracker.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.adamratzman.spotify.utils.Language
import com.example.spotifytracker.R

class GenreListAdapter(private val context: Context, private var spotifyFavGenre: ArrayList<String>) : BaseAdapter(){
    override fun getCount(): Int {
        return spotifyFavGenre.size
    }

    override fun getItem(position: Int): Any {

        return spotifyFavGenre[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun replace(newSpotifyData: ArrayList<String>){

        spotifyFavGenre = newSpotifyData


    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.song_item,null)
        val itemTitleTextView = view.findViewById<TextView>(R.id.itemTitleText)
        val itemSubTextView =  view.findViewById<TextView>(R.id.itemSubText)



        itemTitleTextView.text = spotifyFavGenre[position].split(' ').joinToString(" ") {
            it.replaceFirstChar {
                it.uppercase()

            }
        }
        val myArtists = ""

        itemSubTextView.text = myArtists

        return view
    }

    @SuppressLint("SetTextI18n")
    fun listEmpty(): View {
        val linearLayout : LinearLayout = LinearLayout(context)
        val textView : TextView = TextView(context)
        textView.text = "None Found"
        linearLayout.addView(textView)
        return linearLayout
    }

}