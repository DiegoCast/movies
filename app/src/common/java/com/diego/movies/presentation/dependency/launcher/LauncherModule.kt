package com.diego.movies.presentation.dependency.launcher

import com.diego.movies.presentation.launcher.LauncherActivity
import com.diego.movies.presentation.launcher.LauncherView
import com.diego.movies.presentation.movies.MoviesView
import dagger.Module
import dagger.Provides

@Module
class LauncherModule {
    
    @Provides
    fun provideView(launcherActivity: LauncherActivity): LauncherView {
        return launcherActivity
    }
}