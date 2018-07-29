package com.diego.movies.presentation.dependency

import com.diego.movies.App
import com.diego.movies.presentation.dependency.application.ApplicationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(app: App)
}