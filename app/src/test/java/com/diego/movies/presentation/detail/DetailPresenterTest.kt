package com.diego.movies.presentation.detail

import android.app.Activity
import android.widget.ImageView
import com.diego.movies.domain.model.Movie
import com.diego.movies.domain.model.Page
import com.diego.movies.domain.model.Response
import com.diego.movies.domain.movies.GetSimilarShowsUseCase
import com.diego.movies.presentation.Navigator
import com.diego.movies.presentation.movies.MoviesPresenter
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import createMovie
import createMovieList
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class DetailPresenterTest {
    
    private lateinit var presenter: DetailPresenter
    
    private val view = mock<DetailView> {}
    private val getSimilarShowsUseCase = mock<GetSimilarShowsUseCase> {}
    private val navigator = mock<Navigator> {}
    private val testScheduler = TestScheduler()
    
    private lateinit var scrollConsumer: DetailPresenter.ScrollConsumer
    
    private val movie = createMovie(1)
    private var movies = createMovieList(10)
    
    @Before
    fun setUp() {
        Mockito.`when`(view.getScrollObservable()).thenReturn(Observable.just(0))
        presenter = DetailPresenter(view, getSimilarShowsUseCase, navigator, testScheduler, testScheduler, movie)
        scrollConsumer = presenter.ScrollConsumer()
    }
    
    @Test
    fun create() {
        val response = Response(Page(movies, 0), true)
        
        // given
        Mockito.`when`(getSimilarShowsUseCase.get(1)).thenReturn(Observable.just(response))
        
        //when
        presenter.create()
        testScheduler.triggerActions()
        
        //then
        verify(view).showPoster(movie.imageUrl)
        Mockito.verify(view).getScrollObservable()
        Mockito.verify(getSimilarShowsUseCase).get(1)
        Mockito.verify(view).showSimilar(movies)
    }
    
    @Test
    fun createUnsuccessful() {
        val response = Response(Page(emptyList<Movie>(), 0), false)
        
        // given
        Mockito.`when`(getSimilarShowsUseCase.get(1)).thenReturn(Observable.just(response))
        
        //when
        presenter.create()
        testScheduler.triggerActions()
        
        //then
        verify(view).showPoster(movie.imageUrl)
        Mockito.verify(view).getScrollObservable()
        Mockito.verify(getSimilarShowsUseCase).get(1)
        Mockito.verify(view).showError()
    }
    
    @Test
    fun `create error`() {
        
        // given
        Mockito.`when`(getSimilarShowsUseCase.get(1)).thenReturn(Observable.error(Throwable()))
        
        // when
        presenter.create()
        testScheduler.triggerActions()
        
        // then
        Mockito.verify(view).getScrollObservable()
        Mockito.verify(getSimilarShowsUseCase).get(1)
        Mockito.verify(view).showError()
    }
    
    @Test
    fun next() {
        val response = Response(Page(movies, 0), true)
    
        // given
        Mockito.`when`(getSimilarShowsUseCase.get(1)).thenReturn(Observable.just(response))
        
        // when
        presenter.create()
        testScheduler.triggerActions()
        Observable.just(9).subscribe(scrollConsumer)
        
        // then
        Mockito.verify(view).getScrollObservable()
        Mockito.verify(getSimilarShowsUseCase).next(1, 1)
        Mockito.verify(getSimilarShowsUseCase).get(1)
        Mockito.verify(view).showSimilar(movies)
    }
    
    @Test
    fun start() {
        
        // when
        presenter.start()
        
        // then
        verify(view).show(movie.title, movie.voteAverage, movie.voteCount, movie.description, movie.backgroundUrl)
    }
    
    @Test
    fun `next not enough scroll`() {
        val response = Response(Page(movies, 0), true)
        
        // given
        Mockito.`when`(getSimilarShowsUseCase.get(1)).thenReturn(Observable.just(response))
        
        // when
        presenter.create()
        testScheduler.triggerActions()
        Observable.just(6).subscribe(scrollConsumer)
        Observable.just(7).subscribe(scrollConsumer)
        Observable.just(8).subscribe(scrollConsumer)
        
        // then
        Mockito.verify(view).getScrollObservable()
        Mockito.verify(getSimilarShowsUseCase).get(1)
        verifyNoMoreInteractions(getSimilarShowsUseCase)
        Mockito.verify(view).showSimilar(movies)
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
    
    @Test
    fun retry() {
        val response = Response(Page(movies, 0), true)
        
        // given
        Mockito.`when`(getSimilarShowsUseCase.get(1)).thenReturn(Observable.just(response))
        
        // when
        presenter.create()
        testScheduler.triggerActions()
        presenter.retry()
        testScheduler.triggerActions()
        
        // then
        Mockito.verify(view, times(2)).getScrollObservable()
        Mockito.verify(getSimilarShowsUseCase, times(2)).get(1)
        Mockito.verify(view, times(2)).showSimilar(movies)
    }
}