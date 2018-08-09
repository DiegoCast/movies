package com.diego.movies

import com.diego.movies.presentation.dependency.DaggerApplicationComponent
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class App : DaggerApplication() {
    
    override fun applicationInjector(): AndroidInjector<out DaggerApplication>  = DaggerApplicationComponent.builder().application(this).build()
    
    override fun onCreate() {
        super.onCreate()
        
        var picassoBuilder = Picasso.Builder(this)
        if (BuildConfig.DEBUG) {
            picassoBuilder.indicatorsEnabled(true)
        }
        Picasso.setSingletonInstance(picassoBuilder.build())
    }
}