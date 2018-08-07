package com.diego.movies.presentation.launcher

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.diego.movies.domain.configuration.UpdateConfigurationUseCase
import com.diego.movies.presentation.Navigator
import io.reactivex.Scheduler
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Named

class LauncherPresenter @Inject constructor(private val view: LauncherView,
                                            private val updateConfigurationUseCase: UpdateConfigurationUseCase,
                                            private val navigator: Navigator,
                                            @Named("main") private val main: Scheduler,
                                            @Named("io") private val io: Scheduler) : LifecycleObserver {
    
    private val compositeDisposable = CompositeDisposable()
    
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        updateConfigurationUseCase.update()
                .subscribeOn(io)
                .observeOn(main)
                .subscribe { response ->
                    if (!response) {
                        view.showError()
                    }
                    navigator.navigateToMovies()
                    view.close()
                }
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        compositeDisposable.clear()
    }
}