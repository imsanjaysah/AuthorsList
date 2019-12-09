package com.sanjay.authorslist.pagination.factory

import androidx.paging.DataSource
import com.sanjay.authorslist.data.repository.remote.model.Post
import com.sanjay.authorslist.pagination.datasource.PostsPagingDataSource
import javax.inject.Inject

class PostsPagingDataSourceFactory @Inject constructor(val postsPagingDataSource: PostsPagingDataSource) :
    DataSource.Factory<Int, Post>() {

    override fun create(): DataSource<Int, Post> {
        return postsPagingDataSource
    }
}