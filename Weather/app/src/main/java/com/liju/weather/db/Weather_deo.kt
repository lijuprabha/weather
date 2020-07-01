package com.liju.weather.db

import androidx.room.*

@Dao
interface Weather_deo {
    @Query("SELECT * FROM Weather ORDER BY date_time DESC")
    fun getAll(): MutableList<Weather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weather: Weather)

    @Query("DELETE FROM Weather WHERE date_time = :time")
    fun delete(time: Long?)


    @Query("DELETE FROM Weather")
    fun deleteAll()

    @Query("SELECT * FROM Weather WHERE date_time = :time")
    fun findByTime(time: Long?): Weather

}