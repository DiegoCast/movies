package com.diego.movies.presentation.movies

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.diego.movies.R
import com.diego.movies.domain.model.Movie
import kotlinx.android.synthetic.main.item_list_movie.view.*
import android.support.v7.util.DiffUtil
import android.widget.ImageView
import com.squareup.picasso.Picasso

class MoviesAdapter(val context: Context, val onCardClickListener: OnCardClickListener, val orientation: Int) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    companion object {
        val itemMovie = 0
        val itemLoader = 1
        val orientationHorizontal = 0
        val orientationVertical = 1
    }
    
    private val items = mutableListOf<Movie>()
    
    fun updateList(newList: List<Movie>) {
        val diffResult = DiffUtil.calculateDiff(MoviesDiffUtilCallback(items, newList))
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == itemLoader) {
            val loaderLayout: Int = if (orientation == orientationVertical) {
                R.layout.list_item_loader_vertical
            } else {
                R.layout.item_list_loader
            }
            return LoaderViewHolder(parent, loaderLayout)
        }
        return ViewHolder(parent)
    }
    
    override fun getItemCount(): Int {
        return items.size + 1
    }
    
    override fun getItemViewType(position: Int): Int {
        return if (position == items.size) {
            itemLoader
        } else {
            itemMovie
        }
    }
    
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position], position)
        }
    }
    
    interface OnCardClickListener {
        fun onCardClick(movie: Movie, view: ImageView)
    }
    
    inner class ViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_movie, parent, false)) {
        
        fun bind(item: Movie, position: Int) {
            itemView.moviePosition.text = (position + 1).toString()
            itemView.movieText.text = item.title.capitalize()
            itemView.ratingBar.rating = item.voteAverage / 2
            itemView.movieVotes.text = context.resources.getString(R.string.votes, item.voteCount.toString())
            itemView.movieImage.transitionName = item.id.toString()
            Picasso.get()
                    .load(item.imageUrl)
                    .error(R.drawable.placeholder)
                    .into(itemView.movieImage)
            val image = itemView.findViewById<ImageView>(R.id.movieImage)
            itemView.setOnClickListener { onCardClickListener.onCardClick(item, image) }
        }
    }
    
    inner class LoaderViewHolder(parent: ViewGroup, loaderLayout: Int) :
            RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(loaderLayout, parent, false))
}