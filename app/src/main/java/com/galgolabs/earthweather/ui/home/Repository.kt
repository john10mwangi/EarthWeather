package com.galgolabs.earthweather.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
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

    var res: MutableLiveData<Long> = MutableLiveData()

    var weather: Flow<List<MiniClimate>> = weatherDAO.fetch()

    var weatherRefreshed: MutableLiveData<MiniWeatherData>  = MutableLiveData()

    var allWeather: Flow<List<MiniWeatherData>> = weatherDAO.fetchWeather()

    init {
        webServices.callback = this
    }

    suspend fun fetchData(lat: Double, lng: Double) = webServices.fetchWeatherByLatLng(lat, lng)

    suspend fun fetchData(id: Long){
        weatherRefreshed.value = weatherDAO.fetch(id)
    }

    override fun onRetrievedSuccess(data: WeatherData) {
        result.value = NetworkResponse(data)
    }

    suspend fun insert(climate: WeatherData){
        res.value = weatherDAO.createWeatherData(climate)
    }

}