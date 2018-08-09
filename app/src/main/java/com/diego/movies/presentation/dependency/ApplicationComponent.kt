package com.diego.movies.presentation.dependency

import android.content.Context
import com.diego.movies.App
import com.diego.movies.data.NetworkModule
import com.diego.movies.data.RepositoryModule
import com.diego.movies.presentation.dependency.application.ApplicationModule
import dagger.Component
import javax.inject.Singleton
import dagger.BindsInstance
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication

@Singleton
@Component(modules = arrayOf(AndroidSupportInjectionModule::class, ApplicationModule::class,
        NetworkModule::class, RepositoryModule::class, ActivityBuilder::class))
interface ApplicationComponent : AndroidInjector<DaggerApplication> {
    
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Context): Builder
        fun build(): ApplicationComponent
    }
}