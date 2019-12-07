/*
 * AppModule.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.injection.module


import android.content.Context
import com.sanjay.authorslist.AuthorsApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Sanjay Sah
 */

@Module
open class AppModule(private val application: AuthorsApplication) {

    @Provides
    @Singleton
    fun providesApplicationContext(): Context = application

    @Provides
    @Singleton
    fun providesApplication(): AuthorsApplication = application

}