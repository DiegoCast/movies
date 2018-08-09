package com.diego.movies.presentation.detail

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout

import android.support.v7.widget.LinearLayoutManager
import com.diego.movies.R
import com.diego.movies.presentation.movies.MoviesAdapter
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity(), DetailView, LifecycleOwner, SwipeRefreshLayout.OnRefreshListener {
    
    @Inject
    lateinit var presenter: DetailPresenter
    
    lateinit var adapter: MoviesAdapter
    lateinit var layoutManager: LinearLayoutManager
    
    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, DetailActivity::class.java)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        lifecycle.addObserver(presenter)
    }
    
    override fun onRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}