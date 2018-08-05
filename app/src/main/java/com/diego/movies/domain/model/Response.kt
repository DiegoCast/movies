package com.diego.movies.domain.model

data class Response <T, S> (val result : T, val succesful : S)