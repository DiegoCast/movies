package com.diego.movies.presentation.movies

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.view.View
import android.widget.ImageView
import com.diego.movies.domain.model.Movie
import com.diego.movies.domain.movies.GetMoviesUseCase
import com.diego.movies.presentation.Navigator
import io.reactivex.Scheduler
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit
import javax.inject.Named

open class MoviesPresenter @Inject constructor(private val view: MoviesView,
                                               private val getMoviesUseCase: GetMoviesUseCase,
                                               private val navigator: Navigator,
                                               @Named("main") private val main: Scheduler,
                                               @Named("io") private val io: Scheduler) : LifecycleObserver {
    private val paginationPositionOffset = 4
    
    private val compositeDisposable = CompositeDisposable()
    
    private var moviesSize: Int = 0
    private var page: Int = 0
    
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun start() {
        subscribeToMovies()
        subscribeToScrollObservable()
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stop() {
        compositeDisposable.clear()
    }
    
    fun retry() {
        moviesSize = 0
        page = 0
        compositeDisposable.clear()
        start()
    }
    
    private fun next() {
        getMoviesUseCase.next(page + 1)
    }
    
    private fun subscribeToMovies() {
        compositeDisposable.add(getMoviesUseCase.get()
                .subscribeOn(io)
                .observeOn(main)
                .subscribe({ response ->
                    page = response.result.page
                    if (response.succesful) {
                        val data = response.result.data
                        moviesSize = data.size
                        view.show(response.result.data)
                    } else {
                        view.showError()
                    }
                }, {
                    it.printStackTrace()
                    view.showError()
                }))
    }
    
    private fun subscribeToScrollObservable() {
        compositeDisposable.add(view.getScrollObservable()
                .observeOn(main)
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribe(ScrollConsumer()))
    }
    
    fun detail(activity: Activity, view: ImageView, movie: Movie) {
        navigator.navigateToDetail(activity, view, movie)
    }
    
    inner class ScrollConsumer : Consumer<Int> {
        override fun accept(position: Int) {
            if (moviesSize > 0 && position + paginationPositionOffset > moviesSize) {
                next()
            }
        }
    }
}