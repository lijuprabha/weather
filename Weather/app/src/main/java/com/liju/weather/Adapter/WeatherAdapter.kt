package com.liju.weather.Adapter

import WeatherWrapper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.google.gson.Gson
import com.liju.weather.R
import com.liju.weather.db.Weather
import kotlinx.android.synthetic.main.item_weather.view.*

class WeatherAdapter(internal val items: MutableList<Weather>?, private val listener: WeatherItemClick) : androidx.recyclerview.widget.RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false))
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }



    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            val item = items?.get(position)
            val topic = Gson().fromJson(item?.json_data, WeatherWrapper::class.java)
            itemView.txt_temperature.text = topic?.main?.getKelvinToCelsius()
            itemView.txt_created_Time.text = item?.getFormattedDateFromTimestamp()
            itemView.txt_place.text=topic?.name
            itemView.moreOptions.setOnClickListener {
                showMenusOptions(it,position)
            }
        }

    }

    private fun showMenusOptions(v: View, position: Int) {
        val popup = PopupMenu(v.context, v)
        popup.menuInflater.inflate(R.menu.popup_menu_option, popup.menu)
        popup.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menuEdit -> {

                }
                R.id.menuDelete -> {
                    listener.onItemClickDelete(position)
                }
            }
            true
        }
        popup.show()
    }

    interface WeatherItemClick {
        fun onItemClickDelete(position:Int)
    }
}