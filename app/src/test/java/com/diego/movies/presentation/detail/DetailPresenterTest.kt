package com.diego.movies.presentation.detail

import com.diego.movies.domain.model.Page
import com.diego.movies.domain.model.Response
import com.diego.movies.domain.movies.GetSimilarShowsUseCase
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
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
    private val testScheduler = TestScheduler()
    
    private val movie = createMovie(1)
    private var movies = createMovieList(10)
    
    @Before
    fun setUp() {
        presenter = DetailPresenter(view, getSimilarShowsUseCase, testScheduler, testScheduler, movie)
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
}