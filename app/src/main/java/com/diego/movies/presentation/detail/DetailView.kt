package com.diego.movies.presentation.detail

interface DetailView {
    fun showPoster(url: String?)
    fun show(title: String, voteAverage: Float, voteCount: Long, description: String, backgroundUrl: String?)
}