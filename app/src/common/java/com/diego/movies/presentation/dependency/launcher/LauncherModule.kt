package com.diego.movies.presentation.dependency.launcher

import com.diego.movies.presentation.launcher.LauncherView
import dagger.Module
import dagger.Provides

@Module
class LauncherModule(val view: LauncherView) {
    
    @Provides
    fun provideView() = view
}