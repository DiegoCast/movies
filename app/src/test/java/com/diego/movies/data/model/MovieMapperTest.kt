package com.diego.movies.data.model

import com.diego.movies.data.repository.ConfigurationRepository
import com.nhaarman.mockitokotlin2.mock
import createMovieEntity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MovieMapperTest {
    
    private lateinit var mapper: MovieMapper
    
    private val preferencesRepository = mock<ConfigurationRepository> {}
    private val movieEntity = createMovieEntity(0)
    
    @Before
    fun setUp() {
        Mockito.`when`(preferencesRepository.imageBaseUrl).thenReturn("http://url/")
        Mockito.`when`(preferencesRepository.imageSize).thenReturn("size")
        mapper = MovieMapper(preferencesRepository)
    }
    
    @Test
    fun `map entity to domain`() {
        
        // when
        val model = mapper.mapToDomain(movieEntity)
        
        // then
        assertTrue(model.id == movieEntity.id)
        assertTrue(model.title == movieEntity.name)
        assertTrue(model.voteAverage == movieEntity.voteAverage)
        assertTrue(model.imageUrl == "http://url/" + "size" + movieEntity.posterPath)
    }
}