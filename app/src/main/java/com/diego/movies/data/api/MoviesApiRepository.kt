package com.diego.movies.data.api

import com.diego.movies.data.model.MovieMapper
import com.diego.movies.data.model.MovieResultsEntity
import com.diego.movies.domain.model.Movie
import com.diego.movies.domain.model.Page
import com.diego.movies.domain.model.Response
import com.diego.movies.domain.movies.MoviesRepository
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class MoviesApiRepository @Inject constructor(private val api: MoviesRestApi,
                                              private val mapper: MovieMapper,
                                              @Named("movies_page_emitter") private val emitter: PublishSubject<Int>,
                                              @Named("language") private val language: String,
                                              @Named("api_key") private val apiKey: String) : MoviesRepository {
    
    override fun get(): Observable<Response<Page<List<Movie>>, Boolean>> {
        return Observable
                .merge(seed(), pageEmitter())
                .scan { total, new -> combine(total, new) }
    }
    
    override fun next(page: Int) {
        emitter.onNext(page)
    }
    
    private fun combine(total: Response<Page<List<Movie>>, Boolean>,
                        next: Response<Page<List<Movie>>, Boolean>): Response<Page<List<Movie>>, Boolean> {
        val page = if (next.succesful) next.result.page else total.result.page
        return Response(Page(total.result.data.plus(next.result.data), page), next.succesful)
    }
    
    private fun seed(): Observable<Response<Page<List<Movie>>, Boolean>> {
        return api.popular(apiKey, language, 0).map(Function {
            if (it.isSuccessful) {
                return@Function Response(Page(mapper.mapToDomain(it.body()!!.results),
                        0), true)
            } else {
                return@Function Response(Page(emptyList<Movie>(), 0), false)
            }
        })
    }
    
    private fun pageEmitter(): Observable<Response<Page<List<Movie>>, Boolean>> {
        return emitter.flatMap({ api.popular(apiKey, language, it) }, { page: Int, response: retrofit2.Response<MovieResultsEntity> ->
                    Pair(page, response)
                }).map(Function {
            if (it.second.isSuccessful) {
                return@Function Response(Page(mapper.mapToDomain(it.second.body()!!.results),
                        it.first), true)
            } else {
                return@Function Response(Page(emptyList<Movie>(), it.first), false)
            }
        })
    }
}