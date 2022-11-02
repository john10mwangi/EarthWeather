package com.galgolabs.earthweather.ui.home

import androidx.lifecycle.MutableLiveData
import com.galgolabs.earthweather.ui.WebServices

class Repository(private var webServices: WebServices = WebServices())
    : WebServices.ResultsCallback{

    var result: MutableLiveData<NetworkResponse> = MutableLiveData()

    init {
        webServices.callback = this
    }

    suspend fun fetchData(lat: Double, lng: Double) = webServices.fetchWeatherByLatLng(lat, lng)

    override fun onRetrievedSuccess(data: WeatherData) {
        result.value = NetworkResponse(data)
    }

}