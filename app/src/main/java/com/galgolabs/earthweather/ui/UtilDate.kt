package com.galgolabs.earthweather.ui

import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class UtilDate {
    val DIVISOR = 3600000

    @RequiresApi(Build.VERSION_CODES.O)
    public fun milliToHoursAndMinutes(millisecs: Long): String {
        // define once somewhere in order to reuse it
        val formatter = DateTimeFormatter.ofPattern("HH:mm a");

// JVM representation of a millisecond epoch absolute instant
        val instant = Instant.ofEpochMilli(millisecs)


// Adding the timezone information to be able to format it (change accordingly)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        println(TimeZone.getTimeZone(ZoneId.systemDefault()))
        return formatter.format(date)
    }

    public fun longToDate(timestamp: Long) : String {
        val date = Date(timestamp * 1000)
        val format = SimpleDateFormat("HH:mm aa", Locale.getDefault())
        return format.format(date)
    }
}