package com.diego.movies.presentation.movies

import android.support.v7.util.DiffUtil
import com.diego.movies.domain.model.Movie

class MoviesDiffUtilCallback(val newList: List<Movie>, val oldList: List<Movie>): DiffUtil.Callback() {
    
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition].id == newList[newItemPosition].id
    
    override fun getOldListSize(): Int = oldList.size
    
    override fun getNewListSize(): Int = newList.size
    
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition].title.contentEquals(newList[newItemPosition].title)
}