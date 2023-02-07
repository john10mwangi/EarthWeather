package com.galgolabs.earthweather.ui

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.galgolabs.earthweather.MainActivity
import com.galgolabs.earthweather.R
import com.galgolabs.earthweather.ui.home.WeatherData
import com.galgolabs.earthweather.ui.localDB.MiniWeatherData
import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Implementation of App Widget functionality.
 */
class EarthWeatherAppWidget : AppWidgetProvider() {
	@RequiresApi(Build.VERSION_CODES.O)
	override fun onUpdate(
		context: Context,
		appWidgetManager: AppWidgetManager,
		appWidgetIds: IntArray
	) {
		// There may be multiple widgets active, so update all of them
		for (appWidgetId in appWidgetIds) {
			updateAppWidget(context, appWidgetManager, appWidgetId)
		}
	}

	override fun onEnabled(context: Context) {
		// Enter relevant functionality for when the first widget is created
	}

	override fun onDisabled(context: Context) {
		// Enter relevant functionality for when the last widget is disabled
	}
}

@RequiresApi(Build.VERSION_CODES.O)
internal fun updateAppWidget(
	context: Context,
	appWidgetManager: AppWidgetManager,
	appWidgetId: Int ) {
	val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM")
	val current = LocalDate.now().format(formatter)
	val prefs: SharedPreferences = context.getSharedPreferences("EarthWeather", Context.MODE_PRIVATE)
	val gson = Gson()
	val data = gson.fromJson(prefs.getString("pref_weather", null), MiniWeatherData::class.java)
	val widgetCityText = data.climate.name
	val widgetTempText = data.main.temp
	val widgetForecastText = data.weather[0].main

//	val widgetCityText = context.getString(R.string.city_text_placeholder)
//	val widgetDateText = context.getString(R.string.date_text)
//	val widgetTempText = context.getString(R.string.temp_placeholder)
//	val widgetForecastText = context.getString(R.string.forecast_placeholder)
	// Construct the RemoteViews object
	val views = RemoteViews(context.packageName, R.layout.earth_weather_app_widget)
	views.setOnClickPendingIntent(R.id.parent_layout, PendingIntent.getActivity
		(context, 100, Intent(context, MainActivity::class.java
		), PendingIntent.FLAG_IMMUTABLE))

	views.setTextViewText(R.id.appwidget_city_text2, widgetCityText)
	views.setTextViewText(R.id.appwidget_city_text, current)
	views.setTextViewText(R.id.appwidget_city_temp, widgetTempText.toString())
	views.setTextViewText(R.id.appwidget_city_forecast, widgetForecastText.toString())

	// Instruct the widget manager to update the widget
	appWidgetManager.updateAppWidget(appWidgetId, views)
}