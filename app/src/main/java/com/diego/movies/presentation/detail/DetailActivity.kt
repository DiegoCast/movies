package com.diego.movies.presentation.detail

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout

import android.support.v7.widget.LinearLayoutManager
import com.diego.movies.R
import com.diego.movies.R.id.movieBackground
import com.diego.movies.R.id.movieDescription
import com.diego.movies.domain.model.Movie
import com.squareup.picasso.Callback
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation
import jp.wasabeef.picasso.transformations.ColorFilterTransformation
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : DaggerAppCompatActivity(), DetailView, LifecycleOwner, SwipeRefreshLayout.OnRefreshListener {
    @Inject
    lateinit var presenter: DetailPresenter
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
        toolbar.title = title
        ratingBar.rating = voteAverage / 2
        movieVotes.text = resources.getString(R.string.votes, voteCount.toString())
        movieDescription.text = description
        Picasso.get()
                .load(backgroundUrl)
                .fit()
                .centerCrop()
                .transform(ColorFilterTransformation(ContextCompat.getColor(this, R.color.backgroundFilter)))
                .into(movieBackground)
    }
}