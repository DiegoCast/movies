package com.diego.movies

import android.app.Application
import com.diego.movies.presentation.dependency.ApplicationComponent
import com.diego.movies.presentation.dependency.DaggerApplicationComponent
import com.squareup.picasso.Picasso

class App : Application () {
    
    companion object {
        val component: ApplicationComponent by lazy {
            DaggerApplicationComponent
                    .builder()
                    .build()
        }
        
        fun getAppComponent() = component
    }
    
    
    override fun onCreate() {
        super.onCreate()
        
        var picassoBuilder = Picasso.Builder(this)
        if (BuildConfig.DEBUG) {
            picassoBuilder.indicatorsEnabled(true)
        }
        Picasso.setSingletonInstance(picassoBuilder.build())
    }
}