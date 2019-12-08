package com.sanjay.authorslist.ui.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sanjay.authorslist.data.repository.remote.model.Comment
import com.sanjay.authorslist.data.repository.remote.model.Post
import com.sanjay.authorslist.pagination.datasource.CommentsPagingDataSourceFactory
import com.sanjay.authorslist.ui.BaseViewModel
import javax.inject.Inject

class CommentsViewModel @Inject constructor(private val pagingDataSourceFactory: CommentsPagingDataSourceFactory) :
    BaseViewModel() {

    //LiveData object for comments
    var commentsList: LiveData<PagedList<Comment>>? = null
    //LiveData object for state
    var state = pagingDataSourceFactory.commentsPagingDataSource.state
    var selectedPost = pagingDataSourceFactory.commentsPagingDataSource.selectedPost

    init {
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()
        commentsList = Transformations.switchMap(selectedPost) {
            LivePagedListBuilder<Int, Comment>(
                pagingDataSourceFactory, config
            ).build()
        }
    }


    fun listIsEmpty(): Boolean {
        return commentsList?.value?.isEmpty() ?: true
    }

    //Retrying the API call
    fun retry() {
        pagingDataSourceFactory.commentsPagingDataSource.retry()
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val INITIAL_LOAD_SIZE_HINT = 20
    }

}