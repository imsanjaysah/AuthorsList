/*
 * Utility.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Utility class to perform some utility operations.
 *
 * @author  sanjay.sah
 */

object Utility {
    fun convertUTCtoLocalTime(utcTime: String): String? {
        val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
        var value: Date? = null
        try {
            value = utcFormatter.parse(utcTime)
            val newFormatter = SimpleDateFormat("MMM dd, yyyy - HH:mm")
            newFormatter.timeZone = TimeZone.getDefault()
            return newFormatter.format(value)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }
}

