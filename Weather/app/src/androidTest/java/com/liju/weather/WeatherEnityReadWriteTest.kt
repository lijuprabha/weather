package com.liju.weather

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.liju.weather.db.AppDatabase
import com.liju.weather.db.Weather
import com.liju.weather.db.Weather_deo
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WeatherEnityReadWriteTest {

    private lateinit var weatherDeo: Weather_deo
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getContext()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        weatherDeo = db.weather_deo()
    }
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val weather = Weather()
        weather.json_data="{samples}"
        weather.date_time=System.currentTimeMillis()
        weatherDeo.insertWeather(weather)
        val wetherItem = weatherDeo.findByTime(weather.date_time)
        Assert.assertEquals(wetherItem,weather)
    }
}