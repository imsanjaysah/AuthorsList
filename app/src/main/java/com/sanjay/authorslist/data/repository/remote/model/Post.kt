package com.sanjay.authorslist.data.repository.remote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Post(
    val id: Int,
    val date: String,
    val title: String,
    val body: String,
    val imageUrl: String?,
    val authorId: Int
): Parcelable