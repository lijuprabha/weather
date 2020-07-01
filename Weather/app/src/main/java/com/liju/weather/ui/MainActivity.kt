package com.liju.weather.ui
import WeatherWrapper
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.liju.weather.Adapter.WeatherAdapter
import com.liju.weather.R
import com.liju.weather.db.AppDatabase
import com.liju.weather.db.Weather
import com.liju.weather.db.Weather_deo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_weather_list.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity(), WeatherAdapter.WeatherItemClick {
    private var data: MutableList<Weather>? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var weatherJson: String? = ""
    private var mToolbar: Toolbar? = null
    private lateinit var db: AppDatabase
    private lateinit var genderDao: Weather_deo
    private val httpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.action_refresh) {
            getCurrentWather()
            return true
        } else if (id == R.id.action_save) {
            if (!weatherJson?.isEmpty()!!)
                saveWeatherLocalStorage()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClickDelete(position: Int) {
        deleteDataFromLocalStorage(position)
    }

    private fun init() {
        val bundle = intent.extras
        latitude = bundle?.getDouble("LAT")
        longitude = bundle?.getDouble("LON")
        mToolbar = findViewById(R.id.toolbar)
        mToolbar?.title = ""
        setSupportActionBar(mToolbar)

        db = AppDatabase.getAppDataBase(context = this)!!
        genderDao = db.weather_deo()
        Glide.with(this).load(R.drawable.cloud).into(img_cloud)
        fetchDataFromLocalStorage()
        getCurrentWather()
    }

    private fun getCurrentWather() {
        val request = Request.Builder()
            .url("http://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&APPID=b1885e4909e63a732c2b8abd2eab829e")
            .get().build()
        httpClient.newCall(request).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    showSnackBar(R.string.network_error)
                }
                override fun onResponse(call: Call, response: Response) {
                    dataUpdateToUi(response.body?.string())
                }
            }
        )
    }

    private fun dataUpdateToUi(data: String?) {
        runOnUiThread {
            weatherJson = data
            showSnackBar(R.string.data_refresh)
            val topic = Gson().fromJson(data, WeatherWrapper::class.java)
            mToolbar?.title = topic.name
            tv_temperature.text = topic.main.getKelvinToCelsius()
            todayHumidity.text= "Humidity : " + topic.main.humidity + " %"
            todaySunrise.text = "Sunrise  : " + topic.sys.getDate(topic.sys.sunrise)
            todaySunset.text  = "Sunset   : " + topic.sys.getDate(topic.sys.sunset)

        }
    }

    private fun saveWeatherLocalStorage() {
        AsyncTask.execute {
            val topic = Gson().fromJson(weatherJson, WeatherWrapper::class.java)
            val obj = Weather()
            obj.json_data = Gson().toJson(topic)
            obj.date_time = System.currentTimeMillis()
            genderDao.insertWeather(obj)
            showSnackBar(R.string.successfully_saved)
            fetchDataFromLocalStorage()
        }
    }

    private fun clearAllData(){
        AsyncTask.execute {
            genderDao.deleteAll()
            fetchDataFromLocalStorage()
        }
    }
    private fun fetchDataFromLocalStorage() {
        AsyncTask.execute {
            data = genderDao.getAll()
            updateWeatherList()
        }
    }
    private fun deleteDataFromLocalStorage(position: Int) {
        AsyncTask.execute {
            genderDao.delete(data?.get(position)?.date_time)
            data?.removeAt(position)
        }

        rv_work_details.adapter?.notifyDataSetChanged()
    }

    private fun updateWeatherList() {
        runOnUiThread {
            rv_work_details.adapter = WeatherAdapter(data, this)
            rv_work_details.layoutManager = LinearLayoutManager(this)
        }
    }
    private fun showSnackBar(stringResourceId:Int){
        Snackbar.make(main_view,stringResourceId
            , Snackbar.LENGTH_LONG).show()
    }

    private fun confirmationForClearDataFromLocalStorage(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.clear_warning)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes"){ _, _ ->
            clearAllData()
        }
        builder.setNegativeButton("No"){ _, _ ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun clearAllData(view: View) {
        if(data!=null&&data!!.size>0)
        confirmationForClearDataFromLocalStorage()
    }


}
