package com.example.spotifytracker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SongListAdapter(private val context: Context, private var spotifyDataEntity: List<SpotifyDataEntity>) : BaseAdapter(){

    override fun getCount(): Int {
        return spotifyDataEntity.size
    }

    override fun getItem(position: Int): Any {
        return spotifyDataEntity[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun replace(newSpotifyData: List<SpotifyDataEntity>){
        spotifyDataEntity = newSpotifyData
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.song_item,null)
        val songTitleTexView = view.findViewById<TextView>(R.id.songTitleText)
        val songArtistsTextView =  view.findViewById<TextView>(R.id.songArtistsText)

        songTitleTexView.text = spotifyDataEntity[position].recentlyPlayed
        //songArtistsTextView.text = spotifyDataEntity[position].recentlyPlayed
        return view
    }
}