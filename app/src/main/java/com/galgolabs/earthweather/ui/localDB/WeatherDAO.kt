package com.galgolabs.earthweather.ui.localDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galgolabs.earthweather.ui.home.WeatherData
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDAO {
    @Query("SELECT * FROM climate_data")
    fun fetch(): Flow<List<MiniClimate>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun create(weatherData: MiniClimate)

    @Query("DELETE FROM climate_data")
    suspend fun deleteAll()


//    @Query("DELETE  FROM climate_data")
//    suspend fun deleteAll(id: Int)
}