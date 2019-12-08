package com.sanjay.authorslist.ui.authors

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sanjay.authorslist.data.repository.remote.model.Author
import com.sanjay.authorslist.pagination.datasource.AuthorsPagingDataSourceFactory
import com.sanjay.authorslist.ui.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AuthorsViewModel @Inject constructor(private val pagingDataSourceFactory: AuthorsPagingDataSourceFactory) :
    BaseViewModel() {
    //LiveData object for authors
    var authorsList: LiveData<PagedList<Author>>? = null
    //LiveData object for state
    var state = pagingDataSourceFactory.authorsPagingDataSource.state

    init {
        //Setting up Paging for fetching the authors in pagination
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()
        authorsList = LivePagedListBuilder<Int, Author>(
            pagingDataSourceFactory, config
        ).build()
    }

    fun listIsEmpty(): Boolean {
        return authorsList?.value?.isEmpty() ?: true
    }

    //Retrying the API call
    fun retry() {
        pagingDataSourceFactory.authorsPagingDataSource.retry()
    }

    override var disposable: CompositeDisposable
        get() = pagingDataSourceFactory.authorsPagingDataSource.disposable
        set(_) {}

    companion object {
        private const val PAGE_SIZE = 20
        private const val INITIAL_LOAD_SIZE_HINT = 20
    }
}