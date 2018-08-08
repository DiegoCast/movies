package com.diego.movies.data.repository

import org.junit.Before
import org.junit.Test

class ConfigurationRepositoryTest {
    
    lateinit var repository: ConfigurationRepository
    
    @Before
    fun setUp() {
        repository = ConfigurationRepository()
    }
    
    @Test
    fun `update configuration` () {
        val baseUrl = "http://url"
        val list = listOf("w500", "w342", "w185", "w154", "w90")
        
        // when
        repository.updateConfiguration(baseUrl, list)
        
        // then
        assert(repository.imageBaseUrl == baseUrl)
        assert(repository.imageSize == "w500")
    }
    
    @Test
    fun `update configuration w342` () {
        val baseUrl = "http://url"
        val list = listOf("w342", "w185", "w154", "w90")
        
        // when
        repository.updateConfiguration(baseUrl, list)
        
        // then
        assert(repository.imageBaseUrl == baseUrl)
        assert(repository.imageSize == "w342")
    }
    
    @Test
    fun `update configuration w185` () {
        val baseUrl = "http://url"
        val list = listOf("w185", "w154", "w90")
        
        // when
        repository.updateConfiguration(baseUrl, list)
        
        // then
        assert(repository.imageBaseUrl == baseUrl)
        assert(repository.imageSize == "w185")
    }
    
    @Test
    fun `update configuration w154` () {
        val baseUrl = "http://url"
        val list = listOf("w154", "w90")
        
        // when
        repository.updateConfiguration(baseUrl, list)
        
        // then
        assert(repository.imageBaseUrl == baseUrl)
        assert(repository.imageSize == "w154")
    }
    
    @Test
    fun `update configuration none` () {
        val baseUrl = "http://url"
        val list = listOf("w90")
        
        // when
        repository.updateConfiguration(baseUrl, list)
        
        // then
        assert(repository.imageBaseUrl == baseUrl)
        assert(repository.imageSize == "w90")
    }
}