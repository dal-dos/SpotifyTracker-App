package com.example.spotifytracker.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.adamratzman.spotify.models.Artist
import com.example.spotifytracker.R
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*

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

        setTitleText(view, position)
        setSubText(view, position)
        setImage(view,position)

        return view
    }

    private fun setImage(view: View, position: Int) {
        val image = view.findViewById<ImageView>(R.id.imageView)
        Picasso.get().load(spotifyFavArtist[position].images[0].url).into(image);
    }

    private fun setSubText(view: View, position: Int) {
        val itemSubText =  view.findViewById<TextView>(R.id.itemSubText)
        var followers : String? = getFormattedAmount(spotifyFavArtist[position].followers.total?.toInt()!!)
        itemSubText.text = "$followers followers"
        itemSubText.isSelected = true
    }

    private fun setTitleText(view: View, position: Int) {
        val itemTitleText = view.findViewById<TextView>(R.id.itemTitleText)
        val artist : String = spotifyFavArtist[position].name
        itemTitleText.text = artist
        itemTitleText.isSelected = true
    }

    private fun getFormattedAmount(amount: Int): String? {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

}