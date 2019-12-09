package com.sanjay.authorslist.pagination.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PageKeyedDataSource
import com.nhaarman.mockitokotlin2.*
import com.sanjay.authorslist.RxImmediateSchedulerRule
import com.sanjay.authorslist.constants.State
import com.sanjay.authorslist.data.repository.AuthorsRepository
import com.sanjay.authorslist.data.repository.remote.model.Comment
import com.sanjay.authorslist.data.repository.remote.model.Post
import io.reactivex.Flowable
import org.junit.*
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class CommentsPagingDataSourceTest {

    private var pagingDataSource: CommentsPagingDataSource? = null

    @Mock
    lateinit var repository: AuthorsRepository

    private var loadParams = PageKeyedDataSource.LoadParams(1, 20)

    @Mock
    lateinit var loadCallback: PageKeyedDataSource.LoadCallback<Int, Comment>

    private var loadInitialParams = PageKeyedDataSource.LoadInitialParams<Int>(20, false)

    @Mock
    lateinit var loadInitialCallback: PageKeyedDataSource.LoadInitialCallback<Int, Comment>

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private var observerState = mock<Observer<State>>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        pagingDataSource = CommentsPagingDataSource(repository)
        pagingDataSource!!.selectedPost.value =
            Post(1, "", "", "", "",1)
    }

    @After
    fun tearDown() {
        pagingDataSource = null
    }

    @Test
    fun loadInitial_Success() {
        val commentsList = emptyList<Comment>()
        val observable = Flowable.just(commentsList)

        whenever(repository.getComments(1, 1, loadInitialParams.requestedLoadSize)).thenReturn(
            observable
        )

        pagingDataSource!!.state.observeForever(observerState)

        pagingDataSource!!.loadInitial(loadInitialParams, loadInitialCallback)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedLoadingState = State.LOADING
        val expectedDoneState = State.DONE

        verify(repository).getComments(1, 1, loadInitialParams.requestedLoadSize)
        verify(loadInitialCallback).onResult(commentsList, null, 2)
        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(capture())
            val (loadingState, doneState) = allValues
            assertEquals(loadingState, expectedLoadingState)
            assertEquals(doneState, expectedDoneState)
        }
    }

    @Test
    fun loadInitial_Error() {
        val errorMessage = "Error response"
        val response = Throwable(errorMessage)

        whenever(repository.getComments(1, 1, loadInitialParams.requestedLoadSize)).thenReturn(
            Flowable.error(response)
        )

        pagingDataSource!!.state.observeForever(observerState)

        pagingDataSource!!.loadInitial(loadInitialParams, loadInitialCallback)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedLoadingState = State.LOADING
        val expectedDoneState = State.ERROR

        verify(repository).getComments(1, 1, loadInitialParams.requestedLoadSize)

        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(capture())
            val (loadingState, doneState) = allValues
            assertEquals(loadingState, expectedLoadingState)
            assertEquals(doneState, expectedDoneState)
        }

    }

    @Test
    fun loadInitial_Retry() {

        val errorMessage = "Error response"
        val response = Throwable(errorMessage)

        whenever(
            repository.getComments(
                1,
                1,
                loadInitialParams.requestedLoadSize
            )
        ).thenReturn(Flowable.error(response))

        pagingDataSource!!.state.observeForever(observerState)

        pagingDataSource!!.loadInitial(loadInitialParams, loadInitialCallback)

        pagingDataSource!!.retry()

        verify(repository, times(2)).getComments(1, 1, loadInitialParams.requestedLoadSize)
    }

    @Test
    fun loadAfter_Success() {
        val commentsList = emptyList<Comment>()
        val observable = Flowable.just(commentsList)

        whenever(repository.getComments(1, loadParams.key, loadParams.requestedLoadSize)).thenReturn(
            observable
        )

        pagingDataSource!!.state.observeForever(observerState)

        pagingDataSource!!.loadAfter(loadParams, loadCallback)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedLoadingState = State.LOADING
        val expectedDoneState = State.DONE

        verify(repository).getComments(1, loadParams.key, loadParams.requestedLoadSize)
        verify(loadCallback).onResult(any(), any())
        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(capture())
            val (loadingState, doneState) = allValues
            assertEquals(loadingState, expectedLoadingState)
            assertEquals(doneState, expectedDoneState)
        }
    }

    @Test
    fun loadAfter_Error() {
        val errorMessage = "Error response"
        val response = Throwable(errorMessage)

        whenever(
            repository.getComments(
                1,
                loadParams.key,
                loadParams.requestedLoadSize
            )
        ).thenReturn(Flowable.error(response))

        pagingDataSource!!.state.observeForever(observerState)

        pagingDataSource!!.loadAfter(loadParams, loadCallback)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedLoadingState = State.LOADING
        val expectedDoneState = State.ERROR

        verify(repository).getComments(1, loadParams.key, loadParams.requestedLoadSize)

        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(capture())
            val (loadingState, doneState) = allValues
            assertEquals(loadingState, expectedLoadingState)
            assertEquals(doneState, expectedDoneState)
        }

    }

    @Test
    fun loadAfter_Retry() {

        val errorMessage = "Error response"
        val response = Throwable(errorMessage)

        whenever(
            repository.getComments(
                1,
                loadParams.key,
                loadParams.requestedLoadSize
            )
        ).thenReturn(Flowable.error(response))

        pagingDataSource!!.state.observeForever(observerState)

        pagingDataSource!!.loadAfter(loadParams, loadCallback)

        pagingDataSource!!.retry()

        verify(repository, times(2)).getComments(1, loadParams.key, loadParams.requestedLoadSize)
    }
}