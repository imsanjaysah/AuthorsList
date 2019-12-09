package com.sanjay.authorslist.pagination.factory

import androidx.paging.DataSource
import com.sanjay.authorslist.data.repository.remote.model.Comment
import com.sanjay.authorslist.pagination.datasource.CommentsPagingDataSource
import javax.inject.Inject

class CommentsPagingDataSourceFactory @Inject constructor(val commentsPagingDataSource: CommentsPagingDataSource) :
    DataSource.Factory<Int, Comment>() {

    override fun create(): DataSource<Int, Comment> {
        return commentsPagingDataSource
    }
}