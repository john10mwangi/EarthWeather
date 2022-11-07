package com.galgolabs.earthweather.ui.localDB

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.galgolabs.earthweather.ui.home.*

//@Entity(tableName = "climate_data")
//data class MiniClimate ( @PrimaryKey(autoGenerate = true)
//                         @ColumnInfo(name = "") val name:String,
//                         @ColumnInfo(name = "")val id: Int,
//                         @ColumnInfo(name = "")val cod: Int,
//                         @ColumnInfo(name = "")val weather: ArrayList<Weather>,
//                         @ColumnInfo(name = "")val coord: Coords,
//                         @ColumnInfo(name = "")val main: Main,
//                         @ColumnInfo(name = "")val wind: Wind,
//                         @ColumnInfo(name = "")val sys: Sys,
//                         @ColumnInfo(name = "")val visibility: Int,
//                         @ColumnInfo(name = "") val dt: Long,
//                         @ColumnInfo(name = "")val clouds: Cloud)

@Entity(tableName = "climate_data")
data class MiniClimate ( @PrimaryKey(autoGenerate = true) val climateId: Int,
                         @ColumnInfo(name = "name")val name:String,
                         @ColumnInfo(name = "id")val id: Int,
                         @ColumnInfo(name = "cod")val cod: Int,
                         @ColumnInfo(name = "visibility")val visibility: Int,
                         @ColumnInfo(name = "dt")val dt: Long)

@Entity(tableName = "weather_data")
data class MiniWeather ( @PrimaryKey(autoGenerate = true) val weatherId: Int,
                         @ColumnInfo(name = "id")val id: Int,
                         @ColumnInfo(name = "main")val main: String,
                         @ColumnInfo(name = "description")val description: String)

@Entity(tableName = "coords_data")
data class MiniCoords ( @PrimaryKey(autoGenerate = true) val coordId: Int,
                        @ColumnInfo(name = "lat")val lat: Double,
                        @ColumnInfo(name = "lon")val lon: Double)

@Entity(tableName = "main_data")
data class MiniMain ( @PrimaryKey(autoGenerate = true) val mainId: Int,
                      @ColumnInfo(name = "temp")val temp: Float,
                      @ColumnInfo(name = "feels_like")val feels_like: Float,
                      @ColumnInfo(name = "temp_min")val temp_min: Float,
                      @ColumnInfo(name = "temp_max")val temp_max: Float,
                      @ColumnInfo(name = "pressure") val pressure: Int,
                      @ColumnInfo(name = "humidity")val humidity: Int,
                      @ColumnInfo(name = "sea_level")val sea_level: Int,
                      @ColumnInfo(name = "grnd_level") val grnd_level: Int,
                      @ColumnInfo(name = "timezone")val timezone: Int,
                      @ColumnInfo(name = "icon")val icon: String )

@Entity(tableName = "wind_data")
data class MiniWind ( @PrimaryKey(autoGenerate = true) val windId: Int,
                      @ColumnInfo(name = "speed")val speed: Float,
                      @ColumnInfo(name = "deg")val deg: Int,
                      @ColumnInfo(name = "gust")val gust: Float)

@Entity(tableName = "sys_data")
data class MiniSys ( @PrimaryKey(autoGenerate = true) val sysId: Int,
                     @ColumnInfo(name = "country")val country: String,
                     @ColumnInfo(name = "sunrise")val sunrise: Long,
                     @ColumnInfo(name = "sunset")val sunset: Long)

@Entity(tableName = "cloud_data")
data class MiniCloud ( @PrimaryKey(autoGenerate = true) val cloudId: Int,
                       @ColumnInfo(name = "all")val all: Int)



data class MiniWeatherData (
    @Embedded val climate: MiniClimate,
    @Relation(
        parentColumn = "climateId",
        entityColumn = "main"
    )
    val weather: ArrayList<MiniWeather>,
    @Relation(
        parentColumn = "climateId",
        entityColumn = "lat"
    )
    val coord: MiniCoords,
    @Relation(
        parentColumn = "climateId",
        entityColumn = "temp"
    )
    val main: MiniMain,
    @Relation(
        parentColumn = "climateId",
        entityColumn = "speed"
    )
    val wind: MiniWind,
    @Relation(
        parentColumn = "climateId",
        entityColumn = "country"
    )
    val sys: MiniSys,
    @Relation(
        parentColumn = "climateId",
        entityColumn = "all"
    )
    val clouds: MiniCloud)