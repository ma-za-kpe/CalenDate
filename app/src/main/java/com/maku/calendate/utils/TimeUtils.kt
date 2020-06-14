package com.maku.calendate.utils

import java.text.Format
import java.text.SimpleDateFormat
import java.util.*


/**
 * Returns [Boolean] based on current time.
 * Returns true if hours are between 06:00 pm - 07:00 am
 */
fun isNight(): Boolean {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return (currentHour <= 7 || currentHour >= 18)
}

/**
 * Returns [String] based on current time in "h:mm a". format
 */
fun getTime(hr: Int, min: Int): String? {
    val cal = Calendar.getInstance()
    cal[Calendar.HOUR_OF_DAY] = hr
    cal[Calendar.MINUTE] = min
    val formatter: Format
    formatter = SimpleDateFormat("h:mm a")
    return formatter.format(cal.time)
}

