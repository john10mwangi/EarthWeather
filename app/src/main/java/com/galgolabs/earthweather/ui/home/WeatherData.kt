package com.galgolabs.earthweather.ui.home

import androidx.room.Entity


//@Entity(tableName = "climate_data")
data class WeatherData (
    val name:String,
    val id: Int,
    val cod: Int,
    val weather: ArrayList<Weather>,
    val coord: Coords,
    val main: Main,
    val wind: Wind,
    val sys: Sys,
    val visibility: Int,
    val dt: Long,
    val clouds: Cloud)

//@Entity(tableName = "weather_data")
data class Weather (
    val id: Int,
    val main: String,
    val description: String)

//@Entity(tableName = "coods_data")
data class Coords (
    val lat: Double,
    val lon: Double)

//@Entity(tableName = "main_data")
data class Main (
    val temp: Float,
    val feels_like: Float,
    val temp_min: Float,
    val temp_max: Float,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int,
    val grnd_level: Int,
    val timezone: Int,
    val icon: String )

//@Entity(tableName = "wind_data")
data class Wind (
    val speed: Float,
    val deg: Int,
    val gust: Float)

//@Entity(tableName = "sys_data")
data class Sys (
    val country: String,
    val sunrise: Long,
    val sunset: Long)

@Entity(tableName = "cloud_data")
data class Cloud (val all: Int)