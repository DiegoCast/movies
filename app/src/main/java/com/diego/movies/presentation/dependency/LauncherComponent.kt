package com.diego.movies.presentation.dependency
import com.diego.movies.presentation.dependency.launcher.LauncherModule
import com.diego.movies.presentation.launcher.LauncherActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(LauncherModule::class))
interface LauncherComponent {
    
    fun inject(activity: LauncherActivity)
}