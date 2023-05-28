package com.galgolabs.earthweather.ui.dashboard

import com.galgolabs.earthweather.ui.home.TownData

interface ItemSelectedCallback {
    fun onSelection(city: City){}
    fun onSelection(town: TownData){}
}