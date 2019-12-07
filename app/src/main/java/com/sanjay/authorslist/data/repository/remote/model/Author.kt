/*
 * Author.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.data.repository.remote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Sanjay.Sah
 */

@Parcelize
data class Author(
    val id: Int,
    val name: String,
    val userName: String,
    val email: String,
    val avatarUrl: String
) : Parcelable
