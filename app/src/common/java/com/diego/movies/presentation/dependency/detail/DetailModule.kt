package com.diego.movies.presentation.dependency.detail

import com.diego.movies.domain.model.Movie
import com.diego.movies.presentation.detail.DetailActivity
import com.diego.movies.presentation.detail.DetailView
import dagger.Module
import dagger.Provides

@Module
class DetailModule {
    
    @Provides
    fun provideView(detailActivity: DetailActivity): DetailView {
        return detailActivity
    }
    
    @Provides
    fun provideMovie(detailActivity: DetailActivity): Movie {
        val extras = detailActivity.intent.extras
        return extras.get(DetailActivity.movieTag) as Movie
    }
}