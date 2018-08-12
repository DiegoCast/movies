package com.diego.movies.presentation.detail

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import createMovie
import org.junit.Before
import org.junit.Test

class DetailPresenterTest {
    
    private lateinit var presenter: DetailPresenter
    
    private val view = mock<DetailView> {}
    private val movie = createMovie(1)
    
    @Before
    fun setUp() {
        presenter = DetailPresenter(view, movie)
    }
    
    @Test
    fun create() {
        
        //when
        presenter.create()
        
        //then
        verify(view).showPoster(movie.imageUrl)
    }
    
    @Test
    fun start() {
        
        //when
        presenter.start()
        
        //then
        verify(view).show(movie.title, movie.voteAverage, movie.voteCount, movie.description, movie.backgroundUrl)
    }
}