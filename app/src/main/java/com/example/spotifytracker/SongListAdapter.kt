package com.example.spotifytracker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.adamratzman.spotify.models.PlayHistory

class SongListAdapter(private val context: Context, private var spotifyRecentlyPlayed: List<PlayHistory>) : BaseAdapter(){

    override fun getCount(): Int {
        return spotifyRecentlyPlayed.size
    }

    override fun getItem(position: Int): Any {
        return spotifyRecentlyPlayed[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun replace(newSpotifyData: List<PlayHistory>){
        spotifyRecentlyPlayed = newSpotifyData
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.song_item,null)
        val songTitleTexView = view.findViewById<TextView>(R.id.songTitleText)
        val songArtistsTextView =  view.findViewById<TextView>(R.id.songArtistsText)

        songTitleTexView.text = spotifyRecentlyPlayed[position].track.name
        var myArtists = ""
        spotifyRecentlyPlayed[position].track.artists.forEach { it ->
            if(myArtists == ""){
                myArtists = it.name
            }else{
                myArtists = "$myArtists, ${it.name}"
            }
        }
        songArtistsTextView.text = myArtists
        return view
    }
}