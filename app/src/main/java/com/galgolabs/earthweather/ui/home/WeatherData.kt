package com.galgolabs.earthweather.ui.home

data class WeatherData (val name:String, val id: Int, val cod: Int,
                        val weather: ArrayList<Weather>, val coord: Coords,
                        val main: Main, val wind: Wind, val sys: Sys,
                        val visibility: Int, val dt: Long, val clouds: Cloud)

data class Weather (val id: Int, val main: String, val description: String)

data class Coords (val lat: Double, val lon: Double)

data class Main (val temp: Float, val feels_like: Float, val temp_min: Float,
                 val temp_max: Float, val pressure: Int, val humidity: Int,
                 val sea_level: Int, val grnd_level: Int, val timezone: Int,
                 val icon: String )

data class Wind (val speed: Float, val deg: Int, val gust: Float)

data class Sys (val country: String, val sunrise: Long, val sunset: Long)

data class Cloud (val all: Int)