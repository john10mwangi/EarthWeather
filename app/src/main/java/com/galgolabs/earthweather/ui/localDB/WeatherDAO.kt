package com.galgolabs.earthweather.ui.localDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.galgolabs.earthweather.ui.home.WeatherData
import kotlinx.coroutines.flow.Flow
import java.util.*
import kotlin.collections.ArrayList

@Dao
interface WeatherDAO {
    @Query("SELECT * FROM climate_data")
    fun fetch(): Flow<List<MiniClimate>>

    @Query("SELECT * FROM climate_data WHERE climateId = :id")
    fun fetch(id: Long): MiniWeatherData

    @Transaction
    @Query("SELECT * FROM climate_data")
    fun fetchWeather(): Flow<List<MiniWeatherData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(weatherData: MiniClimate) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(main: MiniMain)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(weather: List<MiniWeather>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(coords: MiniCoords)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(wind: MiniWind): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(sys: MiniSys)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(cloud: MiniCloud)

    @Query("DELETE FROM climate_data")
    suspend fun deleteAll()

    @Transaction
    suspend fun createWeatherData(climate: WeatherData) : Long{
        val climateId = create(MiniClimate(climate.id, climate.name, climate.cod, climate.visibility, climate.dt))
        val weathers = ArrayList<MiniWeather>()
        for (miniWeather in climate.weather){
            weathers.add(MiniWeather(parentId = climateId, main = miniWeather.main, description = miniWeather.description))
        }
        create(weathers)

        val coord = MiniCoords(lat = climate.coord.lat, lon = climate.coord.lon, parentId = climateId)
        create(coord)

        val main = climate.main
        val mainMini = MiniMain(temp = main.temp, parentId = climateId, feels_like = main.feels_like, temp_max = main.temp_max,
            temp_min = main.temp_min, pressure = main.pressure, humidity = main.humidity, sea_level = main.sea_level,
            grnd_level = main.grnd_level, timezone = main.timezone, icon = main.icon)
        create(mainMini)

        val sys = climate.sys
        create(MiniSys(parentId = climateId, country = sys.country, sunrise = sys.sunrise, sunset = sys.sunset))

        create(MiniCloud(parentId = climateId, all = climate.clouds.all))

        val ret = create(MiniWind(parentId = climateId, speed = climate.wind.speed, deg = climate.wind.deg, gust = climate.wind.gust))

        return ret
    }


//    @Query("DELETE  FROM climate_data")
//    suspend fun deleteAll(id: Int)
}