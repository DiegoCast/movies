package com.diego.movies.presentation.movies

import android.app.Activity
import android.os.Bundle
import com.diego.movies.App
import com.diego.movies.R
import com.diego.movies.domain.model.Movie
import com.diego.movies.presentation.dependency.movies.MoviesModule

class MoviesActivity : Activity (), MoviesView {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.getAppComponent()
                .plusMoviesComponent(MoviesModule(this))
                .inject(this)
    }
    
    
    override fun show(movies: List<Movie>) {
    
    }
}
