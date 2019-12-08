package com.sanjay.authorslist.data.repository.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sanjay.authorslist.data.api.AuthorsListService
import com.sanjay.authorslist.data.repository.remote.model.Author
import com.sanjay.authorslist.data.repository.remote.model.Comment
import com.sanjay.authorslist.data.repository.remote.model.Post
import io.reactivex.Flowable
import io.reactivex.Single
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class AuthorsRemoteDataSourceTest {

    @Mock
    lateinit var apiService: AuthorsListService

    private var remoteDataSource: AuthorsRemoteDataSource? = null

    private val currentPage = 1
    private val limit = 20

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        remoteDataSource = AuthorsRemoteDataSource(apiService)
    }

    @After
    fun tearDown() {
        remoteDataSource = null
    }

    @Test
    fun getAuthors() {
        val authorsList = emptyList<Author>()
        val observable = Single.just(authorsList)

        val argumentCaptor = ArgumentCaptor.forClass(Int::class.java)

        whenever(apiService.fetchAuthorsList(currentPage, limit)).thenReturn(observable)

        remoteDataSource?.getAuthors(currentPage, limit)

        verify(apiService).fetchAuthorsList(argumentCaptor.capture(), argumentCaptor.capture())

        assertThat(
            apiService.fetchAuthorsList(currentPage, limit),
            CoreMatchers.instanceOf(Single::class.java)
        )
    }

    @Test
    fun getPosts() {

        val postsList = emptyList<Post>()
        val observable = Single.just(postsList)
        val authorId = 1

        val argumentCaptor = ArgumentCaptor.forClass(Int::class.java)

        whenever(apiService.fetchPosts(authorId, currentPage, limit)).thenReturn(observable)

        remoteDataSource?.getPosts(authorId, currentPage, limit)

        verify(apiService).fetchPosts(
            argumentCaptor.capture(),
            argumentCaptor.capture(),
            argumentCaptor.capture()
        )

        assertThat(
            apiService.fetchPosts(authorId, currentPage, limit),
            CoreMatchers.instanceOf(Single::class.java)
        )
    }

    @Test
    fun getComments() {
        val commentsList = emptyList<Comment>()
        val observable = Single.just(commentsList)
        val postId = 1
        val argumentCaptor = ArgumentCaptor.forClass(Int::class.java)

        whenever(apiService.fetchComments(postId, currentPage, limit)).thenReturn(observable)

        remoteDataSource?.getComments(postId, currentPage, limit)

        verify(apiService).fetchComments(
            argumentCaptor.capture(),
            argumentCaptor.capture(),
            argumentCaptor.capture()
        )

        assertThat(
            apiService.fetchComments(postId, currentPage, limit),
            CoreMatchers.instanceOf(Single::class.java)
        )
    }
}