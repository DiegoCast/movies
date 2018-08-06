package com.diego.movies.presentation.dependency.application

import com.diego.movies.App
import dagger.Module
import dagger.Provides
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
}