package com.diego.movies.presentation.dependency.application

import com.diego.movies.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(val app: App) {
    @Provides
    @Singleton
    fun provideApp() = app
}