package com.sanjay.authorslist.ui.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sanjay.authorslist.data.repository.remote.model.Post
import com.sanjay.authorslist.pagination.factory.PostsPagingDataSourceFactory
import com.sanjay.authorslist.ui.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PostsViewModel @Inject constructor(private val pagingDataSourceFactory: PostsPagingDataSourceFactory) :
    BaseViewModel() {
    //LiveData object for posts
    var postsList: LiveData<PagedList<Post>>? = null
    //LiveData object for state
    var state = pagingDataSourceFactory.postsPagingDataSource.state
    var selectedAuthor = pagingDataSourceFactory.postsPagingDataSource.selectedAuthor

    init {
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()
        postsList = Transformations.switchMap(selectedAuthor) { _ ->
            LivePagedListBuilder<Int, Post>(
                pagingDataSourceFactory, config
            ).build()
        }
    }


    fun listIsEmpty(): Boolean {
        return postsList?.value?.isEmpty() ?: true
    }

    //Retrying the API call
    fun retry() {
        pagingDataSourceFactory.postsPagingDataSource.retry()
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val INITIAL_LOAD_SIZE_HINT = 20
    }

    override var disposable: CompositeDisposable
        get() = pagingDataSourceFactory.postsPagingDataSource.disposable
        set(_) {}
}