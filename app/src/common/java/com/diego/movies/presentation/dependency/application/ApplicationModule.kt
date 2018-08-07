package com.diego.movies.presentation.dependency.application

import android.content.Context
import com.diego.movies.App
import com.diego.movies.data.repository.ConfigurationRepository
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule(val app: App) {
    
    @Provides
    @Singleton
    fun provideContext(): Context = app.baseContext
    
    @Provides
    @Singleton
    fun provideConfigurationRepository() = ConfigurationRepository()
    
    @Provides
    @Singleton
    @Named("language")
    fun provideLanguage() = Locale.getDefault().language
    
    @Provides
    @Singleton
    @Named("main")
    fun provideMainThread() = AndroidSchedulers.mainThread()
    
    @Provides
    @Singleton
    @Named("io")
    fun provideIoThread() = Schedulers.io()
}