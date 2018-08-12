package com.diego.movies.presentation.detail

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.widget.ImageView
import com.diego.movies.domain.model.Movie
import com.diego.movies.domain.movies.GetSimilarShowsUseCase
import com.diego.movies.presentation.Navigator
import com.diego.movies.presentation.movies.MoviesPresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class DetailPresenter @Inject constructor(private val view: DetailView,
                                          private val getSimilarShowsUseCase: GetSimilarShowsUseCase,
                                          private val navigator: Navigator,
                                          @Named("main") private val main: Scheduler,
                                          @Named("io") private val io: Scheduler,
                                          private val movie: Movie) : LifecycleObserver {
    private val paginationPositionOffset = 2
    
    private val compositeDisposable = CompositeDisposable()
    
    private var moviesSize: Int = 0
    private var page: Int = 1
    
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun create() {
        view.showPoster(movie.imageUrl)
        subscribeToSimilarShows()
        subscribeToScrollObservable()
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        view.show(movie.title, movie.voteAverage, movie.voteCount, movie.description, movie.backgroundUrl)
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        compositeDisposable.clear()
    }
    
    private fun next() {
        getSimilarShowsUseCase.next(movie.id, page + 1)
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