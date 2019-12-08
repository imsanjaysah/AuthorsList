package com.sanjay.authorslist.ui.posts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import com.sanjay.authorslist.constants.State
import com.sanjay.authorslist.data.repository.AuthorsRepository
import com.sanjay.authorslist.data.repository.remote.model.Post
import com.sanjay.authorslist.extensions.addToCompositeDisposable
import com.sanjay.authorslist.ui.BaseViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostsViewModel @Inject constructor(private val repository: AuthorsRepository) :
    BaseViewModel() {

    //LiveData object for posts
    var postsList: LiveData<PagedList<Post>>? = null
    //LiveData object for state
    var state = MutableLiveData<State>()
    //Completable required for retrying the API call which gets failed due to any error like no internet
    private var retryCompletable: Completable? = null

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(5)
            .setEnablePlaceholders(false)
            .build()
        postsList = LivePagedListBuilder<Int, Post>(
            PostsDataSourceFactory(1), config
        ).build()
    }

    fun getPosts(authorId: Int) {
        //Setting up Paging for fetching the posts in pagination
        /*val config = PagedList.Config.Builder()
            .setPageSize(5)
            .setEnablePlaceholders(false)
            .build()
        postsList = LivePagedListBuilder<Int, Post>(
            PostsDataSourceFactory(authorId), config
        ).build()*/
    }

    fun listIsEmpty(): Boolean {
        return postsList?.value?.isEmpty() ?: true
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

    inner class PostsDataSourceFactory(val authorId: Int) :
        DataSource.Factory<Int, Post>() {

        override fun create(): DataSource<Int, Post> {
            return PostsDataSource(authorId)
        }
    }

    inner class PostsDataSource(val authorId: Int) :
        PositionalDataSource<Post>() {
        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Post>) {
            updateState(State.LOADING)
            Log.d("Posts", "${params.requestedLoadSize} - ${params.requestedStartPosition}")
            //Call api
            repository.getPosts(authorId, params.requestedStartPosition, params.requestedLoadSize)
                .subscribe(
                    { posts ->
                        updateState(State.DONE)
                        callback.onResult(posts, 0)

                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                ).addToCompositeDisposable(disposable)
        }

        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Post>) {
            updateState(State.LOADING)
            Log.d("Posts", "${params.loadSize} - ${params.startPosition}")
            //Call api
            repository.getPosts(authorId, params.startPosition, params.loadSize).subscribe(
                { posts ->
                    updateState(State.DONE)
                    callback.onResult(posts)

                },
                {
                    updateState(State.ERROR)
                    setRetry(Action { loadRange(params, callback) })
                }
            ).addToCompositeDisposable(disposable)

        }
    }
}