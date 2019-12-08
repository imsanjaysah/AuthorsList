package com.sanjay.authorslist.pagination.datasource

import androidx.paging.DataSource
import com.sanjay.authorslist.data.repository.remote.model.Comment
import javax.inject.Inject

class CommentsPagingDataSourceFactory @Inject constructor(val commentsPagingDataSource: CommentsPagingDataSource) :
    DataSource.Factory<Int, Comment>() {

    override fun create(): DataSource<Int, Comment> {
        return commentsPagingDataSource
    }
}