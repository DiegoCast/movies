package com.diego.movies.presentation.dependency

import com.diego.movies.data.NetworkModule
import com.diego.movies.data.RepositoryModule
import com.diego.movies.presentation.dependency.application.ApplicationModule
import com.diego.movies.presentation.dependency.launcher.LauncherModule
import com.diego.movies.presentation.dependency.movies.MoviesModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, NetworkModule::class, RepositoryModule::class))
interface ApplicationComponent {
    
    fun plusMoviesComponent(moviesModule: MoviesModule) : MoviesComponent
    
    fun plusLauncherComponent(launcherModule: LauncherModule) : LauncherComponent
}