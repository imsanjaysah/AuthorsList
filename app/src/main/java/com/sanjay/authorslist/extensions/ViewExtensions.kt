/*
 * ViewExtensions.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.extensions

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


/**
 * Created by Sanjay Sah.
 */

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun EditText.isEmpty(): Boolean =
        TextUtils.isEmpty(this.text.toString().trim())

fun EditText.toTextString(): String =
        this.text.toString().trim()


fun View.hideSoftKey() {
    val view = this
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


