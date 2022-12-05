package com.example.spotifytracker.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.adamratzman.spotify.models.Track
import com.example.spotifytracker.R
import com.squareup.picasso.Picasso

class TrackListAdapter(private val context: Context, private var spotifyFavTrack: List<Track>) : BaseAdapter(){
    override fun getCount(): Int {
        return spotifyFavTrack.size
    }

    override fun getItem(position: Int): Any {
        return spotifyFavTrack[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun replace(newSpotifyData: List<Track>){
        spotifyFavTrack = newSpotifyData
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
        Picasso.get().load(spotifyFavTrack[position].album.images[0].url).into(image);
    }

    private fun setSubText(view: View, position: Int) {
        val itemSubText =  view.findViewById<TextView>(R.id.itemSubText)
        var myArtists = ""
        spotifyFavTrack[position].artists.forEach { it ->
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
        val track : String = spotifyFavTrack[position].name
        itemTitleText.text = track
        itemTitleText.isSelected = true
    }


}