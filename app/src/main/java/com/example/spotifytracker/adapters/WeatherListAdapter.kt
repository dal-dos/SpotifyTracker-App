package com.example.spotifytracker.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.spotifytracker.R
import com.example.spotifytracker.WeatherObject
import com.example.spotifytracker.ui.playlists.PlaylistsData
import com.example.spotifytracker.ui.playlists.SpotifyPlaylist
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class WeatherListAdapter(private val context: Context, private var futureWeather: List<WeatherObject>) : BaseAdapter(), AdapterView.OnItemClickListener{
    override fun getCount(): Int {
        return futureWeather.size
    }

    override fun getItem(position: Int): Any {
        return futureWeather[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun replace(newWeatherData: List<WeatherObject>){
        futureWeather = newWeatherData
    }

    private lateinit var IDmap: ArrayList<SpotifyPlaylist>
    private var randomNumber = 0

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.weather_item,null)

        setTitleText(view, position)
        setSubText(view, position)
        setImage(view,position)
        setTemp(view,position)
        setTime(view,position)
        setListView(view,position)

        return view
    }

    private fun setListView(view: View, position: Int) {
        val innerPlaylistListView = view.findViewById<ListView>(R.id.rec_tomorrow_list)
        innerPlaylistListView.onItemClickListener = this
        IDmap = PlaylistsData.allPlaylist
        var index = 0
        val currWeatherPlaylistIndices = arrayListOf<Int>()
        for(item in IDmap){
            if(item.weatherType == futureWeather[position].weather){
                currWeatherPlaylistIndices.add(index)
            }
            index += 1
        }
        if(currWeatherPlaylistIndices.isEmpty()){
            val emptyListAdapter = GenreListAdapter(context, arrayListOf("None Found"))
            innerPlaylistListView.adapter = emptyListAdapter
            setListViewHeightBasedOnChildren(innerPlaylistListView,view)
        }else {
            randomNumber = currWeatherPlaylistIndices.random()
            val currWeatherPlaylistArrayList = listOf(IDmap[randomNumber])
            innerPlaylistListView.adapter = PlaylistListAdapter(context, currWeatherPlaylistArrayList)
            setListViewHeightBasedOnChildren(innerPlaylistListView,view)
        }

    }

    private fun setImage(view: View, position: Int) {
        val image = view.findViewById<ImageView>(R.id.imageView)
        Picasso.get().load(futureWeather[position].icon).into(image);
    }

    private fun setSubText(view: View, position: Int) {
        val itemSubText =  view.findViewById<TextView>(R.id.itemSubText)
        val weatherDesc = futureWeather[position].weatherDesc.split(' ').joinToString(" ") {words ->
            words.replaceFirstChar {firstLetter->
                firstLetter.uppercase()
            }
        }
        itemSubText.text = weatherDesc
        itemSubText.isSelected = true
    }

    private fun setTemp(view: View, position: Int) {
        val itemTemp =  view.findViewById<TextView>(R.id.itemTemp)
        val weatherTemp = futureWeather[position].temp.roundToInt().toString() + "°C"
        itemTemp.text = weatherTemp
        itemTemp.isSelected = true
    }

    private fun setTime(view: View, position: Int) {
        val itemTemp =  view.findViewById<TextView>(R.id.itemDate)
        val weatherDate = futureWeather[position].time
        itemTemp.text = weatherDate
        itemTemp.isSelected = true
    }

    private fun setTitleText(view: View, position: Int) {
        val itemTitleText = view.findViewById<TextView>(R.id.curr_weather)
        val weather : String = futureWeather[position].weather
        itemTitleText.text = weather
        itemTitleText.isSelected = true
    }

    private fun setListViewHeightBasedOnChildren(listView: ListView, view: View) {
        val listAdapter = listView.adapter
            ?: // pre-condition
            return
        var totalHeight = listView.paddingTop + listView.paddingBottom
        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            (listItem as? ViewGroup)?.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            listItem.measure(view.measuredWidth, view.measuredHeight)

            totalHeight += listItem.measuredHeight
        }
        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (IDmap.isNotEmpty()){
            val hyperlink = IDmap[randomNumber].webLink
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(hyperlink))
            context.startActivity(intent)
        }
    }
}