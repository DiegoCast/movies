import com.diego.movies.data.model.MovieEntity
import com.diego.movies.data.model.MovieResultsEntity
import com.diego.movies.domain.model.Movie

fun createMovieResultsEntity(page: Int, size: Int) : MovieResultsEntity {
    return MovieResultsEntity(page, createMovieEntityList(size))
}

fun createMovieEntityList(size: Int) : List<MovieEntity> {
    val list = mutableListOf<MovieEntity>()
    for (i in 0 until size) list.add(i, createMovieEntity(i))
    return list
}

fun createMovieEntity(id: Int) : MovieEntity {
    return MovieEntity(id, "name", "originalName",
            "originalLanguage", 5, 6,
            "overview", "posterPath")
}

fun createMovieList(size: Int) : List<Movie> {
    val list = mutableListOf<Movie>()
    for (i in 0 until size) list.add(i, createMovie(i))
    return list
}

fun createMovie(id: Int) : Movie {
    return Movie(id, "title", "imageUrl", 4)
}