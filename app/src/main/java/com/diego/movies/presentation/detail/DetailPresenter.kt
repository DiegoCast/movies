package com.diego.movies.presentation.detail

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.diego.movies.domain.movies.GetSimilarShowsUseCase
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class DetailPresenter @Inject constructor(private val view: DetailView,
                                          private val getSimilarShowsUseCase: GetSimilarShowsUseCase,
                                          @Named("main") private val main: Scheduler,
                                          @Named("io") private val io: Scheduler) : LifecycleObserver {
    
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
    }
}