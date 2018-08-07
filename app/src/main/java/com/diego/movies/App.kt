package com.diego.movies

import android.app.Application
import com.diego.movies.presentation.dependency.ApplicationComponent
import com.diego.movies.presentation.dependency.DaggerApplicationComponent
import com.diego.movies.presentation.dependency.application.ApplicationModule
import com.squareup.picasso.Picasso

class App : Application () {
    
    lateinit var component: ApplicationComponent private set
    
    override fun onCreate() {
        super.onCreate()
        
        var picassoBuilder = Picasso.Builder(this)
        if (BuildConfig.DEBUG) {
            picassoBuilder.indicatorsEnabled(true)
        }
        Picasso.setSingletonInstance(picassoBuilder.build())
        
        component = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }
}