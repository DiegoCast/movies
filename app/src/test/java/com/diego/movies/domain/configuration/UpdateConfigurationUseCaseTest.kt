package com.diego.movies.domain.configuration

import com.diego.movies.domain.movies.MoviesRepository
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class UpdateConfigurationUseCaseTest {
    
    private lateinit var useCase: UpdateConfigurationUseCase
    
    private val repository = mock<MoviesRepository> {}
    
    @Before
    fun setUp() {
        useCase = UpdateConfigurationUseCase(repository)
    }
    
    @Test
    fun update() {
        
        // given
        Mockito.`when`(repository.updateConfiguration()).thenReturn(Single.just(true))
        
        // when
        val test = useCase.update().test()
        
        // then
        Mockito.verify(repository).updateConfiguration()
        test.assertValue(true)
    }
}