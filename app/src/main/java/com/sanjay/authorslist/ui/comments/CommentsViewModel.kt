package com.sanjay.authorslist.ui.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.sanjay.authorslist.constants.State
import com.sanjay.authorslist.data.repository.AuthorsRepository
import com.sanjay.authorslist.data.repository.remote.model.Comment
import com.sanjay.authorslist.data.repository.remote.model.Post
import com.sanjay.authorslist.extensions.addToCompositeDisposable
import com.sanjay.authorslist.ui.BaseViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CommentsViewModel @Inject constructor(private val repository: AuthorsRepository) :
    BaseViewModel() {

    //LiveData object for comments
    var commentsList: LiveData<PagedList<Comment>>? = null
    //LiveData object for state
    var state = MutableLiveData<State>()
    var selectedPost = MutableLiveData<Post>()
    //Completable required for retrying the API call which gets failed due to any error like no internet
    private var retryCompletable: Completable? = null

    init {
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(20)
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()
        commentsList = Transformations.switchMap(selectedPost) { post ->
            LivePagedListBuilder<Int, Comment>(
                CommentsDataSourceFactory(post.id), config
            ).build()
        }
    }


    fun listIsEmpty(): Boolean {
        return commentsList?.value?.isEmpty() ?: true
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

    inner class CommentsDataSourceFactory(private val postId: Int) :
        DataSource.Factory<Int, Comment>() {

        override fun create(): DataSource<Int, Comment> {
            return CommentsDataSource(postId)
        }
    }

    inner class CommentsDataSource(private val postId: Int) :
        PageKeyedDataSource<Int, Comment>() {
        override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Int, Comment>
        ) {
            updateState(State.LOADING)
            val currentPage = 1
            val nextPage = currentPage + 1
            //Call api
            repository.getComments(postId, 1, params.requestedLoadSize)
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
            repository.getComments(postId, currentPage, params.requestedLoadSize)
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
}