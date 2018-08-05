package com.diego.movies.data.api

import com.diego.movies.data.model.MovieResultsEntity
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesRestApi {
    
    @GET("tv/popular")
    fun popular(@Query("api_key") apiKey: String, @Query("language") language: String,
                @Query("page") page: Int): Observable<Response<MovieResultsEntity>>
}