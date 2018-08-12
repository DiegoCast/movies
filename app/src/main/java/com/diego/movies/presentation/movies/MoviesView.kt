package com.diego.movies.presentation.movies

import com.diego.movies.domain.model.Movie
import io.reactivex.Observable

interface MoviesView {
    fun show(movies: List<Movie>)
    fun getScrollObservable() : Observable<Int>
    fun showError()
}