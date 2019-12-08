package com.sanjay.authorslist.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sanjay.authorslist.data.repository.local.AuthorsLocalDataSource
import com.sanjay.authorslist.data.repository.remote.AuthorsRemoteDataSource
import com.sanjay.authorslist.data.repository.remote.model.Author
import com.sanjay.authorslist.data.repository.remote.model.Comment
import com.sanjay.authorslist.data.repository.remote.model.Post
import io.reactivex.Flowable
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.expect

class AuthorsRepositoryTest {

    @Mock
    lateinit var localRepository: AuthorsLocalDataSource
    @Mock
    lateinit var remoteRepository: AuthorsRemoteDataSource

    private var repository: AuthorsRepository? = null

    private val currentPage = 1
    private val limit = 20

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = AuthorsRepository(localRepository, remoteRepository)
    }

    @After
    fun tearDown() {
        repository = null
    }

    @Test
    fun getAuthors() {
        val authorsList = emptyList<Author>()
        val observable = Flowable.just(authorsList)
        val argumentCaptor = ArgumentCaptor.forClass(Int::class.java)

        whenever(remoteRepository.getAuthors(currentPage, limit)).thenReturn(observable)
        repository?.getAuthors(currentPage, limit)

        verify(remoteRepository).getAuthors(argumentCaptor.capture(), argumentCaptor.capture())
        assertThat(
            remoteRepository.getAuthors(currentPage, limit),
            instanceOf(Flowable::class.java)
        )
    }

    @Test
    fun getPosts() {
        val postsList = emptyList<Post>()
        val observable = Flowable.just(postsList)
        val authorId = 1
        val argumentCaptor = ArgumentCaptor.forClass(Int::class.java)
        whenever(remoteRepository.getPosts(authorId, currentPage, limit)).thenReturn(observable)

        repository?.getPosts(authorId, currentPage, limit)

        verify(remoteRepository).getPosts(
            argumentCaptor.capture(),
            argumentCaptor.capture(),
            argumentCaptor.capture()
        )
        assertThat(
            remoteRepository.getPosts(authorId, currentPage, limit),
            instanceOf(Flowable::class.java)
        )
    }

    @Test
    fun getComments() {
        val commentsList = emptyList<Comment>()
        val observable = Flowable.just(commentsList)
        val postId = 1
        val argumentCaptor = ArgumentCaptor.forClass(Int::class.java)

        whenever(remoteRepository.getComments(postId, currentPage, limit)).thenReturn(observable)
        repository?.getComments(postId, currentPage, limit)

        verify(remoteRepository).getComments(
            argumentCaptor.capture(),
            argumentCaptor.capture(),
            argumentCaptor.capture()
        )
        assertThat(
            remoteRepository.getComments(postId, currentPage, limit),
            instanceOf(Flowable::class.java)
        )
    }
}