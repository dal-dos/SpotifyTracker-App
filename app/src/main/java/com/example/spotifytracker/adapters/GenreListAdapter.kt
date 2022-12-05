package com.example.spotifytracker.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.spotifytracker.R

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

        setTitleText(view, position)
        setSubText(view, position)

        return view
    }

    private fun setSubText(view: View, position: Int) {
        val itemSubTextView =  view.findViewById<TextView>(R.id.itemSubText)
        itemSubTextView.text = ""
        itemSubTextView.isSelected = true
    }

    private fun setTitleText(view: View, position: Int) {
        val itemTitleTextView = view.findViewById<TextView>(R.id.itemTitleText)
        itemTitleTextView.text = spotifyFavGenre[position].split(' ').joinToString(" ") { words ->
            words.replaceFirstChar { firstLetter ->
                firstLetter.uppercase()
            }
        }
        itemTitleTextView.isSelected = true
    }


}