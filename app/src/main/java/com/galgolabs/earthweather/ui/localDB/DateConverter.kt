package com.galgolabs.earthweather.ui.localDB

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
	@TypeConverter
	fun toLong(date: Date): Long{
		return date.time
	}

	@TypeConverter
	fun toDate(ln: Long): Date{
		return Date(ln)
	}
}