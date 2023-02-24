package com.galgolabs.earthweather.ui

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.*

class WeatherWorkScheduler(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
	val returnMode = "json"
	val returnUnit = "metric"
	val returnLang = "en"
	override suspend fun doWork(): Result {
		val lat = inputData.getDouble("weather_lat", 0.0)
		val lng = inputData.getDouble("weather_lon", 0.0)
		println("doWork lat : $lat , lon : $lng")
		val retrofit = ApiModule.instance()
		val api = retrofit.create(WebServiceInterface.WebServiceInterface::class.java)
		try {
			val  response = api.getWeather(unit = returnUnit, form = returnMode,
				ln = returnLang, lat = lat, lng = lng)

			if (response.isSuccessful){

				response.body()?.let {
					val response = workDataOf("response" to it)
					return Result.success(response)
				}
				println(response.body().toString())
			}else {
				println("Failed")
				return Result.retry()
			}
		}catch (Ex: java.lang.Exception){
			val error = workDataOf("response" to Ex)
			Ex.localizedMessage?.let { Log.e("Error", it) }
			return Result.failure(error)
		}
		return Result.failure()
	}
}