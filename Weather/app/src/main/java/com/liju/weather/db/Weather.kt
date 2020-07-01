package com.liju.weather.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Weather (@PrimaryKey(autoGenerate = true) var id: Int?=null,
                    @ColumnInfo(name = "weather_json_data") var json_data: String?="",
                    @ColumnInfo(name = "date_time") var date_time: Long?=null){

    fun getFormattedDateFromTimestamp(): String? {
        val date = Date()
        date_time?.let { date.time = it }
        return SimpleDateFormat("MMM d, yyyy hh:mm a", Locale.ENGLISH).format(date)
    }
}
