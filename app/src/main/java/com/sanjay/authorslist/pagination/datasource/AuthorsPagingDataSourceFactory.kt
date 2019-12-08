package com.sanjay.authorslist.pagination.datasource

import androidx.paging.DataSource
import com.sanjay.authorslist.data.repository.remote.model.Author
import javax.inject.Inject

class AuthorsPagingDataSourceFactory @Inject constructor(val authorsPagingDataSource: AuthorsPagingDataSource) :
    DataSource.Factory<Int, Author>() {

    override fun create(): DataSource<Int, Author> {
        return authorsPagingDataSource
    }
}