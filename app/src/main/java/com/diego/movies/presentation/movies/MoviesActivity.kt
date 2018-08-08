package com.diego.movies.presentation.movies

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.diego.movies.R
import com.diego.movies.domain.model.Movie
import com.diego.movies.presentation.dependency.movies.MoviesModule
import com.diego.movies.presentation.getApplicationComponent
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_movies.*
import javax.inject.Inject



class MoviesActivity : AppCompatActivity (), MoviesView, LifecycleOwner {
    @Inject lateinit var presenter: MoviesPresenter
    
    lateinit var adapter: MoviesAdapter
    lateinit var layoutManager: GridLayoutManager
    
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
    
        layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    MoviesAdapter.itemLoader -> 2
                    else -> 1
                }
            }
        }
        moviesRecyclerView.layoutManager = layoutManager
        moviesRecyclerView.adapter = adapter
    }
    
    override fun show(movies: List<Movie>) {
        val recyclerViewState = layoutManager.onSaveInstanceState()
        adapter.updateList(movies)
        layoutManager.onRestoreInstanceState(recyclerViewState)
    }
    
    override fun getScrollObservable(): Observable<Int> {
        return RxRecyclerView.scrollEvents(moviesRecyclerView).flatMap {
            Observable.just(layoutManager.findLastVisibleItemPosition())
        }
    }
    
    override fun showError() {
        //show retry button and error
    }
}
