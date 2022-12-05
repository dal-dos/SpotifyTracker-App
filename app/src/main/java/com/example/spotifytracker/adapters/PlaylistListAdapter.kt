package com.example.spotifytracker.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.Playlist
import com.example.spotifytracker.MainActivity
import com.example.spotifytracker.R
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*

class PlaylistListAdapter(private val context: Context, private var spotifyPlaylists: List<Playlist>) : BaseAdapter(){
    override fun getCount(): Int {
        return spotifyPlaylists.size
    }

    override fun getItem(position: Int): Any {
        return spotifyPlaylists[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun replace(newSpotifyData: List<Playlist>){
        spotifyPlaylists = newSpotifyData
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.song_item,null)

        setTitleText(view, position)
        setSubText(view, position)
        setImage(view,position)

        return view
    }

    private fun setImage(view: View, position: Int) {
        val image = view.findViewById<ImageView>(R.id.imageView)
        Picasso.get().load(spotifyPlaylists[position].images[0].url).into(image);
    }

    private fun setSubText(view: View, position: Int) {
        val itemSubText =  view.findViewById<TextView>(R.id.itemSubText)
        val author = spotifyPlaylists[position].owner.displayName
        val id : String = spotifyPlaylists[position].id

        itemSubText.text = "$author"
        itemSubText.isSelected = true
    }

    private fun setTitleText(view: View, position: Int) {
        val itemTitleText = view.findViewById<TextView>(R.id.itemTitleText)
        val playlistName : String = spotifyPlaylists[position].name
        itemTitleText.text = playlistName
        itemTitleText.isSelected = true
    }


}