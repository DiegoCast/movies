package com.diego.movies.presentation

import android.content.Context
import com.diego.movies.presentation.movies.MoviesActivity
import javax.inject.Inject

class Navigator @Inject constructor(private val context: Context) {
    
    fun navigateToMovies() {
        val intent = MoviesActivity.newInstance(context)
        context.startActivity(intent)
    }
}