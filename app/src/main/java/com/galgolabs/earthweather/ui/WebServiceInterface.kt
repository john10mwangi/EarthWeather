package com.galgolabs.earthweather.ui

import com.galgolabs.earthweather.ui.home.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class WebServiceInterface {
    interface WebServiceInterface{
//        @GET("?lat=-0.29473234067640336&lon=36.13100872582516&appid=17ffb6660023c4cd2d7e3fb43c9960c3&mode=json")
        @GET("?appid=17ffb6660023c4cd2d7e3fb43c9960c3")
//        @GET("?lat={LAT}&lon={LNG}&mode={MODE}&units={UNITS}&lang={LANG}&appid=17ffb6660023c4cd2d7e3fb43c9960c3&mode=json")
//        suspend fun getWeather(@Path("LAT") LAT: Double, @Path("LNG") LNG: Double,
//                               @Path("MODE") MODE: String, @Path("UNITS") UNITS: String,
//                               @Path( "LANG") LANG: String): Response<WeatherData>
        suspend fun getWeather(
                @Query("units") unit: String, @Query("lang") ln: String,
                @Query("mode") form: String, @Query("lat") lat: Double,
                @Query("lon") lng: Double): Response<WeatherData>
    }
}