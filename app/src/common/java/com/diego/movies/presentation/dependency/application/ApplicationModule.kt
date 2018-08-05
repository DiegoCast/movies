package com.diego.movies.presentation.dependency.application

import com.diego.movies.App
import com.diego.movies.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule(val app: App) {
    
    @Provides
    @Singleton
    fun provideApp() = app
    
    @Provides
    @Singleton
    @Named("language")
    fun provideLanguage() = Locale.getDefault().language
}