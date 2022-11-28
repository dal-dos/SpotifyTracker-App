package com.example.spotifytracker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.PlayHistory
import com.adamratzman.spotify.models.Track
import com.squareup.picasso.Picasso

class ArtistListAdapter(private val context: Context, private var spotifyFavArtist: List<Artist>) : BaseAdapter(){
    override fun getCount(): Int {
        return spotifyFavArtist.size
    }

    override fun getItem(position: Int): Any {
        return spotifyFavArtist[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun replace(newSpotifyData: List<Artist>){
        spotifyFavArtist = newSpotifyData
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.song_item,null)

        val itemTitleText = view.findViewById<TextView>(R.id.itemTitleText)
        val itemSubText =  view.findViewById<TextView>(R.id.itemSubText)
        val image = view.findViewById<ImageView>(R.id.imageView)

        var artist : String = spotifyFavArtist[position].name
        itemTitleText.text = artist

        var popularity : String = spotifyFavArtist[position].popularity.toString()
        var followers : String = spotifyFavArtist[position].followers.total.toString()
        itemSubText.text = "$followers followers"
        itemSubText.isSelected = true
        itemTitleText.isSelected = true
        //        println("debug: Image address is " + spotifyRecentlyPlayed[position].track.album.images[0].url)
        //image.setImageResource(R.drawable.ic_spotify_icon)
        Picasso.get().load(spotifyFavArtist[position].images[0].url).into(image);
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