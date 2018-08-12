package com.diego.movies.domain.movies

import com.diego.movies.domain.model.Movie
import com.diego.movies.domain.model.Page
import com.diego.movies.domain.model.Response
import io.reactivex.Observable
import io.reactivex.Single

interface MoviesRepository {
    
    fun get(): Observable<Response<Page<List<Movie>>, Boolean>>
    
    fun updateConfiguration(): Single<Boolean>
    
    fun next(page: Int)
    
    fun nextSimilar(id: Int, page: Int)
    
    fun getSimilar(id: Int): Observable<Response<Page<List<Movie>>, Boolean>>
}