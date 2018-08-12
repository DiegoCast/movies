package com.diego.movies.presentation.detail

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.diego.movies.domain.model.Movie
import com.diego.movies.domain.movies.GetSimilarShowsUseCase
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class DetailPresenter @Inject constructor(private val view: DetailView,
                                          private val movie: Movie) : LifecycleObserver {
    
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun create() {
        view.showPoster(movie.imageUrl)
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        view.show(movie.title, movie.voteAverage, movie.voteCount, movie.description, movie.backgroundUrl)
    }
}