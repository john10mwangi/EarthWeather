package com.galgolabs.earthweather.ui.localDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(version = 1, exportSchema = false,
    entities = [MiniClimate::class, MiniMain::class, MiniSys::class,
        MiniCloud::class, MiniWind::class, MiniCoords::class, MiniWeather::class]
)
abstract class WeatherAppRoomDB : RoomDatabase() {
    abstract fun weatherDao() : WeatherDAO

    companion object {
        private var INSTANCE: WeatherAppRoomDB? = null

        fun getDatabase(context: Context, applicationScope: CoroutineScope) : WeatherAppRoomDB{

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherAppRoomDB::class.java,
                    "weather_dababase",
                ).addCallback(WeatherDataBaseCallBack(applicationScope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }


        private class WeatherDataBaseCallBack(private val applicationScope: CoroutineScope) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    applicationScope.launch {
                        populateDB(database.weatherDao())
                    }
                }
            }

            private suspend fun populateDB(weatherDao: WeatherDAO) {
                weatherDao.deleteAll()
            }
        }
    }
}
