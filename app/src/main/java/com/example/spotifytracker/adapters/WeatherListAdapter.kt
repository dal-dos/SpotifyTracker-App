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
import com.example.spotifytracker.WeatherObject
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class WeatherListAdapter(private val context: Context, private var futureWeather: List<WeatherObject>) : BaseAdapter(){
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

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.weather_item,null)

        setTitleText(view, position)
        setSubText(view, position)
        setImage(view,position)
        setTemp(view,position)
        setTime(view,position)

        return view
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


}