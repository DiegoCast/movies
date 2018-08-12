package com.diego.movies.presentation.detail

import com.diego.movies.domain.model.Movie

interface DetailView {
    fun showPoster(url: String?)
    fun show(title: String, voteAverage: Float, voteCount: Long, description: String, backgroundUrl: String?)
    fun showError()
    fun showSimilar(data: List<Movie>)
}