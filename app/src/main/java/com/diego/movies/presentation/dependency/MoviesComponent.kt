package com.diego.movies.presentation.dependency

import com.diego.movies.presentation.dependency.movies.MoviesModule
import com.diego.movies.presentation.dependency.movies.MoviesScope
import com.diego.movies.presentation.movies.MoviesActivity
import dagger.Subcomponent

@MoviesScope
@Subcomponent(modules = arrayOf(MoviesModule::class))
interface MoviesComponent {
    
    fun inject(activity: MoviesActivity)
}