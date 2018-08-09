package com.diego.movies.data.model

import com.diego.movies.data.repository.ConfigurationRepository
import com.diego.movies.domain.model.Movie
import com.squareup.moshi.Json
import javax.inject.Inject

data class MovieEntity(val id: Int,
                       val name: String,
                       @Json(name = "original_name") val originalName: String,
                       @Json(name = "original_language") val originalLanguage: String,
                       val popularity: Double,
                       @Json(name = "vote_average") val voteAverage: Float,
                       @Json(name = "vote_count") val voteCount: Long,
                       val overview: String,
                       @Json(name = "poster_path") val posterPath: String)

data class MovieResultsEntity(@Json(name = "page") val page: Int,
                              @Json(name = "results") val results: List<MovieEntity>)


class MovieMapper @Inject constructor(private val repository: ConfigurationRepository) {
    
    fun mapToDomain(entity: MovieEntity): Movie = Movie(entity.id,
            entity.name,
            repository.imageBaseUrl + repository.imageSize + entity.posterPath,
            entity.voteAverage,
            entity.voteCount)
    
    fun mapToDomain(list: List<MovieEntity>): List<Movie> = list.map { mapToDomain(it) }
}