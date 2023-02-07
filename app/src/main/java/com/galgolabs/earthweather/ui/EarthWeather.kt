package com.galgolabs.earthweather.ui

import android.app.Application
import android.content.Context
import com.galgolabs.earthweather.ui.home.Repository
import com.galgolabs.earthweather.ui.localDB.WeatherAppRoomDB
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class EarthWeather : Application() {
	lateinit var instance: Application

	val  applicationScope = CoroutineScope(SupervisorJob())

	val database by lazy { WeatherAppRoomDB.getDatabase(this, applicationScope)}

	val repository by lazy { Repository(database.weatherDao()) }

	override fun onCreate() {
		super.onCreate()
		instance = this
	}

	public fun getAppContext(): Context {
		return applicationContext
	}
}