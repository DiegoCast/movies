package com.diego.movies.domain.movies

import com.diego.movies.domain.model.Page
import com.diego.movies.domain.model.Response
import com.nhaarman.mockitokotlin2.mock
import createMovieList
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetMoviesUseCaseTest {
    
    private lateinit var useCase: GetMoviesUseCase
    
    private val repository = mock<MoviesRepository> {}
    
    private val response = Response(Page(createMovieList(2), 0), true)
    
    @Before
    fun setUp() {
        useCase = GetMoviesUseCase(repository)
    }
    
    @Test
    fun `get`() {
        
        // given
        Mockito.`when`(repository.get()).thenReturn(Observable.just(response))
        
        // when
        val test = useCase.get().test()
        
        // then
        Mockito.verify(repository).get()
        test.assertValue(response)
    }
    
    @Test
    fun `next`() {
        
        // when
        useCase.next(0)
        
        // then
        Mockito.verify(repository).next(0)
    }
}