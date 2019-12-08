/*
 * BaseViewModel.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.ui

import androidx.lifecycle.ViewModel
import com.sanjay.authorslist.constants.State
import com.sanjay.authorslist.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    abstract var disposable : CompositeDisposable

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}