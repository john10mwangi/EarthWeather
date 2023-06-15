package com.galgolabs.earthweather.ui

import com.galgolabs.earthweather.ui.home.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
object ApiModule {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/weather/"
//    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/forecast/hourly/"

    private const val extra = "lat=-0.29473234067640336&lon=36.13100872582516&appid=17ffb6660023c4cd2d7e3fb43c9960c3&mode=json"


    fun instance() : Retrofit {
        val mHttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val mOkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(mHttpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(mOkHttpClient)
            .build()

        return retrofit
    }
}

object ApiModule2 {
    private const val BASE_URL = "https://us1.locationiq.com/v1/"

    fun instance() : Retrofit {
        val mHttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val mOkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(mHttpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(mOkHttpClient)
            .build()

        return retrofit
    }
}