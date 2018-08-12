package com.diego.movies.presentation.detail

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import com.diego.movies.R
import com.diego.movies.domain.model.Movie
import com.diego.movies.presentation.movies.MoviesAdapter
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.squareup.picasso.Callback
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import jp.wasabeef.picasso.transformations.ColorFilterTransformation
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : DaggerAppCompatActivity(), DetailView, LifecycleOwner, SwipeRefreshLayout.OnRefreshListener,
        MoviesAdapter.OnCardClickListener {
   
    @Inject
    lateinit var presenter: DetailPresenter
    
    lateinit var adapter: MoviesAdapter
    lateinit var layoutManager: LinearLayoutManager
    
    companion object {
        const val movieTag = "movieTag"
        const val sharedImageTag = "sharedImageTag"
        
        fun newInstance(context: Context, movie: Movie): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(movieTag, movie)
            return intent
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        lifecycle.addObserver(presenter)
        postponeEnterTransition()
        movieImage.transitionName = intent.getStringExtra(sharedImageTag)
        
        adapter = MoviesAdapter(this, this, MoviesAdapter.orientationVertical)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    
        similarRecyclerView.isNestedScrollingEnabled = false
        similarRecyclerView.layoutManager = layoutManager
        similarRecyclerView.adapter = adapter
    }
    
    override fun onRefresh() {
    
    }
    
    override fun showPoster(url: String?) {
        Picasso.get()
                .load(url)
                .noFade()
                .into(movieImage, object : Callback {
                    override fun onSuccess() {
                        startPostponedEnterTransition()
                    }
                    
                    override fun onError(e: Exception) {
                        startPostponedEnterTransition()
                    }
                })
    }
    
    override fun show(title: String, voteAverage: Float, voteCount: Long, description: String, backgroundUrl: String?) {
        toolbarDetail.title = title
        ratingBar.rating = voteAverage / 2
        movieVotes.text = resources.getString(R.string.votes, voteCount.toString())
        if (!description.isEmpty()) {
            movieDescription.text = description
        } else {
            movieDescription.text = resources.getString(R.string.label_no_description)
        }
        Picasso.get()
                .load(backgroundUrl)
                .fit()
                .centerCrop()
                .transform(ColorFilterTransformation(ContextCompat.getColor(this, R.color.backgroundFilter)))
                .into(movieBackground)
    }
    
    override fun showSimilar(similar: List<Movie>) {
        val recyclerViewState = layoutManager.onSaveInstanceState()
        adapter.updateList(similar)
        layoutManager.onRestoreInstanceState(recyclerViewState)
    }
    
    override fun getScrollObservable(): Observable<Int> {
        return RxRecyclerView.scrollEvents(similarRecyclerView).flatMap {
            Observable.just(layoutManager.findLastVisibleItemPosition())
        }
    }
    
    override fun showError() {
    }
    
    override fun onCardClick(movie: Movie, view: ImageView) {
        presenter.detail(this, view, movie)
    }
}