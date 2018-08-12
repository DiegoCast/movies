package com.diego.movies.presentation

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.support.v4.view.ViewCompat
import android.widget.ImageView
import com.diego.movies.domain.model.Movie
import com.diego.movies.presentation.detail.DetailActivity
import com.diego.movies.presentation.movies.MoviesActivity
import javax.inject.Inject

class Navigator @Inject constructor(private val context: Context) {
    
    fun navigateToMovies() {
        val intent = MoviesActivity.newInstance(context)
        context.startActivity(intent)
    }
    
    fun navigateToDetail(activity: Activity, view: ImageView, movie: Movie) {
        val options = ActivityOptions
                .makeSceneTransitionAnimation(activity, view, ViewCompat.getTransitionName(view))
    
        val intent = DetailActivity.newInstance(context, movie)
        intent.putExtra(DetailActivity.sharedImageTag, movie.id.toString())
        activity.startActivity(intent, options.toBundle())
    }
}