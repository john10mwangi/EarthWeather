package com.galgolabs.earthweather.ui.home

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.Observable
import androidx.lifecycle.*
import com.galgolabs.earthweather.ui.CompassUtil
import com.galgolabs.earthweather.ui.UtilDate
import com.galgolabs.earthweather.ui.localDB.MiniClimate
import com.galgolabs.earthweather.ui.localDB.MiniWeather
import com.galgolabs.earthweather.ui.localDB.MiniWeatherData
import kotlinx.coroutines.launch
import java.util.Date

//@HiltViewModel
class HomeViewModel(private val repo: Repository) : ViewModel(), Observable {
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

    suspend fun insertData(climate: WeatherData){
        repo.insert(climate)
    }

    val weather : LiveData<List<MiniClimate>> = repo.weather.asLiveData()

    val allWeather: LiveData<List<MiniWeatherData>> = repo.allWeather.asLiveData()

    var fetchResult: MutableLiveData<NetworkResponse> = repo.result

    //
    fun fetchMyLocation(lat: Double, lng: Double, preferences: SharedPreferences, isFromTown: Boolean = false) {
        sharedPreferences = preferences
        val lastUpdate = preferences.getLong("lastUpdate", 0)
        println("lastupdate : $lastUpdate")
        val now = System.currentTimeMillis()
        val minEllapsedMillisecs = 1000*60*60
        if (isFromTown){
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun devolve(weatherData: WeatherData) {
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
        rangeTemp.value = weatherData.main.temp_max.toString()+
                " - "+weatherData.main.temp_min.toString()
        forecast.value = weatherData.weather[0].main
        averageTemp.value = weatherData.main.temp.toString()

        val direction = compassUtil.degreesToCampassDirection(weatherData.wind.deg)
        windDirection.value = direction.toString()

        val timeRise = utilDate.milliToHoursAndMinutes(weatherData.sys.sunrise)
        sunRise.value = timeRise

        val timeSet = utilDate.milliToHoursAndMinutes(weatherData.sys.sunset)
        sunSet.value = timeSet

        val speed = weatherData.wind.speed.toString()+" m/s"
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
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}