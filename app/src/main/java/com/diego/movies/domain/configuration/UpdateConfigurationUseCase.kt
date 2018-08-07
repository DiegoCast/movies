package com.diego.movies.domain.configuration

import com.diego.movies.domain.movies.MoviesRepository
import io.reactivex.Single
import javax.inject.Inject

class UpdateConfigurationUseCase @Inject constructor(private val repository: MoviesRepository) {
    
    fun update(): Single<Boolean> = repository.updateConfiguration()
}