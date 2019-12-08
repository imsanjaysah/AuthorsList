/*
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.data.api

import com.sanjay.authorslist.data.repository.remote.model.Author
import com.sanjay.authorslist.data.repository.remote.model.Comment
import com.sanjay.authorslist.data.repository.remote.model.Post
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**Interface where all the api used in app are defined.
 * @author Sanjay.Sah
 */
interface AuthorsListService {

    /**
     * Api for fetching authors list
     */
    @GET("/authors")
    fun fetchAuthorsList(@Query("_page") page: Int, @Query("_limit") limit: Int): Single<List<Author>>


    /**
     * Api for fetching author's posts
     */
    @GET("/posts")
    fun fetchPosts(@Query("authorId") authorId: Int, @Query("_page") page: Int, @Query("_limit") limit: Int): Single<List<Post>>

    /**
     * Api for fetching post's comments
     */
    @GET("/comments")
    fun fetchComments(@Query("postId") postId: Int, @Query("_page") page: Int, @Query("_limit") limit: Int): Single<List<Comment>>


}