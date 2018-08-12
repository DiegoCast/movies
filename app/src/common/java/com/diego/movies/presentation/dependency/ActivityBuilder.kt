package com.diego.movies.presentation.dependency

import com.diego.movies.presentation.dependency.detail.DetailModule
import com.diego.movies.presentation.dependency.launcher.LauncherModule
import com.diego.movies.presentation.dependency.movies.MoviesModule
import com.diego.movies.presentation.dependency.movies.MoviesScope
import com.diego.movies.presentation.detail.DetailActivity
import com.diego.movies.presentation.launcher.LauncherActivity
import com.diego.movies.presentation.movies.MoviesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    
    @MoviesScope
    @ContributesAndroidInjector(modules = arrayOf(MoviesModule::class))
    internal abstract fun bindMoviesActivity(): MoviesActivity
    
    @ContributesAndroidInjector(modules = arrayOf(LauncherModule::class))
    internal abstract fun bindLauncherActivity(): LauncherActivity
    
    @ContributesAndroidInjector(modules = arrayOf(DetailModule::class))
    internal abstract fun bindDetailActivity(): DetailActivity
}