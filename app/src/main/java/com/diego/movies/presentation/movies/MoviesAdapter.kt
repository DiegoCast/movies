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
        RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    
    private val items = mutableListOf<Movie>()
    
    fun updateList(newList: List<Movie>) {
        val diffResult = DiffUtil.calculateDiff(MoviesDiffUtilCallback(items, newList))
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.ViewHolder {
        return ViewHolder(parent)
    }
    
    override fun getItemCount(): Int {
        return items.size
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
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
}