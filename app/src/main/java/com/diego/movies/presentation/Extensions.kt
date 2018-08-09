package com.diego.movies.presentation

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import com.diego.movies.App
import com.diego.movies.presentation.dependency.ApplicationComponent

val Activity.app: App get() = application as App