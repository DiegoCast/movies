package com.diego.movies.presentation.movies

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.diego.movies.App
import com.diego.movies.R
import com.diego.movies.domain.model.Movie
import com.diego.movies.presentation.dependency.movies.MoviesModule
import com.diego.movies.presentation.getApplicationComponent
import kotlinx.android.synthetic.main.activity_movies.*
import javax.inject.Inject

class MoviesActivity : AppCompatActivity (), MoviesView, LifecycleOwner {
    
    @Inject lateinit var presenter: MoviesPresenter
    
    lateinit var adapter: MoviesAdapter
    
    companion object {
        fun newMeInstance(context: Context): Intent {
            return Intent(context, MoviesActivity::class.java)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        getApplicationComponent()
                .plusMoviesComponent(MoviesModule(this))
                .inject(this)
        lifecycle.addObserver(presenter)
    
        adapter = MoviesAdapter(this)
        
        moviesRecyclerView.layoutManager = GridLayoutManager(this, 2)
        moviesRecyclerView.adapter = adapter
        
    }
    
    override fun show(movies: List<Movie>) {
        adapter.updateList(movies)
    }
    
    override fun showError() {
        //show retry button and error
    }
}
