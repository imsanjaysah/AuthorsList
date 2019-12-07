/*
 * ErrorUtils.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.data.api

import com.google.gson.Gson
import com.sanjay.authorslist.data.repository.remote.model.APIError
import retrofit2.Response

/**
 * Class used to parse the error response received in api calling
 * @author Sanjay.Sah
 */
object ErrorUtils {

    fun<T> parseError(response: Response<T>): APIError {

        return Gson().fromJson(response.errorBody()!!.charStream(), APIError::class.java)
    }
}