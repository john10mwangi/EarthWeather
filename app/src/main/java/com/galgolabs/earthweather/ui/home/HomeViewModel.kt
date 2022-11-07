package com.galgolabs.earthweather.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galgolabs.earthweather.ui.CompassUtil
import com.galgolabs.earthweather.ui.UtilDate
import kotlinx.coroutines.launch

//@HiltViewModel
class HomeViewModel  : ViewModel(), Observable {
//    private var repo: Repository = Repository()
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

//    var fetchResult: MutableLiveData<NetworkResponse> = repo.result

    //
    fun fetchMyLocation(lat: Double, lng: Double) {
        viewModelScope.launch {
//            repo.fetchData(lat, lng)
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
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}