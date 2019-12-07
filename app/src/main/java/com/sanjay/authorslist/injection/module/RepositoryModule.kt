/*
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.injection.module

import com.sanjay.authorslist.data.repository.AuthorsDataSource
import com.sanjay.authorslist.data.repository.local.AuthorsLocalDataSource
import com.sanjay.authorslist.data.repository.remote.AuthorsRemoteDataSource
import com.sanjay.authorslist.injection.annotations.Local
import com.sanjay.authorslist.injection.annotations.Remote
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Sanjay Sah on 14/11/2017.
 */

@Module
class RepositoryModule {

    @Provides
    @Singleton
    @Local
    fun providesLocalDataSource(localDataSource: AuthorsLocalDataSource): AuthorsDataSource = localDataSource

    @Provides
    @Singleton
    @Remote
    fun providesRemoteDataSource(remoteDataSource: AuthorsRemoteDataSource): AuthorsDataSource = remoteDataSource

}