package com.galgolabs.earthweather

import android.app.Application
import com.galgolabs.earthweather.ui.home.Repository
import com.galgolabs.earthweather.ui.localDB.WeatherAppRoomDB
import com.galgolabs.earthweather.ui.localDB.WeatherDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class EarthWeatherApp: Application() {

    val  applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy {WeatherAppRoomDB.getDatabase(this, applicationScope)}

    val repository by lazy {Repository(database.weatherDao())}
}