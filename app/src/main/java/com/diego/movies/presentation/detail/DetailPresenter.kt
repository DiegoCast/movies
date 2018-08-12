package com.diego.movies.presentation.detail

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.diego.movies.domain.model.Movie
import com.diego.movies.domain.movies.GetSimilarShowsUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class DetailPresenter @Inject constructor(private val view: DetailView,
                                          private val getSimilarShowsUseCase: GetSimilarShowsUseCase,
                                          @Named("main") private val main: Scheduler,
                                          @Named("io") private val io: Scheduler,
                                          private val movie: Movie) : LifecycleObserver {
    
    private val compositeDisposable = CompositeDisposable()
    
    private var moviesSize: Int = 0
    private var page: Int = 0
    
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun create() {
        view.showPoster(movie.imageUrl)
        subscribeToSimilarShows()
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        view.show(movie.title, movie.voteAverage, movie.voteCount, movie.description, movie.backgroundUrl)
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        compositeDisposable.clear()
    }
    
    private fun subscribeToSimilarShows() {
        compositeDisposable.add(getSimilarShowsUseCase.get(movie.id)
                .subscribeOn(io)
                .observeOn(main)
                .subscribe({ response ->
                    page = response.result.page
                    if (response.succesful) {
                        val data = response.result.data
                        moviesSize = data.size
                        view.showSimilar(response.result.data)
                    } else {
                        view.showError()
                    }
                }, {
                    it.printStackTrace()
                    view.showError()
                }))
    }
}