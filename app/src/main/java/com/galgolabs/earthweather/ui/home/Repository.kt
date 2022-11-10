package com.galgolabs.earthweather.ui.home

import androidx.lifecycle.MutableLiveData
import com.galgolabs.earthweather.ui.WebServices
import com.galgolabs.earthweather.ui.localDB.MiniClimate
import com.galgolabs.earthweather.ui.localDB.MiniWeatherData
import com.galgolabs.earthweather.ui.localDB.WeatherDAO
import kotlinx.coroutines.flow.Flow
import java.util.Date

class Repository(private val weatherDAO: WeatherDAO)
    : WebServices.ResultsCallback{
    private var webServices: WebServices = WebServices()

    var result: MutableLiveData<NetworkResponse> = MutableLiveData()

    var weather: Flow<List<MiniClimate>> = weatherDAO.fetch()

    var allWeather: Flow<List<MiniWeatherData>> = weatherDAO.fetchWeather()

    init {
        webServices.callback = this
    }

    suspend fun fetchData(lat: Double, lng: Double) = webServices.fetchWeatherByLatLng(lat, lng)

    override fun onRetrievedSuccess(data: WeatherData) {
        result.value = NetworkResponse(data)
    }

    suspend fun insert(climate: WeatherData){
        weatherDAO.createWeatherData(climate)
    }

}