package com.diego.movies.presentation.movies

import com.diego.movies.domain.model.Movie

interface MoviesView {
    fun show(movies: List<Movie>)
    fun showError()
}