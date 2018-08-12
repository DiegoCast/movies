package com.diego.movies.domain.movies

import com.diego.movies.domain.model.Movie
import com.diego.movies.domain.model.Page
import com.diego.movies.domain.model.Response
import io.reactivex.Observable
import javax.inject.Inject

class GetSimilarShowsUseCase @Inject constructor(private val repository: MoviesRepository) {
    
    fun get(id: Int): Observable<Response<Page<List<Movie>>, Boolean>> = Observable.empty()
    
    fun next(id: Int, page: Int) {}
}