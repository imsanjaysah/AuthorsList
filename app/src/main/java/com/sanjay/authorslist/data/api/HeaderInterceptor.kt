/*
 * HeaderInterceptor.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.data.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 *
 * @author Sanjay
 */
class HeaderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-type", "application/json")
                .build()
        return chain.proceed(request)
    }
}
