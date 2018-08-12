package com.diego.movies.data.api

import com.diego.movies.data.model.MovieMapper
import com.diego.movies.data.model.MovieResultsEntity
import com.diego.movies.data.repository.MoviesApiRepository
import com.diego.movies.data.repository.ConfigurationRepository
import com.diego.movies.domain.model.Movie
import com.diego.movies.domain.model.Page
import com.nhaarman.mockitokotlin2.mock
import createMovieResultsEntity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Response
import readFileContent
import java.io.File
import com.google.gson.JsonParser
import io.reactivex.Single

class MoviesApiRepositoryTest {
    
    private val language = "en"
    private val apiKey = "1234"
    
    private lateinit var repository: MoviesApiRepository
    
    private val mockApi = mock<MoviesRestApi> {}
    private val mockConfigurationRepository = mock<ConfigurationRepository> {}
    
    private lateinit var mapper: MovieMapper
    private val emitter: PublishSubject<Int> = PublishSubject.create()
    private val emitterSimilar: PublishSubject<Int> = PublishSubject.create()
    
    private val movieResultsEntityItem = createMovieResultsEntity(1, 2)
    private val movieResultsEntityNextItem = createMovieResultsEntity(2, 1)
    private val movieResultsEntityNext2Item = createMovieResultsEntity(3, 1)
    
    private lateinit var moviesResponse: List<Movie>
    private lateinit var moviesNextResponse: List<Movie>
    private lateinit var moviesNext2Response: List<Movie>
    
    private val errorResponseBody = ResponseBody.create(MediaType.parse("application/json"), "error")
    
    private val parser = JsonParser()
    
    @Before
    fun setUp() {
        
        mapper = MovieMapper(mockConfigurationRepository)
        
        moviesResponse = mapper.mapToDomain(movieResultsEntityItem.results)
        moviesNextResponse = mapper.mapToDomain(movieResultsEntityNextItem.results)
        moviesNext2Response = mapper.mapToDomain(movieResultsEntityNextItem.results)
        
        repository = MoviesApiRepository(mockApi, mapper, mockConfigurationRepository, emitter, emitterSimilar, language, apiKey)
    }
    
    @Test
    fun `get popular movies remote success`() {
        val response = Response.success(movieResultsEntityItem)
        
        // given
        Mockito.`when`(mockApi.popular(apiKey, language, 1)).thenReturn(Observable.just(response))
        
        // when
        val test = repository.get().test()
        
        // then
        Mockito.verify(mockApi).popular(apiKey, language, 1)
        test.assertValue(com.diego.movies.domain.model.Response(
                Page(moviesResponse, 1), true))
    }
    
    @Test
    fun `get popular movies remote error`() {
        val response = Response.error<MovieResultsEntity>(500, errorResponseBody)
        
        // given
        Mockito.`when`(mockApi.popular(apiKey, language, 1))
                .thenReturn(Observable.just(response))
        
        // when
        val test = repository.get().test()
        
        // then
        Mockito.verify(mockApi).popular(apiKey, language, 1)
        test.assertValue(com.diego.movies.domain.model.Response(
                Page(emptyList(), 1), false))
    }
    
    @Test
    fun `get popular movies remote next page success`() {
        val response = Response.success(movieResultsEntityItem)
        val nextPage = Response.success(movieResultsEntityNextItem)
        val nextPage2 = Response.success(movieResultsEntityNext2Item)
        val first = com.diego.movies.domain.model.Response(
                Page(moviesResponse, 1), true)
        val next = com.diego.movies.domain.model.Response(
                Page(moviesResponse.plus(moviesNextResponse), 2), true)
        val next2 = com.diego.movies.domain.model.Response(
                Page(moviesResponse.plus(moviesNextResponse).plus(moviesNext2Response), 3), true)
        
        // given
        Mockito.`when`(mockApi.popular(apiKey, language, 1)).thenReturn(Observable.just(response))
        Mockito.`when`(mockApi.popular(apiKey, language, 2)).thenReturn(Observable.just(nextPage))
        Mockito.`when`(mockApi.popular(apiKey, language, 3)).thenReturn(Observable.just(nextPage2))
        
        // when
        val test = repository.get().test()
        emitter.onNext(2)
        emitter.onNext(3)
        
        // then
        Mockito.verify(mockApi).popular(apiKey, language, 1)
        Mockito.verify(mockApi).popular(apiKey, language, 2)
        Mockito.verify(mockApi).popular(apiKey, language, 3)
        test.assertValues(first, next, next2)
    }
    
    @Test
    fun `get popular movies remote next page unsuccessful`() {
        val response = Response.success(movieResultsEntityItem)
        val nextPage = Response.success(movieResultsEntityNextItem)
        val responseError = Response.error<MovieResultsEntity>(500, errorResponseBody)
        val first = com.diego.movies.domain.model.Response(
                Page(moviesResponse, 1), true)
        val next = com.diego.movies.domain.model.Response(
                Page(moviesResponse.plus(moviesNextResponse), 2), true)
        val next2 = com.diego.movies.domain.model.Response(
                Page(moviesResponse.plus(moviesNextResponse), 2), false)
        
        // given
        Mockito.`when`(mockApi.popular(apiKey, language, 1)).thenReturn(Observable.just(response))
        Mockito.`when`(mockApi.popular(apiKey, language, 2)).thenReturn(Observable.just(nextPage))
        Mockito.`when`(mockApi.popular(apiKey, language, 3)).thenReturn(Observable.just(responseError))
        
        // when
        val test = repository.get().test()
        emitter.onNext(2)
        emitter.onNext(3)
        
        // then
        Mockito.verify(mockApi).popular(apiKey, language, 1)
        Mockito.verify(mockApi).popular(apiKey, language, 2)
        Mockito.verify(mockApi).popular(apiKey, language, 3)
        test.assertValues(first, next, next2)
    }
    
    
    @Test
    fun `get similar shows remote success`() {
        val response = Response.success(movieResultsEntityItem)
        
        // given
        Mockito.`when`(mockApi.similar(1, apiKey, language, 1)).thenReturn(Observable.just(response))
        
        // when
        val test = repository.getSimilar(1).test()
        
        // then
        Mockito.verify(mockApi).similar(1, apiKey, language, 1)
        test.assertValue(com.diego.movies.domain.model.Response(
                Page(moviesResponse, 1), true))
    }
    
    @Test
    fun `get similar shows remote error`() {
        val response = Response.error<MovieResultsEntity>(500, errorResponseBody)
        
        // given
        Mockito.`when`(mockApi.similar(1, apiKey, language, 1))
                .thenReturn(Observable.just(response))
        
        // when
        val test = repository.getSimilar(1).test()
        
        // then
        Mockito.verify(mockApi).similar(1, apiKey, language, 1)
        test.assertValue(com.diego.movies.domain.model.Response(
                Page(emptyList(), 1), false))
    }
    
    @Test
    fun `get similar shows remote next page success`() {
        val response = Response.success(movieResultsEntityItem)
        val nextPage = Response.success(movieResultsEntityNextItem)
        val nextPage2 = Response.success(movieResultsEntityNext2Item)
        val first = com.diego.movies.domain.model.Response(
                Page(moviesResponse, 1), true)
        val next = com.diego.movies.domain.model.Response(
                Page(moviesResponse.plus(moviesNextResponse), 2), true)
        val next2 = com.diego.movies.domain.model.Response(
                Page(moviesResponse.plus(moviesNextResponse).plus(moviesNext2Response), 3), true)
        
        // given
        Mockito.`when`(mockApi.similar(1, apiKey, language, 1)).thenReturn(Observable.just(response))
        Mockito.`when`(mockApi.similar(1, apiKey, language, 2)).thenReturn(Observable.just(nextPage))
        Mockito.`when`(mockApi.similar(1, apiKey, language, 3)).thenReturn(Observable.just(nextPage2))
        
        // when
        val test = repository.getSimilar(1).test()
        emitterSimilar.onNext(2)
        emitterSimilar.onNext(3)
        
        // then
        Mockito.verify(mockApi).similar(1, apiKey, language, 1)
        Mockito.verify(mockApi).similar(1, apiKey, language, 2)
        Mockito.verify(mockApi).similar(1, apiKey, language, 3)
        test.assertValues(first, next, next2)
    }
    
    @Test
    fun `get similar shows remote next page unsuccessful`() {
        val response = Response.success(movieResultsEntityItem)
        val nextPage = Response.success(movieResultsEntityNextItem)
        val responseError = Response.error<MovieResultsEntity>(500, errorResponseBody)
        val first = com.diego.movies.domain.model.Response(
                Page(moviesResponse, 1), true)
        val next = com.diego.movies.domain.model.Response(
                Page(moviesResponse.plus(moviesNextResponse), 2), true)
        val next2 = com.diego.movies.domain.model.Response(
                Page(moviesResponse.plus(moviesNextResponse), 2), false)
        
        // given
        Mockito.`when`(mockApi.similar(1, apiKey, language, 1)).thenReturn(Observable.just(response))
        Mockito.`when`(mockApi.similar(1, apiKey, language, 2)).thenReturn(Observable.just(nextPage))
        Mockito.`when`(mockApi.similar(1, apiKey, language, 3)).thenReturn(Observable.just(responseError))
        
        // when
        val test = repository.getSimilar(1).test()
        emitterSimilar.onNext(2)
        emitterSimilar.onNext(3)
        
        // then
        Mockito.verify(mockApi).similar(1, apiKey, language, 1)
        Mockito.verify(mockApi).similar(1, apiKey, language, 2)
        Mockito.verify(mockApi).similar(1, apiKey, language, 3)
        test.assertValues(first, next, next2)
    }
    
    @Test
    fun `update configuration success`() {
        val jsonFile = File("configuration_test.json")
        val path = jsonFile.absolutePath
        val jsonString = readFileContent(File(path))
        val response = okhttp3.ResponseBody.create(MediaType.parse("application/json"), jsonString)
        val jsonObject = parser.parse(jsonString).asJsonObject
        val images = jsonObject.getAsJsonObject("images")
        val imageBaseUrl = images.get("base_url").asString
        val imageSizeJsonArray = images.getAsJsonArray("poster_sizes")
        val imageSizeArray: MutableList<String> = mutableListOf()
        
        for (i in 0..(imageSizeJsonArray.size() - 1)) {
            imageSizeArray.add(imageSizeJsonArray[i].asString)
        }
        
        // given
        Mockito.`when`(mockApi.configuration(apiKey)).thenReturn(Single.just(response))
        
        // when
        val test = repository.updateConfiguration().test()
        
        // then
        Mockito.verify(mockConfigurationRepository).updateConfiguration(imageBaseUrl, imageSizeArray)
        test.assertValue(true)
    }
    
    @Test
    fun `update configuration error`() {
        
        // given
        Mockito.`when`(mockApi.configuration(apiKey)).thenReturn(Single.error(Throwable()))
        
        // when
        val test = repository.updateConfiguration().test()
        
        // then
        test.assertValue(false)
    }
}