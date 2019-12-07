/*
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.data.repository.remote

import com.sanjay.authorslist.data.api.AuthorsListService
import com.sanjay.authorslist.data.repository.AuthorsDataSource
import com.sanjay.authorslist.data.repository.remote.model.Author
import com.sanjay.authorslist.data.repository.remote.model.Comment
import com.sanjay.authorslist.data.repository.remote.model.Post
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Class to handle remote operations
 *
 * @author Sanjay.Sah
 */
class AuthorsRemoteDataSource @Inject constructor(private var remoteService: AuthorsListService) :
    AuthorsDataSource {

    override fun getAuthors(page: Int, limit: Int): Flowable<List<Author>> =
        remoteService.fetchAuthorsList(page, limit).toFlowable().take(1)


    override fun getPosts(authorId: Int, page: Int, limit: Int): Flowable<List<Post>> =
        remoteService.fetchPosts(authorId, page, limit).toFlowable().take(1)


    override fun getComments(postId: Int, page: Int, limit: Int): Flowable<List<Comment>> =
        remoteService.fetchComments(postId, page, limit).toFlowable().take(1)

}