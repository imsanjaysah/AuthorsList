package com.sanjay.authorslist.pagination.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.sanjay.authorslist.constants.State
import com.sanjay.authorslist.data.repository.AuthorsRepository
import com.sanjay.authorslist.data.repository.remote.model.Comment
import com.sanjay.authorslist.data.repository.remote.model.Post
import com.sanjay.authorslist.extensions.addToCompositeDisposable
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CommentsPagingDataSource @Inject constructor(private val repository: AuthorsRepository) :
    PageKeyedDataSource<Int, Comment>() {

    val disposable = CompositeDisposable()
    //LiveData object for state
    var state = MutableLiveData<State>()
    var selectedPost = MutableLiveData<Post>()
    //Completable required for retrying the API call which gets failed due to any error like no internet
    private var retryCompletable: Completable? = null

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

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Comment>
    ) {
        updateState(State.LOADING)
        val currentPage = 1
        val nextPage = currentPage + 1
        //Call api
        repository.getComments(selectedPost.value!!.id, 1, params.requestedLoadSize)
            .subscribe(
                { comments ->
                    updateState(State.DONE)
                    callback.onResult(comments, null, nextPage)

                },
                {
                    updateState(State.ERROR)
                    setRetry(Action { loadInitial(params, callback) })
                }
            ).addToCompositeDisposable(disposable)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Comment>) {
        updateState(State.LOADING)
        val currentPage = params.key
        val nextPage = currentPage + 1
        //Call api
        repository.getComments(selectedPost.value!!.id, currentPage, params.requestedLoadSize)
            .subscribe(
                { comments ->
                    updateState(State.DONE)
                    callback.onResult(comments, nextPage)

                },
                {
                    updateState(State.ERROR)
                    setRetry(Action { loadAfter(params, callback) })
                }
            ).addToCompositeDisposable(disposable)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Comment>) {
    }
}