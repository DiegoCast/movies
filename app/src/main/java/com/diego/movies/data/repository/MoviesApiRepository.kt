package com.diego.movies.data.repository

import com.diego.movies.data.api.MoviesRestApi
import com.diego.movies.data.model.MovieMapper
import com.diego.movies.data.model.MovieResultsEntity
import com.diego.movies.domain.model.Movie
import com.diego.movies.domain.model.Page
import com.diego.movies.domain.model.Response
import com.diego.movies.domain.movies.MoviesRepository
import com.google.gson.JsonParser
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Singleton
class MoviesApiRepository @Inject constructor(private val api: MoviesRestApi,
                                              private val mapper: MovieMapper,
                                              private val configurationRepository: ConfigurationRepository,
                                              @Named("movies_page_emitter") private val emitter: PublishSubject<Int>,
                                              @Named("similar_page_emitter") private val emitterSimilar: PublishSubject<Int>,
                                              @Named("language") private val language: String,
                                              @Named("api_key") private val apiKey: String) : MoviesRepository {
    
    private val parser = JsonParser()
    
    override fun updateConfiguration(): Single<Boolean> {
        return api.configuration(apiKey).flatMap { response ->
            
            val obj = parser.parse(response.string()).asJsonObject
            val images = obj.getAsJsonObject("images")
            val imageBaseUrl = images.get("base_url").asString
            val imageSizeJsonArray = images.getAsJsonArray("poster_sizes")
            
            val imageSizeArray: MutableList<String> = mutableListOf()
            for (i in 0..(imageSizeJsonArray.size() - 1)) {
                imageSizeArray.add(imageSizeJsonArray[i].asString)
            }
            
            configurationRepository.updateConfiguration(imageBaseUrl, imageSizeArray)
            return@flatMap Single.just(true)
            
        }.onErrorReturn { false }
    }
    
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
    
    override fun nextSimilar(id: Int, page: Int) {
        emitterSimilar.onNext(page)
    }
    
    override fun getSimilar(id: Int): Observable<Response<Page<List<Movie>>, Boolean>> {
        return Observable
                .merge(seedSimilar(id), pageEmitterSimilar(id))
                .scan { total, new -> combine(total, new) }
    }
    
    private fun seed(): Observable<Response<Page<List<Movie>>, Boolean>> {
        return api.popular(apiKey, language, 1).map(Function {
            if (it.isSuccessful) {
                return@Function Response(Page(mapper.mapToDomain(it.body()!!.results),
                        1), true)
            } else {
                return@Function Response(Page(emptyList<Movie>(), 1), false)
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
    
    private fun seedSimilar(id: Int): Observable<Response<Page<List<Movie>>, Boolean>> {
        return api.similar(id, apiKey, language, 1).map(Function {
            if (it.isSuccessful) {
                return@Function Response(Page(mapper.mapToDomain(it.body()!!.results),
                        1), true)
            } else {
                return@Function Response(Page(emptyList<Movie>(), 1), false)
            }
        })
    }
    
    private fun pageEmitterSimilar(id: Int): Observable<Response<Page<List<Movie>>, Boolean>> {
        return emitterSimilar.flatMap({ api.similar(id, apiKey, language, it) }, { page: Int, response: retrofit2.Response<MovieResultsEntity> ->
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