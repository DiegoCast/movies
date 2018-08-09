package com.diego.movies.presentation.dependency.movies

import com.diego.movies.presentation.movies.MoviesActivity
import com.diego.movies.presentation.movies.MoviesView
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@MoviesScope
@Module
class MoviesModule {
    
    @Provides
    fun provideView(moviesActivity: MoviesActivity): MoviesView {
        return moviesActivity
    }
}