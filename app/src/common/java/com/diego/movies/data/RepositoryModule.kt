package com.diego.movies.data

import com.diego.movies.data.repository.MoviesApiRepository
import com.diego.movies.domain.movies.MoviesRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    
    @Binds
    abstract fun bindMoviesRepository(repository: MoviesApiRepository): MoviesRepository
}