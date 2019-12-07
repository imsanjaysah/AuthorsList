package com.sanjay.authorslist.ui.authors

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import com.sanjay.authorslist.constants.State
import com.sanjay.authorslist.data.repository.AuthorsRepository
import com.sanjay.authorslist.data.repository.remote.model.Author
import com.sanjay.authorslist.extensions.addToCompositeDisposable
import com.sanjay.authorslist.ui.BaseViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthorsViewModel @Inject constructor(private val repository: AuthorsRepository) :
    BaseViewModel() {

    //LiveData object for authors
    var authorsList: LiveData<PagedList<Author>>? = null
    //LiveData object for state
    var state = MutableLiveData<State>()
    //Completable required for retrying the API call which gets failed due to any error like no internet
    private var retryCompletable: Completable? = null

    init {
        //Setting up Paging for fetching the authors in pagination
        val config = PagedList.Config.Builder()
            .setPageSize(5)
            .setEnablePlaceholders(false)
            .build()
        authorsList = LivePagedListBuilder<Int, Author>(
            AuthorsDataSourceFactory(), config
        ).build()
    }

    fun listIsEmpty(): Boolean {
        return authorsList?.value?.isEmpty() ?: true
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
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

    /**
     * Creating the observable for specific page to call the API
     */
    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    inner class AuthorsDataSourceFactory() :
        DataSource.Factory<Int, Author>() {

        override fun create(): DataSource<Int, Author> {
            return AuthorsDataSource()
        }
    }

    inner class AuthorsDataSource() :
        PositionalDataSource<Author>() {
        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Author>) {
            updateState(State.LOADING)
            Log.d("Authors", "${params.requestedLoadSize} - ${params.requestedStartPosition}")
            //Call api
            repository.getAuthors(params.requestedStartPosition, params.requestedLoadSize)
                .subscribe(
                    { authors ->
                        updateState(State.DONE)
                        callback.onResult(authors, 0)

                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                ).addToCompositeDisposable(disposable)
        }

        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Author>) {
            updateState(State.LOADING)
            Log.d("Authors", "${params.loadSize} - ${params.startPosition}")
            //Call api
            repository.getAuthors(params.startPosition, params.loadSize).subscribe(
                { authors ->
                    updateState(State.DONE)
                    callback.onResult(authors)

                },
                {
                    updateState(State.ERROR)
                    setRetry(Action { loadRange(params, callback) })
                }
            ).addToCompositeDisposable(disposable)

        }
    }
}