package com.sanjay.authorslist.pagination.factory

import androidx.paging.DataSource
import com.sanjay.authorslist.data.repository.remote.model.Author
import com.sanjay.authorslist.pagination.datasource.AuthorsPagingDataSource
import javax.inject.Inject

class AuthorsPagingDataSourceFactory @Inject constructor(val authorsPagingDataSource: AuthorsPagingDataSource) :
    DataSource.Factory<Int, Author>() {

    override fun create(): DataSource<Int, Author> {
        return authorsPagingDataSource
    }
}