package com.galgolabs.earthweather.ui.localDB

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

@Database(version = 1, exportSchema = false,
    entities = [MiniClimate::class, MiniMain::class, MiniSys::class,
        MiniCloud::class, MiniWind::class, MiniCoords::class,
        MiniWeather::class]
)
@TypeConverters(DateConverter::class)
abstract class WeatherAppRoomDB : RoomDatabase() {
    abstract fun weatherDao() : WeatherDAO

    companion object {
        private var INSTANCE: WeatherAppRoomDB? = null

        fun getDatabase(context: Context, applicationScope: CoroutineScope) : WeatherAppRoomDB{

            return INSTANCE ?: synchronized(WeatherAppRoomDB::class.java) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherAppRoomDB::class.java,
                    "weather_dababase",
                ).addCallback(WeatherDataBaseCallBack(applicationScope))
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }


        private class WeatherDataBaseCallBack(private val applicationScope: CoroutineScope) : RoomDatabase.Callback() {
            private val scope = CoroutineScope(newSingleThreadContext("name"))
            private lateinit var dao: WeatherDAO
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                applicationScope.launch {
                    INSTANCE?.weatherDao()
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                applicationScope.launch {
                    dao = INSTANCE!!.weatherDao()
                }
            }
        }
    }
}
