package com.galgolabs.earthweather.ui.home

import androidx.lifecycle.MutableLiveData
import com.galgolabs.earthweather.ui.WebServices
import com.galgolabs.earthweather.ui.localDB.MiniClimate
import com.galgolabs.earthweather.ui.localDB.WeatherDAO
import kotlinx.coroutines.flow.Flow

class Repository(private val weatherDAO: WeatherDAO)
    : WebServices.ResultsCallback{
    private var webServices: WebServices = WebServices()

    var result: MutableLiveData<NetworkResponse> = MutableLiveData()

    var allWeather: Flow<List<MiniClimate>> = weatherDAO.fetch()

    init {
        webServices.callback = this
    }

    suspend fun fetchData(lat: Double, lng: Double) = webServices.fetchWeatherByLatLng(lat, lng)

    override fun onRetrievedSuccess(data: WeatherData) {
        result.value = NetworkResponse(data)
    }

    suspend fun insert(climate: MiniClimate){
        weatherDAO.create(climate)
    }

}