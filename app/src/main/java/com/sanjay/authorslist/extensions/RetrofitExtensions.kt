/*
 * RetrofitExtensions.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.extensions

import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import java.io.IOException

/**
 * Created by Sanjay Sah
 */

fun RequestBody?.bodyToString(): String {
    try {
        val copy = this
        val buffer = Buffer()
        if (copy != null)
            copy.writeTo(buffer)
        else
            return ""
        return buffer.readUtf8()
    } catch (e: IOException) {
        return "IO Exception"
    }

}

fun ResponseBody?.bodyToString(): String {
    try {
        this!!.byteStream().bufferedReader().use {
            return it.readText()
        }
    } catch (e: IOException) {
        return "IO Exception"
    }

}