/*
 * DaggerExtensions.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.extensions
import android.content.Context
import com.sanjay.authorslist.AuthorsApplication

/**
 * Created by Sanjay Sah
 */

val Context.appComponent
    get() = (applicationContext as AuthorsApplication).appComponent
