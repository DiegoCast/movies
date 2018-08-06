package com.diego.movies.presentation.dependency.application

import com.diego.movies.App
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
    fun provideApp() = app
    
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