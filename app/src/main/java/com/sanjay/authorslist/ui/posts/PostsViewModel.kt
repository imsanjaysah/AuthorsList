package com.sanjay.authorslist.ui.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.sanjay.authorslist.constants.State
import com.sanjay.authorslist.data.repository.AuthorsRepository
import com.sanjay.authorslist.data.repository.remote.model.Author
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
    var selectedAuthor = MutableLiveData<Author>()
    //Completable required for retrying the API call which gets failed due to any error like no internet
    private var retryCompletable: Completable? = null

    init {
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(20)
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()
        postsList = Transformations.switchMap(selectedAuthor) { author ->
            LivePagedListBuilder<Int, Post>(
                PostsDataSourceFactory(author.id), config
            ).build()
        }
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

    inner class PostsDataSource(private val authorId: Int) :
        PageKeyedDataSource<Int, Post>() {
        override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Int, Post>
        ) {
            updateState(State.LOADING)
            val currentPage = 1
            val nextPage = currentPage + 1
            //Call api
            repository.getPosts(authorId,1, params.requestedLoadSize)
                .subscribe(
                    { posts ->
                        updateState(State.DONE)
                        callback.onResult(posts, null, nextPage)

                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                ).addToCompositeDisposable(disposable)
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Post>) {
            updateState(State.LOADING)
            val currentPage = params.key
            val nextPage = currentPage + 1
            //Call api
            repository.getPosts(authorId, currentPage, params.requestedLoadSize)
                .subscribe(
                    { posts ->
                        updateState(State.DONE)
                        callback.onResult(posts, nextPage)

                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadAfter(params, callback) })
                    }
                ).addToCompositeDisposable(disposable)
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Post>) {
        }
    }
}