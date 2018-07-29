package com.diego.movies.data.api

import com.diego.movies.domain.model.Movie
import com.diego.movies.domain.model.Page
import com.diego.movies.domain.model.Response
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesRestApi {

    @GET("tv/popular")
    fun popular(@Query("api_key") apiKey : String, @Query("language") language : String,
                @Query("page") page : Int) : Observable<Response<Page<Movie>, Boolean>>
}