package com.galgolabs.earthweather.ui.localDB

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import androidx.room.Relation

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
data class MiniClimate ( @PrimaryKey(autoGenerate = false) val climateId: Int,
                         @ColumnInfo(name = "name")val name:String,
                         @ColumnInfo(name = "cod")val cod: Int,
                         @ColumnInfo(name = "visibility")val visibility: Int,
                         @ColumnInfo(name = "dt")val dt: Long)

@Entity(tableName = "weather_data", foreignKeys = [ForeignKey(entity = MiniClimate::class, onDelete = CASCADE,
    parentColumns = ["climateId"], childColumns = ["parentId"])])
data class MiniWeather(@PrimaryKey(autoGenerate = true) val weatherId: Int = 0,
                       @ColumnInfo(name = "parentId") val parentId: Long,
                       @ColumnInfo(name = "main") val main: String,
                       @ColumnInfo(name = "description") val description: String)

@Entity(tableName = "coords_data", foreignKeys = [ForeignKey(entity = MiniClimate::class, onDelete = CASCADE,
    parentColumns = ["climateId"], childColumns = ["parentId"])])
data class MiniCoords(@PrimaryKey(autoGenerate = true) val coordId: Int = 0,
                      @ColumnInfo(name = "parentId") val parentId: Long,
                      @ColumnInfo(name = "lat") val lat: Double,
                      @ColumnInfo(name = "lon") val lon: Double)

@Entity(tableName = "main_data", foreignKeys = [ForeignKey(entity = MiniClimate::class, onDelete = CASCADE,
    parentColumns = ["climateId"], childColumns = ["parentId"])])
data class MiniMain(@PrimaryKey(autoGenerate = true) val mainId: Int = 0,
                    @ColumnInfo(name = "temp") val temp: Float,
                    @ColumnInfo(name = "parentId") val parentId: Long = 0,
                    @ColumnInfo(name = "feels_like") val feels_like: Float,
                    @ColumnInfo(name = "temp_min") val temp_min: Float,
                    @ColumnInfo(name = "temp_max") val temp_max: Float,
                    @ColumnInfo(name = "pressure") val pressure: Int,
                    @ColumnInfo(name = "humidity") val humidity: Int,
                    @ColumnInfo(name = "sea_level") val sea_level: Int,
                    @ColumnInfo(name = "grnd_level") val grnd_level: Int,
                    @ColumnInfo(name = "timezone") val timezone: Int,
                    @ColumnInfo(name = "icon") val icon: String? )

@Entity(tableName = "wind_data", foreignKeys = [ForeignKey(entity = MiniClimate::class, onDelete = CASCADE,
    parentColumns = ["climateId"], childColumns = ["parentId"])])
data class MiniWind(@PrimaryKey(autoGenerate = true) val windId: Int = 0,
                    @ColumnInfo(name = "speed") val speed: Float,
                    @ColumnInfo(name = "parentId") val parentId: Long,
                    @ColumnInfo(name = "deg") val deg: Int,
                    @ColumnInfo(name = "gust") val gust: Float)

@Entity(tableName = "sys_data", foreignKeys = [ForeignKey(entity = MiniClimate::class, onDelete = CASCADE,
    parentColumns = ["climateId"], childColumns = ["parentId"])])
data class MiniSys(@PrimaryKey(autoGenerate = true) val sysId: Int = 0,
                   @ColumnInfo(name = "parentId") val parentId: Long,
                   @ColumnInfo(name = "country") val country: String,
                   @ColumnInfo(name = "sunrise") val sunrise: Long,
                   @ColumnInfo(name = "sunset") val sunset: Long)

@Entity(tableName = "cloud_data", foreignKeys = [ForeignKey(entity = MiniClimate::class, onDelete = CASCADE,
    parentColumns = ["climateId"], childColumns = ["parentId"])])
data class MiniCloud(@PrimaryKey(autoGenerate = true) val cloudId: Int = 0,
                     @ColumnInfo(name = "parentId") val parentId: Long,
                     @ColumnInfo(name = "all") val all: Int)


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