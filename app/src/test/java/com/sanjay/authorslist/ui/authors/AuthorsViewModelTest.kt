package com.sanjay.authorslist.ui.authors

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sanjay.authorslist.constants.State
import com.sanjay.authorslist.data.repository.remote.model.Author
import com.sanjay.authorslist.mockPagedList
import com.sanjay.authorslist.pagination.datasource.AuthorsPagingDataSource
import com.sanjay.authorslist.pagination.datasource.AuthorsPagingDataSourceFactory
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AuthorsViewModelTest {

    private var viewModel: AuthorsViewModel? = null
    @Mock
    lateinit var pagingDataSourceFactory: AuthorsPagingDataSourceFactory

    @Mock
    lateinit var pagingDataSource: AuthorsPagingDataSource

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private var observerState = mock<Observer<State>>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(pagingDataSourceFactory.authorsPagingDataSource).thenReturn(pagingDataSource)
        whenever(pagingDataSourceFactory.authorsPagingDataSource.state).thenReturn(MutableLiveData())

        viewModel = AuthorsViewModel(pagingDataSourceFactory)
    }

    @After
    fun close() {
        viewModel = null
    }

    @Test
    fun test_Initialization() {

        assertNotNull(viewModel?.authorsList)
        assertNotNull(viewModel?.state)

        /*viewModel.state.observeForever(observerState)
        verify(observerState).onChanged(State.LOADING)*/
    }

    @Test
    fun test_Retry() {
        viewModel?.retry()

        verify(pagingDataSourceFactory.authorsPagingDataSource).retry()

    }

    @Test
    fun test_ListIsEmpty() {

        assertTrue(viewModel!!.listIsEmpty())

        val author = Author(1, "Sanjay", "sanjay.sah", "sanjays644@gmail.com", "")
        val pagedAuthorsList = MutableLiveData(mockPagedList(listOf(author)))

        viewModel?.authorsList = pagedAuthorsList

        assertFalse(viewModel!!.listIsEmpty())

    }

    @Test
    fun test_Disposable() {
        viewModel?.disposable

        verify(pagingDataSourceFactory.authorsPagingDataSource).disposable

    }

}