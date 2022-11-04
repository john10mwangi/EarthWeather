package com.galgolabs.earthweather.ui

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.galgolabs.earthweather.R

/**
 * Implementation of App Widget functionality.
 */
class EarthWeatherAppWidget : AppWidgetProvider() {
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

internal fun updateAppWidget(
	context: Context,
	appWidgetManager: AppWidgetManager,
	appWidgetId: Int
) {
	val widgetCityText = context.getString(R.string.city_text_placeholder)
	val widgetDateText = context.getString(R.string.date_text)
	val widgetTempText = context.getString(R.string.temp_placeholder)
	val widgetForecastText = context.getString(R.string.forecast_placeholder)
	// Construct the RemoteViews object
	val views = RemoteViews(context.packageName, R.layout.earth_weather_app_widget)

	views.setTextViewText(R.id.appwidget_city_text2, widgetCityText)
	views.setTextViewText(R.id.appwidget_city_text, widgetDateText)
	views.setTextViewText(R.id.appwidget_city_temp, widgetTempText)
	views.setTextViewText(R.id.appwidget_city_forecast, widgetForecastText)

	// Instruct the widget manager to update the widget
	appWidgetManager.updateAppWidget(appWidgetId, views)
}