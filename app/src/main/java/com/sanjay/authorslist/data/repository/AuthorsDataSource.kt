/*
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.data.repository

import com.sanjay.authorslist.data.repository.remote.model.Author
import com.sanjay.authorslist.data.repository.remote.model.Comment
import com.sanjay.authorslist.data.repository.remote.model.Post
import io.reactivex.Flowable

/**
 * @author Sanjay Sah
 */
interface AuthorsDataSource {
    fun getAuthors(page: Int, limit: Int): Flowable<List<Author>>

    fun getPosts(authorId: Int, page: Int, limit: Int): Flowable<List<Post>>

    fun getComments(postId: Int, page: Int, limit: Int): Flowable<List<Comment>>
}