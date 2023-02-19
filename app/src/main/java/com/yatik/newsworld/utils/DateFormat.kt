package com.yatik.newsworld.utils

import java.text.SimpleDateFormat
import java.util.*

class DateFormat {

    companion object {
        fun convertDate(dateString: String?): String {
            return dateString?.let {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val date = inputFormat.parse(dateString)
                val currentDate = Calendar.getInstance().time
                val diff = currentDate.time - date.time

                val seconds = diff / 1000
                val minutes = seconds / 60
                val hours = minutes / 60
                val days = hours / 24
                val months = days / 30

                when {
                    months > 0 -> "${months}months(s) ago"
                    days > 0 -> "${days}d ago"
                    hours > 0 -> "${hours}h ago"
                    minutes > 0 -> "${minutes}m ago"
                    else -> "${seconds}s ago"
                }
            } ?: ""
        }
    }

}