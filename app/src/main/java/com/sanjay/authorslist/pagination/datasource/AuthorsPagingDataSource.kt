package com.sanjay.authorslist.pagination.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.sanjay.authorslist.constants.State
import com.sanjay.authorslist.data.repository.AuthorsRepository
import com.sanjay.authorslist.data.repository.remote.model.Author
import com.sanjay.authorslist.extensions.addToCompositeDisposable
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthorsPagingDataSource @Inject constructor(private val repository: AuthorsRepository) :
    PageKeyedDataSource<Int, Author>() {
    val disposable = CompositeDisposable()
    //LiveData object for state
    var state = MutableLiveData<State>()
    //Completable required for retrying the API call which gets failed due to any error like no internet
    private var retryCompletable: Completable? = null
    /**
     * Creating the observable for specific page to call the API
     */
    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    //Retrying the API call
    fun retry() {
        if (retryCompletable != null) {
            disposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Author>
    ) {
        updateState(State.LOADING)
        val currentPage = 1
        val nextPage = currentPage + 1
        //Call api
        repository.getAuthors(1, params.requestedLoadSize)
            .subscribe(
                { authors ->
                    updateState(State.DONE)
                    callback.onResult(authors, null, nextPage)

                },
                {
                    updateState(State.ERROR)
                    setRetry(Action { loadInitial(params, callback) })
                }
            ).addToCompositeDisposable(disposable)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Author>) {
        updateState(State.LOADING)
        val currentPage = params.key
        val nextPage = currentPage + 1
        //Call api
        repository.getAuthors(currentPage, params.requestedLoadSize)
            .subscribe(
                { authors ->
                    updateState(State.DONE)
                    callback.onResult(authors, nextPage)

                },
                {
                    updateState(State.ERROR)
                    setRetry(Action { loadAfter(params, callback) })
                }
            ).addToCompositeDisposable(disposable)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Author>) {
    }
}