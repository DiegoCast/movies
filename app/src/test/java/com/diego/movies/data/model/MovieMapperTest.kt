package com.diego.movies.data.model

import createMovieEntity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MovieMapperTest {
    
    private lateinit var mapper: MovieMapper
    
    private val movieEntity = createMovieEntity(0)
    
    @Before
    fun setUp() {
        mapper = MovieMapper()
    }
    
    @Test
    fun `map entity to domain`() {
        
        // when
        val model = mapper.mapToDomain(movieEntity)
        
        // then
        assertTrue(model.id == movieEntity.id)
        assertTrue(model.title == movieEntity.name)
        assertTrue(model.voteAverage == movieEntity.voteAverage)
        assertTrue(model.imageUrl == movieEntity.posterPath)
    }
}