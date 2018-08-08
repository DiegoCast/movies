package com.diego.movies.presentation.movies

import com.diego.movies.domain.model.Movie
import com.diego.movies.domain.model.Page
import com.diego.movies.domain.model.Response
import com.diego.movies.domain.movies.GetMoviesUseCase
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import createMovieList
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MoviesPresenterTest {
    
    private lateinit var presenter: MoviesPresenter
    
    private val view = mock<MoviesView> {}
    private val getMoviesUseCase = mock<GetMoviesUseCase> {}
    private val testScheduler = TestScheduler()
    
    private lateinit var scrollConsumer: MoviesPresenter.ScrollConsumer
    
    private var movies = createMovieList(10)
    
    @Before
    fun setUp() {
        Mockito.`when`(view.getScrollObservable()).thenReturn(Observable.just(0))
        presenter = MoviesPresenter(view, getMoviesUseCase, testScheduler, testScheduler)
        scrollConsumer = presenter.ScrollConsumer()
    }
    
    @Test
    fun start() {
        val response = Response(Page(movies, 0), true)
        
        // given
        Mockito.`when`(getMoviesUseCase.get()).thenReturn(Observable.just(response))
        
        // when
        presenter.start()
        testScheduler.triggerActions()
        
        // then
        Mockito.verify(view).getScrollObservable()
        Mockito.verify(getMoviesUseCase).get()
        Mockito.verify(view).show(movies)
    }
    
    @Test
    fun `start unsuccessful`() {
        val response = Response(Page(emptyList<Movie>(), 0), false)
        
        // given
        Mockito.`when`(getMoviesUseCase.get()).thenReturn(Observable.just(response))
        
        // when
        presenter.start()
        testScheduler.triggerActions()
        
        // then
        Mockito.verify(getMoviesUseCase).get()
        Mockito.verify(view).showError()
    }
    
    @Test
    fun `start error`() {
        
        // given
        Mockito.`when`(getMoviesUseCase.get()).thenReturn(Observable.error(Throwable()))
        
        // when
        presenter.start()
        testScheduler.triggerActions()
        
        // then
        Mockito.verify(getMoviesUseCase).get()
        Mockito.verify(view).showError()
    }
    
    @Test
    fun retry() {
        val response = Response(Page(movies, 0), true)
        
        // given
        Mockito.`when`(getMoviesUseCase.get()).thenReturn(Observable.just(response))
        
        // when
        presenter.start()
        testScheduler.triggerActions()
        presenter.retry()
        testScheduler.triggerActions()
        
        // then
        Mockito.verify(getMoviesUseCase, times(2)).get()
        Mockito.verify(view, times(2)).show(movies)
    }
    
    @Test
    fun next() {
        val response = Response(Page(movies, 0), true)
        
        // given
        Mockito.`when`(getMoviesUseCase.get()).thenReturn(Observable.just(response))
        
        // when
        presenter.start()
        testScheduler.triggerActions()
        Observable.just(7).subscribe(scrollConsumer)
        
        // then
        Mockito.verify(view).getScrollObservable()
        Mockito.verify(getMoviesUseCase).next(1)
        Mockito.verify(getMoviesUseCase).get()
        Mockito.verify(view).show(movies)
    }
    
    @Test
    fun `next not enough scroll`() {
        val response = Response(Page(movies, 0), true)
    
        // given
        Mockito.`when`(getMoviesUseCase.get()).thenReturn(Observable.just(response))
    
        // when
        presenter.start()
        testScheduler.triggerActions()
        Observable.just(4).subscribe(scrollConsumer)
        Observable.just(5).subscribe(scrollConsumer)
        Observable.just(6).subscribe(scrollConsumer)
    
        // then
        Mockito.verify(view).getScrollObservable()
        Mockito.verify(getMoviesUseCase).get()
        verifyNoMoreInteractions(getMoviesUseCase)
        Mockito.verify(view).show(movies)
    }
}