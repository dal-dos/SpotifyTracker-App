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

        setTitleText(view, position)
        setSubText(view, position)
        setImage(view,position)

        return view
    }

    private fun setImage(view: View, position: Int) {
        val image = view.findViewById<ImageView>(R.id.imageView)
        Picasso.get().load(spotifyRecentlyPlayed[position].track.album.images[0].url).into(image);
    }

    private fun setSubText(view: View, position: Int) {
        var myArtists = ""
        val itemSubText =  view.findViewById<TextView>(R.id.itemSubText)
        spotifyRecentlyPlayed[position].track.artists.forEach { it ->
            if(myArtists == ""){
                myArtists = it.name
            }else{
                myArtists = "$myArtists, ${it.name}"
            }
        }
        itemSubText.text = myArtists
        itemSubText.isSelected = true
    }

    private fun setTitleText(view: View, position: Int) {
        val itemTitleText = view.findViewById<TextView>(R.id.itemTitleText)
        itemTitleText.text = spotifyRecentlyPlayed[position].track.name
        itemTitleText.isSelected = true
    }


}