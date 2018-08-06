package com.diego.movies.presentation.movies

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.diego.movies.domain.movies.GetMoviesUseCase
import io.reactivex.Scheduler
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Named

class MoviesPresenter @Inject constructor(private val view: MoviesView,
                                          private val getMoviesUseCase: GetMoviesUseCase,
                                          @Named("main") private val main: Scheduler,
                                          @Named("io") private val io: Scheduler) : LifecycleObserver {
    
    private val compositeDisposable = CompositeDisposable()
    private var page: Int = 0
    
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        subscribeToMovies()
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        compositeDisposable.clear()
    }
    
    fun retry() {
        compositeDisposable.clear()
        subscribeToMovies()
    }
    
    fun next() {
        getMoviesUseCase.next(page + 1)
    }
    
    private fun subscribeToMovies() {
        compositeDisposable.add(getMoviesUseCase.get()
                .subscribeOn(io)
                .observeOn(main)
                .subscribe({ response ->
                    page = response.result.page
                    if (response.succesful) {
                        view.show(response.result.data)
                    } else {
                        view.showError()
                    }
                }, {
                    it.printStackTrace()
                    view.showError()
                }))
    }
}