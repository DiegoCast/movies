package com.diego.movies.domain.movies

import com.diego.movies.domain.model.Page
import com.diego.movies.domain.model.Response
import com.nhaarman.mockitokotlin2.mock
import createMovieList
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetSimilarShowsUseCaseTest {
    
    private lateinit var useCase: GetSimilarShowsUseCase
    
    private val repository = mock<MoviesRepository> {}
    
    private val response = Response(Page(createMovieList(2), 0), true)
    
    @Before
    fun setUp() {
        useCase = GetSimilarShowsUseCase(repository)
    }
    
    @Test
    fun `get`() {
        
        // given
        Mockito.`when`(repository.getSimilar(1)).thenReturn(Observable.just(response))
        
        // when
        val test = useCase.get(1).test()
        
        // then
        Mockito.verify(repository).getSimilar(1)
        test.assertValue(response)
    }
    
    @Test
    fun `next`() {
        
        // when
        useCase.next(1, 1)
        
        // then
        Mockito.verify(repository).nextSimilar(1, 1)
    }
}