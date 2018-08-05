package com.diego.movies.domain.movies

import com.diego.movies.domain.model.Movie
import com.diego.movies.domain.model.Page
import com.diego.movies.domain.model.Response
import io.reactivex.Observable
import javax.inject.Inject


class GetMoviesUseCase @Inject constructor(private val repository: MoviesRepository) {
    
    fun get(): Observable<Response<Page<List<Movie>>, Boolean>> = repository.get()
    
    fun next(page: Int) = repository.next(page)
}