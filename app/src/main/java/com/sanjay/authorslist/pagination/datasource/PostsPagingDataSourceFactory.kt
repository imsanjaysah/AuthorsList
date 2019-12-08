package com.sanjay.authorslist.pagination.datasource

import androidx.paging.DataSource
import com.sanjay.authorslist.data.repository.remote.model.Post
import javax.inject.Inject

class PostsPagingDataSourceFactory @Inject constructor(val postsPagingDataSource: PostsPagingDataSource) :
    DataSource.Factory<Int, Post>() {

    override fun create(): DataSource<Int, Post> {
        return postsPagingDataSource
    }
}