/*
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.data.repository.local

import com.sanjay.authorslist.data.repository.AuthorsDataSource
import com.sanjay.authorslist.data.repository.remote.model.Author
import com.sanjay.authorslist.data.repository.remote.model.Comment
import com.sanjay.authorslist.data.repository.remote.model.Post
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Class to handle local db operations
 *
 * @author Sanjay Sah
 */
class AuthorsLocalDataSource @Inject constructor() : AuthorsDataSource {
    override fun getAuthors(page: Int, limit: Int): Flowable<List<Author>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPosts(authorId: Int, page: Int, limit: Int): Flowable<List<Post>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getComments(postId: Int, page: Int, limit: Int): Flowable<List<Comment>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}