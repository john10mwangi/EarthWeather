package com.galgolabs.earthweather.ui

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.galgolabs.earthweather.ui.home.TownData
import com.galgolabs.earthweather.ui.home.WeatherData

class WebServices {
    private val urlStr = "https://api.openweathermap.org/data/2.5/weather?lat=-0.29473234067640336&lon=36.13100872582516&appid=17ffb6660023c4cd2d7e3fb43c9960c3&mode=xml"
    private val baseUrl = "https://api.openweathermap.org/data/2.5/weather?"
    private val apiKey = "17ffb6660023c4cd2d7e3fb43c9960c3"
    private val returnMode = "json"
    private val returnUnit = "metric"
    private val returnLang = "en"
    lateinit var callback : ResultsCallback

    suspend fun fetchWeatherByLatLng(lat: Double, lng: Double){
//        val url = baseUrl+"lat=$lat&lon=$lng&mode=$returnMode&units=$returnUnit&lang=$returnLang&appid=$apiKey"
        postRequest(lat,lng)
    }

    suspend fun postRequest(lat: Double, lng: Double){
        val retrofit = ApiModule.instance()
        val api = retrofit.create(WebServiceInterface.WebServiceInterface::class.java)
        try {
            val  response = api.getWeather(unit = returnUnit, form = returnMode,
                ln = returnLang, lat = lat, lng = lng)

            if (response.isSuccessful){
                response.body()?.let { callback.onRetrievedSuccess(data= it) }
                println(response.body().toString())
            }else {
                println("Failed")
            }
        }catch (Ex: java.lang.Exception){
            Ex.localizedMessage?.let { Log.e("Error", it) }
        }
    }

    suspend fun fetchTown(value: String) {
        val retrofit = ApiModule2.instance()
        val api = retrofit.create(WebServiceInterface.WebServiceInterface::class.java)
        println("fetchTown")
        try {
            val  response = api.getTown(path = "search",key = "pk.65543f2ae9fb6a50584ac5d4e0eed386", q = value, format = returnMode)

            if (response.isSuccessful){
                response.body()?.let { callback.onRetrievedSuccess(data= it) }
                println("Places are : ${response.body().toString()}")
            }else {
                println("Failed")
            }
        }catch (Ex: java.lang.Exception){
            Ex.localizedMessage?.let { Log.e("Error", it) }
        }
    }

    interface ResultsCallback {
        fun onRetrievedSuccess(data : WeatherData)
        fun onRetrievedSuccess(data : ArrayList<TownData>)
//        fun onRetrievedSuccess(data: TownData)
    }
}