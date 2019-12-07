/*
 * BaseActivity.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.sanjay.authorslist.AuthorsApplication
import com.sanjay.authorslist.R
import com.sanjay.authorslist.eventbus.RxBus
import com.sanjay.authorslist.injection.ActivityComponent
import com.sanjay.authorslist.injection.module.ActivityModule

import io.reactivex.disposables.CompositeDisposable


/**
 * Activity which holds all the common operations across all the Activities.
 *
 * @author Sanjay Sah
 */
abstract class BaseActivity : AppCompatActivity() {

    lateinit var activityComponent: ActivityComponent
    var disposeBag = CompositeDisposable()
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = (application as AuthorsApplication).appComponent
            .activityModule(ActivityModule(this))

        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.unregister(this)
        disposeBag.clear()

    }

    /*fun showNoConnectivityError() {
         showSnack(getCoordinatorLayout(), R.string.no_connectivity, Snackbar.LENGTH_SHORT)
    }*/


    fun showSnack(
        parent: ViewGroup, message: String, length: Int,
        actionLabel: String? = null, action: ((View) -> Unit)? = null,
        callback: ((Snackbar) -> Unit)? = null
    ) {
        val snack = Snackbar.make(parent, message, length)
            .apply {
                if (actionLabel != null) {
                    setAction(actionLabel, action)
                }
            }
        customizeSnackbar(this, snack)
        snack.show()

    }

    private fun customizeSnackbar(context: Context, snackbar: Snackbar) {
        snackbar.setActionTextColor(Color.WHITE)
        val sbView = snackbar.view
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))

    }
}