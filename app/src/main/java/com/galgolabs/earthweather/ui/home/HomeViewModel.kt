package com.galgolabs.earthweather.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.*
import com.galgolabs.earthweather.MainViewModel
import com.galgolabs.earthweather.ui.CompassUtil
import com.galgolabs.earthweather.ui.UtilDate
import com.galgolabs.earthweather.ui.localDB.MiniClimate
import com.galgolabs.earthweather.ui.localDB.MiniMain
import com.galgolabs.earthweather.ui.localDB.MiniWeather
import com.galgolabs.earthweather.ui.localDB.MiniWeatherData
import kotlinx.coroutines.launch

//@HiltViewModel
class HomeViewModel(private val repo: Repository) : ViewModel(), Observable {
    private var compassUtil = CompassUtil()
    private var utilDate = UtilDate()

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

    val allWeather: LiveData<List<MiniClimate>> = repo.allWeather.asLiveData()

    var fetchResult: MutableLiveData<NetworkResponse> = repo.result

    //
    fun fetchMyLocation(lat: Double, lng: Double) {
        viewModelScope.launch {
            repo.fetchData(lat, lng)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun devolve(weatherData: WeatherData) {
        locationName.value = weatherData.name
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

        print("devolve : $locationName")

        try {
//            val climate = MiniClimate(3163858, "Zocca",200,10000,1661870592)
            viewModelScope.launch{
                insertData(weatherData)
            }
        }catch (ex: java.lang.Exception){
            ex.printStackTrace()
        }
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