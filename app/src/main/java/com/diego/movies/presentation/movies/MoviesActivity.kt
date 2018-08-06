package com.diego.movies.presentation.movies

import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.diego.movies.App
import com.diego.movies.R
import com.diego.movies.domain.model.Movie
import com.diego.movies.presentation.dependency.movies.MoviesModule
import javax.inject.Inject

class MoviesActivity : AppCompatActivity (), MoviesView, LifecycleOwner {
    
    @Inject lateinit var presenter: MoviesPresenter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.getAppComponent()
                .plusMoviesComponent(MoviesModule(this))
                .inject(this)
        lifecycle.addObserver(presenter)
    }
    
    
    override fun show(movies: List<Movie>) {
    
    }
}
