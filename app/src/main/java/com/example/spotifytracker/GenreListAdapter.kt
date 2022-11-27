package com.example.spotifytracker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.adamratzman.spotify.models.PlayHistory

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
        val songTitleTexView = view.findViewById<TextView>(R.id.songTitleText)
        val songArtistsTextView =  view.findViewById<TextView>(R.id.songArtistsText)

        songTitleTexView.text = spotifyFavGenre[position]
        val myArtists = ""
        songArtistsTextView.text = myArtists

        return view
    }

}