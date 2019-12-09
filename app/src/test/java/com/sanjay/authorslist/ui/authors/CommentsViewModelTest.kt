package com.sanjay.authorslist.ui.authors

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sanjay.authorslist.data.repository.remote.model.Comment
import com.sanjay.authorslist.data.repository.remote.model.Post
import com.sanjay.authorslist.mockPagedList
import com.sanjay.authorslist.pagination.datasource.CommentsPagingDataSource
import com.sanjay.authorslist.pagination.datasource.CommentsPagingDataSourceFactory
import com.sanjay.authorslist.ui.comments.CommentsViewModel
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

class CommentsViewModelTest {

    private var viewModel: CommentsViewModel? = null
    @Mock
    lateinit var pagingDataSourceFactory: CommentsPagingDataSourceFactory

    @Mock
    lateinit var pagingDataSource: CommentsPagingDataSource

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(pagingDataSourceFactory.commentsPagingDataSource).thenReturn(pagingDataSource)
        whenever(pagingDataSourceFactory.commentsPagingDataSource.state).thenReturn(MutableLiveData())

        viewModel = CommentsViewModel(pagingDataSourceFactory)
    }

    @After
    fun close() {
        viewModel = null
    }

    @Test
    fun test_Initialization() {

        assertNotNull(viewModel?.commentsList)
        assertNotNull(viewModel?.state)
        assertNull(viewModel?.selectedPost)

        viewModel?.selectedPost?.value =
            Post(1, "", "", "", "", 1)

        verify(pagingDataSourceFactory.commentsPagingDataSource, times(1)).selectedPost

    }

    @Test
    fun test_Retry() {
        viewModel?.retry()

        verify(pagingDataSourceFactory.commentsPagingDataSource).retry()

    }

    @Test
    fun test_ListIsEmpty() {

        assertTrue(viewModel!!.listIsEmpty())

        val post = Comment(1, "", "", "", "", "",1)
        val pagedPostsList = MutableLiveData(mockPagedList(listOf(post)))

        viewModel?.commentsList = pagedPostsList

        assertFalse(viewModel!!.listIsEmpty())

    }

    @Test
    fun test_Disposable() {
        viewModel?.disposable

        verify(pagingDataSourceFactory.commentsPagingDataSource).disposable

    }

}