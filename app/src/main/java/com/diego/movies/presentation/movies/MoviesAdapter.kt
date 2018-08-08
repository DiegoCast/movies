package com.diego.movies.presentation.movies

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.diego.movies.R
import com.diego.movies.domain.model.Movie
import kotlinx.android.synthetic.main.item_list_movie.view.*
import android.support.v7.util.DiffUtil
import com.squareup.picasso.Picasso

class MoviesAdapter(val context: Context) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    companion object {
        val itemMovie = 0
        val itemLoader = 1
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
            return LoaderViewHolder(parent)
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
            holder.bind(items[position])
        }
    }
    
    inner class ViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_movie, parent, false)) {
        
        fun bind(item: Movie) {
            itemView.movieText.text = item.title.capitalize()
            Picasso.get()
                    .load(item.imageUrl)
                    .error(R.drawable.placeholder)
                    .into(itemView.movieImage)
        }
    }
    
    inner class LoaderViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_loader, parent, false))
}