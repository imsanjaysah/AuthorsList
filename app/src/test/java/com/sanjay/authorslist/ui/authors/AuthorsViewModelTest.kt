package com.sanjay.authorslist.ui.authors

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import com.sanjay.authorslist.constants.State
import com.sanjay.authorslist.data.repository.AuthorsRepository
import com.sanjay.authorslist.data.repository.remote.model.Author
import io.reactivex.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class AuthorsViewModelTest {

    @Mock
    lateinit var repository: AuthorsRepository

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private var observerState = mock<Observer<State>>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @After
    fun close() {
        //viewModel = null
    }

    @Test
    fun test_authorsApiCall() {
        val authorsList = emptyList<Author>()
        val observable = Flowable.just(authorsList)
        val currentPage = 1
        val limit = 20

        whenever(repository.getAuthors(currentPage, limit)).thenReturn(observable)

        val viewModel = AuthorsViewModel(repository)

        viewModel.state.observeForever(observerState)

        //verify(repository).getAuthors(currentPage, limit)
        verify(observerState).onChanged(State.LOADING)
    }

}