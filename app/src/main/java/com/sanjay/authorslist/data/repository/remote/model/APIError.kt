/*
 * APIError.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.data.repository.remote.model

/**
 * Class used for mapping the api error response
 * @author Sanjay.Sah
 */
data class APIError(val errorCode: Int, val message: String?)