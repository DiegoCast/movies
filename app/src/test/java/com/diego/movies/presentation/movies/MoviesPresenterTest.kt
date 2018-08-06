package com.diego.movies.presentation.movies

import com.diego.movies.domain.model.Movie
import com.diego.movies.domain.model.Page
import com.diego.movies.domain.model.Response
import com.diego.movies.domain.movies.GetMoviesUseCase
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
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
    
    private var movies = createMovieList(2)
    
    @Before
    fun setUp() {
        presenter = MoviesPresenter(view, getMoviesUseCase, testScheduler, testScheduler)
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
    fun next() {
        val response = Response(Page(movies, 0), true)
        
        // given
        Mockito.`when`(getMoviesUseCase.get()).thenReturn(Observable.just(response))
        
        // when
        presenter.start()
        testScheduler.triggerActions()
        presenter.next()
        
        // then
        Mockito.verify(getMoviesUseCase).get()
        Mockito.verify(view).show(movies)
        Mockito.verify(getMoviesUseCase).next(1)
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
}