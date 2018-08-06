package com.diego.movies.presentation.movies

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.diego.movies.domain.movies.GetMoviesUseCase
import javax.inject.Inject

class MoviesPresenter @Inject constructor(private val view: MoviesView,
                                          private val getMoviesUseCase: GetMoviesUseCase) : LifecycleObserver {
    
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
    
    }
    
    fun next(page: Int) {
    
    }
}