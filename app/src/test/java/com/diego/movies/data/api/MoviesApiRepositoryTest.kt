package com.diego.movies.data.api

import com.diego.movies.data.model.MovieMapper
import com.diego.movies.data.model.MovieResultsEntity
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

class MoviesApiRepositoryTest {
    
    private val language = "en"
    private val apiKey = "1234"
    
    private lateinit var repository: MoviesApiRepository
    
    private val mockApi = mock<MoviesRestApi> {}
    private val mapper = MovieMapper()
    private val emitter: PublishSubject<Int> = PublishSubject.create()
    
    private val movieResultsEntityItem = createMovieResultsEntity(0, 2)
    private val movieResultsEntityNextItem = createMovieResultsEntity(1, 1)
    private val movieResultsEntityNext2Item = createMovieResultsEntity(2, 1)
    
    private var moviesResponse = mapper.mapToDomain(movieResultsEntityItem.results)
    private var moviesNextResponse = mapper.mapToDomain(movieResultsEntityNextItem.results)
    private var moviesNext2Response = mapper.mapToDomain(movieResultsEntityNextItem.results)
    
    private val errorResponseBody = ResponseBody.create(MediaType.parse("application/json"), "error")
    
    @Before
    fun setUp() {
        repository = MoviesApiRepository(mockApi, mapper, emitter, language, apiKey)
    }
    
    @Test
    fun `get popular movies remote success`() {
        val response = Response.success(movieResultsEntityItem)
        
        // given
        Mockito.`when`(mockApi.popular(apiKey, language, 0)).thenReturn(Observable.just(response))
        
        // when
        val test = repository.get().test()
        
        // then
        Mockito.verify(mockApi).popular(apiKey, language, 0)
        test.assertValue(com.diego.movies.domain.model.Response(
                Page(moviesResponse, 0), true))
    }
    
    @Test
    fun `get popular movies remote error`() {
        val response = Response.error<MovieResultsEntity>(500, errorResponseBody)
        
        // given
        Mockito.`when`(mockApi.popular(apiKey, language, 0))
                .thenReturn(Observable.just(response))
        
        // when
        val test = repository.get().test()
        
        // then
        Mockito.verify(mockApi).popular(apiKey, language, 0)
        test.assertValue(com.diego.movies.domain.model.Response(
                Page(emptyList(), 0), false))
    }
    
    @Test
    fun `get popular movies remote next page success`() {
        val response = Response.success(movieResultsEntityItem)
        val nextPage = Response.success(movieResultsEntityNextItem)
        val nextPage2 = Response.success(movieResultsEntityNext2Item)
        val first = com.diego.movies.domain.model.Response(
                Page(mapper.mapToDomain(movieResultsEntityItem.results), 0), true)
        val next = com.diego.movies.domain.model.Response(
                Page(moviesResponse.plus(moviesNextResponse), 1), true)
        val next2 = com.diego.movies.domain.model.Response(
                Page(moviesResponse.plus(moviesNextResponse).plus(moviesNext2Response), 2), true)
        
        // given
        Mockito.`when`(mockApi.popular(apiKey, language, 0)).thenReturn(Observable.just(response))
        Mockito.`when`(mockApi.popular(apiKey, language, 1)).thenReturn(Observable.just(nextPage))
        Mockito.`when`(mockApi.popular(apiKey, language, 2)).thenReturn(Observable.just(nextPage2))
        
        // when
        val test = repository.get().test()
        emitter.onNext(1)
        emitter.onNext(2)
        
        // then
        Mockito.verify(mockApi).popular(apiKey, language, 0)
        Mockito.verify(mockApi).popular(apiKey, language, 1)
        Mockito.verify(mockApi).popular(apiKey, language, 2)
        test.assertValues(first, next, next2)
    }
    
    @Test
    fun `get popular movies remote next page unsuccessful`() {
        val response = Response.success(movieResultsEntityItem)
        val nextPage = Response.success(movieResultsEntityNextItem)
        val responseError = Response.error<MovieResultsEntity>(500, errorResponseBody)
        val first = com.diego.movies.domain.model.Response(
                Page(mapper.mapToDomain(movieResultsEntityItem.results), 0), true)
        val next = com.diego.movies.domain.model.Response(
                Page(moviesResponse.plus(moviesNextResponse), 1), true)
        val next2 = com.diego.movies.domain.model.Response(
                Page(moviesResponse.plus(moviesNextResponse), 1), false)
        
        // given
        Mockito.`when`(mockApi.popular(apiKey, language, 0)).thenReturn(Observable.just(response))
        Mockito.`when`(mockApi.popular(apiKey, language, 1)).thenReturn(Observable.just(nextPage))
        Mockito.`when`(mockApi.popular(apiKey, language, 2)).thenReturn(Observable.just(responseError))
        
        // when
        val test = repository.get().test()
        emitter.onNext(1)
        emitter.onNext(2)
        
        // then
        Mockito.verify(mockApi).popular(apiKey, language, 0)
        Mockito.verify(mockApi).popular(apiKey, language, 1)
        Mockito.verify(mockApi).popular(apiKey, language, 2)
        test.assertValues(first, next, next2)
    }
}