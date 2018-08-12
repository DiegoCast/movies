package com.diego.movies.data

import com.diego.movies.BuildConfig
import com.diego.movies.data.api.MoviesRestApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {
    
    @Provides
    @Singleton
    @Named("api_key")
    fun provideApiKey() = BuildConfig.APIKEY
    
    @Provides
    @Singleton
    fun provideOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(getLoggingInterceptor())
                .build()
    }
    
    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.APIURL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
                .client(okHttpClient)
                .build()
    }
    
    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        }
        
        return interceptor
    }
    
    @Provides
    @Singleton
    @Named("movies_page_emitter")
    fun provideMoviesPageEmitter() : PublishSubject<Int> {
        return PublishSubject.create()
    }
    
    @Provides
    fun provideMoviesApi(retrofit: Retrofit): MoviesRestApi = retrofit.create(MoviesRestApi::class.java)
}
