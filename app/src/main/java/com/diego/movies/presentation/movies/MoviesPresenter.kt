package com.diego.movies.presentation.movies

import com.diego.movies.domain.movies.GetMoviesUseCase
import javax.inject.Inject

class MoviesPresenter @Inject constructor(private val view: MoviesView,
                                          private val getMoviesUseCase: GetMoviesUseCase) {
    
    fun next(page: Int) {
    
    }
}