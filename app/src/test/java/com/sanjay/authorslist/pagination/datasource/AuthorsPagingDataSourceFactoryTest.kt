package com.sanjay.authorslist.pagination.datasource

import com.nhaarman.mockitokotlin2.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class AuthorsPagingDataSourceFactoryTest {

    @Mock
    lateinit var pagingDataSource: AuthorsPagingDataSource

    private var pagingDataSourceFactory: AuthorsPagingDataSourceFactory? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        pagingDataSourceFactory = AuthorsPagingDataSourceFactory(pagingDataSource)
    }

    @After
    fun tearDown() {
        pagingDataSourceFactory = null
    }

    @Test
    fun test_Create() {
        verify(pagingDataSource)
    }
}