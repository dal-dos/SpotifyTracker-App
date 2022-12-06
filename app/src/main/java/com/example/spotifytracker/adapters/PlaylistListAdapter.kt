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
import com.example.spotifytracker.ui.playlists.SpotifyPlaylist
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*

class PlaylistListAdapter(private val context: Context, private var spotifyPlaylists: List<SpotifyPlaylist>) : BaseAdapter(){
    override fun getCount(): Int {
        return spotifyPlaylists.size
    }

    override fun getItem(position: Int): Any {
        return spotifyPlaylists[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun replace(newSpotifyData: List<SpotifyPlaylist>){
        spotifyPlaylists = newSpotifyData
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.song_item,null)

        setTitleText(view, position)
        setSubText(view, position)
        setImage(view,position)
        view.tag = setLink(view, position)
        return view
    }

    private fun setLink(view: View, position: Int): String {
        return spotifyPlaylists[position].webLink
    }

    private fun setImage(view: View, position: Int) {
        val image = view.findViewById<ImageView>(R.id.imageView)
        Picasso.get().load(spotifyPlaylists[position].imageUrl).into(image);
    }

    private fun setSubText(view: View, position: Int) {
        val itemSubText =  view.findViewById<TextView>(R.id.itemSubText)
        val weatherType = spotifyPlaylists[position].weatherType

        itemSubText.text = "Weather: $weatherType"
        itemSubText.isSelected = true
    }

    private fun setTitleText(view: View, position: Int) {
        val itemTitleText = view.findViewById<TextView>(R.id.itemTitleText)
        val playlistName : String = spotifyPlaylists[position].title
        itemTitleText.text = playlistName
        itemTitleText.isSelected = true
    }


}