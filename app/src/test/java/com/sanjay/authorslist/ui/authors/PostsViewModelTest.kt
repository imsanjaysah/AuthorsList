package com.sanjay.authorslist.ui.authors

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sanjay.authorslist.data.repository.remote.model.Author
import com.sanjay.authorslist.data.repository.remote.model.Post
import com.sanjay.authorslist.mockPagedList
import com.sanjay.authorslist.pagination.datasource.PostsPagingDataSource
import com.sanjay.authorslist.pagination.datasource.PostsPagingDataSourceFactory
import com.sanjay.authorslist.ui.posts.PostsViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PostsViewModelTest {

    private var viewModel: PostsViewModel? = null
    @Mock
    lateinit var pagingDataSourceFactory: PostsPagingDataSourceFactory

    @Mock
    lateinit var pagingDataSource: PostsPagingDataSource

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(pagingDataSourceFactory.postsPagingDataSource).thenReturn(pagingDataSource)
        whenever(pagingDataSourceFactory.postsPagingDataSource.state).thenReturn(MutableLiveData())

        viewModel = PostsViewModel(pagingDataSourceFactory)
    }

    @After
    fun close() {
        viewModel = null
    }

    @Test
    fun test_Initialization() {

        assertNotNull(viewModel?.postsList)
        assertNotNull(viewModel?.state)
        assertNull(viewModel?.selectedAuthor)

        viewModel?.selectedAuthor?.value =
            Author(1, "Sanjay", "sanjay.sah", "sanjays644@gmail.com", "")

        verify(pagingDataSourceFactory.postsPagingDataSource, times(1)).selectedAuthor

    }

    @Test
    fun test_Retry() {
        viewModel?.retry()

        verify(pagingDataSourceFactory.postsPagingDataSource).retry()

    }

    @Test
    fun test_ListIsEmpty() {

        assertTrue(viewModel!!.listIsEmpty())

        val post = Post(1, "", "", "", "", 1)
        val pagedPostsList = MutableLiveData(mockPagedList(listOf(post)))

        viewModel?.postsList = pagedPostsList

        assertFalse(viewModel!!.listIsEmpty())

    }

    @Test
    fun test_Disposable() {
        viewModel?.disposable

        verify(pagingDataSourceFactory.postsPagingDataSource).disposable

    }

}