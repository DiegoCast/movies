package com.diego.movies.domain.movies

import com.diego.movies.domain.model.Movie
import com.diego.movies.domain.model.Page
import com.diego.movies.domain.model.Response
import io.reactivex.Observable

interface MoviesRepository {
    
    fun get() : Observable<Response<Page<List<Movie>>, Boolean>>
    
    fun next(page : Int)
}