package com.diego.movies.presentation.movies

import android.app.Activity
import android.widget.ImageView
import com.diego.movies.domain.model.Movie
import com.diego.movies.domain.model.Page
import com.diego.movies.domain.model.Response
import com.diego.movies.domain.movies.GetMoviesUseCase
import com.diego.movies.presentation.Navigator
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import createMovie
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
    private val navigator = mock<Navigator> {}
    private val testScheduler = TestScheduler()
    
    private lateinit var scrollConsumer: MoviesPresenter.ScrollConsumer
    
    private var movies = createMovieList(10)
    
    @Before
    fun setUp() {
        Mockito.`when`(view.getScrollObservable()).thenReturn(Observable.just(0))
        presenter = MoviesPresenter(view, getMoviesUseCase, navigator, testScheduler, testScheduler)
        scrollConsumer = presenter.ScrollConsumer()
    }
    
    @Test
    fun create() {
        val response = Response(Page(movies, 0), true)
        
        // given
        Mockito.`when`(getMoviesUseCase.get()).thenReturn(Observable.just(response))
        
        // when
        presenter.create()
        testScheduler.triggerActions()
        
        // then
        Mockito.verify(view).getScrollObservable()
        Mockito.verify(getMoviesUseCase).get()
        Mockito.verify(view).show(movies)
    }
    
    @Test
    fun `create unsuccessful`() {
        val response = Response(Page(emptyList<Movie>(), 0), false)
        
        // given
        Mockito.`when`(getMoviesUseCase.get()).thenReturn(Observable.just(response))
        
        // when
        presenter.create()
        testScheduler.triggerActions()
        
        // then
        Mockito.verify(view).getScrollObservable()
        Mockito.verify(getMoviesUseCase).get()
        Mockito.verify(view).showError()
    }
    
    @Test
    fun `create error`() {
        
        // given
        Mockito.`when`(getMoviesUseCase.get()).thenReturn(Observable.error(Throwable()))
        
        // when
        presenter.create()
        testScheduler.triggerActions()
        
        // then
        Mockito.verify(view).getScrollObservable()
        Mockito.verify(getMoviesUseCase).get()
        Mockito.verify(view).showError()
    }
    
    @Test
    fun retry() {
        val response = Response(Page(movies, 0), true)
        
        // given
        Mockito.`when`(getMoviesUseCase.get()).thenReturn(Observable.just(response))
        
        // when
        presenter.create()
        testScheduler.triggerActions()
        presenter.retry()
        testScheduler.triggerActions()
        
        // then
        Mockito.verify(view, times(2)).getScrollObservable()
        Mockito.verify(getMoviesUseCase, times(2)).get()
        Mockito.verify(view, times(2)).show(movies)
    }
    
    @Test
    fun next() {
        val response = Response(Page(movies, 0), true)
        
        // given
        Mockito.`when`(getMoviesUseCase.get()).thenReturn(Observable.just(response))
        
        // when
        presenter.create()
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
        presenter.create()
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
    
    @Test
    fun detail() {
        val movie = createMovie(1)
        val mockActivity = mock<Activity> {  }
        val mockView = mock<ImageView> {  }
        
        // when
        presenter.detail(mockActivity, mockView, movie)
        
        // then
        Mockito.verify(navigator).navigateToDetail(mockActivity, mockView, movie)
    }
}