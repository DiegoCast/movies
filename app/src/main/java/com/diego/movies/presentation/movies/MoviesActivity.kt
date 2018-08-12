package com.diego.movies.presentation.movies

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.ImageView
import com.diego.movies.R
import com.diego.movies.domain.model.Movie
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_movies.*
import javax.inject.Inject

class MoviesActivity : DaggerAppCompatActivity(), MoviesView, LifecycleOwner,
        SwipeRefreshLayout.OnRefreshListener, MoviesAdapter.OnCardClickListener {
    
    @Inject lateinit var presenter: MoviesPresenter
    
    lateinit var adapter: MoviesAdapter
    lateinit var layoutManager: GridLayoutManager
    
    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, MoviesActivity::class.java)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
    
        adapter = MoviesAdapter(this, this)
    
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
        
        swipeContainer.setOnRefreshListener(this)
        lifecycle.addObserver(presenter)
    }
    
    override fun show(movies: List<Movie>) {
        swipeContainer.isRefreshing = false
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
    
    override fun onRefresh() {
        presenter.retry()
    }
    
    override fun onCardClick(movie: Movie, view: ImageView) {
        presenter.detail(this, view, movie)
    }
}
