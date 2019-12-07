/*
 * PerActivity.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.injection.annotations

import javax.inject.Scope

/**
 *  @author Sanjay Sah
 */

/**
 * A scoping annotation to permit objects whose lifetime should
 * conform to the life of the Activity to be memorised in the
 * correct component.
 */
@MustBeDocumented
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity