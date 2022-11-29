package com.example.spotifytracker.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.adamratzman.spotify.models.PlayHistory
import com.example.spotifytracker.R
import com.squareup.picasso.Picasso

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
        val itemTitleText = view.findViewById<TextView>(R.id.itemTitleText)
        val itemSubText =  view.findViewById<TextView>(R.id.itemSubText)
        val image = view.findViewById<ImageView>(R.id.imageView)
        val layout = view.findViewById<LinearLayout>(R.id.linearlayout)

        itemTitleText.text = spotifyRecentlyPlayed[position].track.name

        var myArtists = ""
        spotifyRecentlyPlayed[position].track.artists.forEach { it ->
            if(myArtists == ""){
                myArtists = it.name
            }else{
                myArtists = "$myArtists, ${it.name}"
            }
        }
        itemSubText.text = myArtists
        itemSubText.isSelected = true
        itemTitleText.isSelected = true
        //println("debug: Image address is " + spotifyRecentlyPlayed[position].track.album.images[0].url)
        //image.setImageResource(R.drawable.ic_spotify_icon)
        Picasso.get().load(spotifyRecentlyPlayed[position].track.album.images[0].url).into(image);

        return view
    }


}