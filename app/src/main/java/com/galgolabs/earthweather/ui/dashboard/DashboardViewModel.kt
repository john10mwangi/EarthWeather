package com.galgolabs.earthweather.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    val cities = MutableLiveData<ArrayList<City>>()
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    fun addDummy(){
        var arr = ArrayList<City>()
        val c1 = City(id = "bsfjhdy", name = "Nakuru", country = "Kenya", lat = -0.29761576183126925, lng = 36.07473749288049)
        val c2 = City(id = "mzfbdsk", name = "Nairobi", country = "Kenya", lat = -1.264076056115005, lng = 36.81717226278329)
        val c3 = City(id = "jdossld", name = "Kitale", country = "Kenya", lat = 1.022404978002921, lng = 34.99792358715327)
        val c4 = City(id = "sdfhssd", name = "Thika", country = "Kenya", lat = -1.0391680119572233, lng = 37.07224426815673)
        val c5 = City(id = "sdmfbdj", name = "Mombasa", country = "Kenya", lat = -4.0478570900050945, lng = 39.65218390461765)
        val c6 = City(id = "smgddjk", name = "Kampala", country = "Uganda", lat = 0.3137157587345642, lng = 32.600495704472735)
        val c7 = City(id = "smdbdhu", name = "Jinja", country = "Uganda", lat = 0.4470122241508398, lng = 33.208659044437056)
        val c8 = City(id = "wohdgdd", name = "Dodoma", country = "Tanzania", lat = -6.166116329795121, lng = 35.73881493090536)
        val c9 = City(id = "khsyeud", name = "Voi", country = "Kenya", lat = -3.3971497477897694, lng = 38.55597651234854)
        val c11 = City(id = "hesijlj", name = "Busia", country = "Kenya", lat = 0.45857788301619384, lng = 34.111090320787966)
        val c12 = City(id = "sunysci", name = "Limuru", country = "Kenya", lat = -1.1084288260157624, lng = 36.64283211510969)
        val c13 = City(id = "yndkscv", name = "Bura", country = "Kenya", lat = -1.0943029169218002, lng = 39.94105843731054)
        val c14 = City(id = "iung9uy", name = "Garisa", country = "Kenya", lat = -0.4551553421687792, lng = 39.644207845851405)

        arr.add(c1)
        arr.add(c2)
        arr.add(c12)
        arr.add(c13)
        arr.add(c14)
        arr.add(c11)
        arr.add(c3)
        arr.add(c4)
        arr.add(c5)
        arr.add(c6)
        arr.add(c7)
        arr.add(c8)
        arr.add(c9)

        cities.value = arr
    }
}