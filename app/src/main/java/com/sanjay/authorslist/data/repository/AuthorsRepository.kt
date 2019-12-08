/*
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.data.repository

import com.sanjay.authorslist.data.repository.remote.model.Author
import com.sanjay.authorslist.data.repository.remote.model.Comment
import com.sanjay.authorslist.data.repository.remote.model.Post
import com.sanjay.authorslist.injection.annotations.Local
import com.sanjay.authorslist.injection.annotations.Remote
import io.reactivex.Flowable
import javax.inject.Inject

/**
 *
 * @author Sanjay Sah.
 */
class AuthorsRepository @Inject constructor(@Local private val localDataSource: AuthorsDataSource, @Remote private val remoteDataSource: AuthorsDataSource) :
    AuthorsDataSource {
    override fun getAuthors(page: Int, limit: Int): Flowable<List<Author>> =
        remoteDataSource.getAuthors(page, limit)

    override fun getPosts(authorId: Int, page: Int, limit: Int): Flowable<List<Post>> =
        remoteDataSource.getPosts(authorId, page, limit)

    override fun getComments(postId: Int, page: Int, limit: Int): Flowable<List<Comment>> =
        remoteDataSource.getComments(postId, page, limit)

}