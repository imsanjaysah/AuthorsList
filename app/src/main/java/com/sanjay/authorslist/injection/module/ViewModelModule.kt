/*
 * ViewModelModule.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sanjay.authorslist.injection.ViewModelFactory
import com.sanjay.authorslist.ui.authors.AuthorsViewModel
import com.sanjay.authorslist.ui.posts.PostsViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
open class ViewModelModule {

    @Provides
    fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory = factory

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(AuthorsViewModel::class)
    fun providesAuthorsListViewModel(viewModel: AuthorsViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(PostsViewModel::class)
    fun providesPostsViewModel(viewModel: PostsViewModel): ViewModel = viewModel


}