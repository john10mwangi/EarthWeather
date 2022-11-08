package com.galgolabs.earthweather

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.galgolabs.earthweather.ui.home.Repository
import com.galgolabs.earthweather.ui.localDB.MiniClimate

class MainViewModel(private val repository: Repository): ViewModel() {


    suspend fun insertData(climate: MiniClimate){
        repository.insert(climate)
    }

    val allWeather: LiveData<List<MiniClimate>> = repository.allWeather.asLiveData()
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}