package com.delet_dis.elementarylauncher.domain.helpers

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun getCurrentTime(): String {
    val calendar = Calendar.getInstance()
    val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
    val minute: Int = calendar.get(Calendar.MINUTE)

    return if (minute < 10) {
        "$hour:0$minute"
    } else {
        "$hour:$minute"
    }
}

fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val dayOfWeek =
        calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())

    return "$dayOfWeek, $day $month"
}

fun formatToDigitalClock(mills: Long): String {
    val date = Date(mills)
    val formatter: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(date)
}
