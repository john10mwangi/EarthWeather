package com.galgolabs.earthweather.ui.home

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.Observable
import androidx.lifecycle.*
import com.galgolabs.earthweather.ui.CompassUtil
import com.galgolabs.earthweather.ui.UtilDate
import com.galgolabs.earthweather.ui.dashboard.DashboardViewModel
import com.galgolabs.earthweather.ui.localDB.MiniWeatherData
import kotlinx.coroutines.launch
import java.util.*

//@HiltViewModel
class HomeViewModel(private val repo: Repository) : ViewModel(), Observable {
    private val CONSTCONVERT: Float = 1.609344F
    private var compassUtil = CompassUtil()
    private var utilDate = UtilDate()

    private lateinit var sharedPreferences: SharedPreferences

    var locationName: MutableLiveData<String> = MutableLiveData()
    var averageTemp: MutableLiveData<String> = MutableLiveData()
    var rangeTemp: MutableLiveData<String> = MutableLiveData()
    var windSpeed: MutableLiveData<String> = MutableLiveData()
    var windDirection: MutableLiveData<String> = MutableLiveData()
    var sunSet: MutableLiveData<String> = MutableLiveData()
    var sunRise: MutableLiveData<String> = MutableLiveData()
    var forecast: MutableLiveData<String> = MutableLiveData()
    var humidity: MutableLiveData<Int> = MutableLiveData()

    var refreshNeeded : MutableLiveData<Long> = repo.res

    suspend fun insertData(climate: WeatherData){
        repo.insert(climate)
    }

    val weather : MutableLiveData<MiniWeatherData> = repo.weatherRefreshed

    val allWeather: LiveData<List<MiniWeatherData>> = repo.allWeather.asLiveData()

    var fetchResult: MutableLiveData<NetworkResponse> = repo.result

    //
    fun fetchMyLocation(lat: Double, lng: Double,
                        preferences: SharedPreferences, isFromTown: Boolean = false) {
        sharedPreferences = preferences
        val lastUpdate = preferences.getLong("lastUpdate", 0)
        val now = System.currentTimeMillis()
        val minEllapsedMillisecs = 1000*60*60
        if (isFromTown){
            println("lastupdate : $lastUpdate")
            viewModelScope.launch {
                repo.fetchData(lat, lng)
            }
        }else if (lastUpdate > 0 ) {
            val date = Date(lastUpdate)
            if (now.minus(date.time) >= minEllapsedMillisecs){
                viewModelScope.launch {
                    repo.fetchData(lat, lng)
                }
            }
        }else {
            viewModelScope.launch {
                repo.fetchData(lat, lng)
            }
        }
    }

    fun fetchById(id: Long) {
        viewModelScope.launch {
            repo.fetchData(id)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun devolve(weatherData: WeatherData, preferences: SharedPreferences) {
        sharedPreferences = preferences
        try {
            val now = System.currentTimeMillis()
            viewModelScope.launch{
                insertData(weatherData)
                sharedPreferences.edit().putLong("lastUpdate", now).apply()
            }
        }catch (ex: java.lang.Exception){
            ex.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun populate(weatherData: MiniWeatherData) {
        locationName.value = weatherData.climate.name
        averageTemp.value = weatherData.main.temp.toString()
        rangeTemp.value = String.format(Locale.getDefault(), "%.0f / %.0f",
            weatherData.main.temp_min,weatherData.main.temp_max)
        forecast.value = weatherData.weather[0].main
        averageTemp.value = weatherData.main.temp.toString()
        humidity.value = weatherData.main.humidity

        val direction = compassUtil.degreesToCampassDirection(weatherData.wind.deg)
        windDirection.value = direction.toString()

        sunRise.value = utilDate.longToDate(weatherData.sys.sunrise)
        sunSet.value = utilDate.longToDate(weatherData.sys.sunset)

        val speed = String.format(Locale.getDefault(), "%.0f KM/H",
            (weatherData.wind.speed * CONSTCONVERT))
        windSpeed.value = speed
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(DashboardViewModel::class.java)){
            return DashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}