/*
 * ActivityComponent.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.injection

import com.sanjay.authorslist.injection.annotations.PerActivity
import com.sanjay.authorslist.injection.module.ActivityModule
import com.sanjay.authorslist.ui.authors.AuthorsListActivity
import dagger.Subcomponent

/**
 * @author Sanjay Sah
 */

@PerActivity
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(activity: AuthorsListActivity)


}