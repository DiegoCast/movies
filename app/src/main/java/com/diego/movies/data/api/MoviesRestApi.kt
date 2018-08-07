package com.diego.movies.data.api

import com.diego.movies.data.model.MovieResultsEntity
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesRestApi {
    
    //For some reason moshi is not converting this call properly so I had to resort to the raw response:
    @GET("configuration")
    fun configuration(@Query("api_key") apiKey: String) : Single<okhttp3.ResponseBody>
    
    @GET("tv/popular")
    fun popular(@Query("api_key") apiKey: String, @Query("language") language: String,
                @Query("page") page: Int): Observable<Response<MovieResultsEntity>>
}