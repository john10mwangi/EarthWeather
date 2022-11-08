package com.galgolabs.earthweather.ui

import android.app.Application
import com.galgolabs.earthweather.ui.home.Repository
import com.galgolabs.earthweather.ui.localDB.WeatherAppRoomDB
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class EarthWeather: Application() {

	val  applicationScope = CoroutineScope(SupervisorJob())

	val database by lazy { WeatherAppRoomDB.getDatabase(this, applicationScope)}

	val repository by lazy { Repository(database.weatherDao()) }
}