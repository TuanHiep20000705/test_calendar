package com.example.testcalendar.custom_calendar

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

internal enum class DateTiming {
    NONE,
    START,
    END
}

internal fun resetTime(date: Calendar, dateTiming: DateTiming) {
    when (dateTiming) {
        DateTiming.START -> {
            date[Calendar.HOUR_OF_DAY] = 0
            date[Calendar.MINUTE] = 0
            date[Calendar.SECOND] = 0
            date[Calendar.MILLISECOND] = 0
        }
        DateTiming.END -> {
            date[Calendar.HOUR_OF_DAY] = 23
            date[Calendar.MINUTE] = 59
            date[Calendar.SECOND] = 59
            date[Calendar.MILLISECOND] = 999
        }
        else -> {
            date[Calendar.HOUR_OF_DAY] = 0
            date[Calendar.MINUTE] = 0
            date[Calendar.SECOND] = 0
            date[Calendar.MILLISECOND] = 0
        }
    }
}

fun isMonthSame(one: Calendar, second: Calendar): Boolean {
//    Log.e(">>>>", "one year: ${one[Calendar.YEAR]} - second year: ${second[Calendar.YEAR]}, one month: ${one[Calendar.MONTH] + 1} - second month: ${second[Calendar.MONTH]}")
    return one[Calendar.YEAR] == second[Calendar.YEAR]
            && (one[Calendar.MONTH] + 1) == second[Calendar.MONTH]
}

fun isDateSame(one: Calendar, second: Calendar): Boolean {
//    Log.e(">>>", "one date: ${one[Calendar.DATE]} - second date: ${second[Calendar.DATE]}")
    return isMonthSame(one, second)
            && one[Calendar.DATE] == second[Calendar.DATE]
}

fun printDate(calendar: Calendar?): String {
    return if (calendar != null) {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return simpleDateFormat.format(calendar.time)
    } else {
        "null"
    }
}