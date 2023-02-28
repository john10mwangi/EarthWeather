package com.galgolabs.earthweather.ui.home

class NetworkResponse(data: WeatherData) {
    var weatherData: WeatherData = data
//    lateinit var error: String

//    fun NetworkResponse(data: WeatherData) {
//        this.weatherData = data
//    }
//    fun NetworkResponse(error: String) {
//        this.error = error
//    }
}

class NetworkResponse2(private val data: ArrayList<TownData>)