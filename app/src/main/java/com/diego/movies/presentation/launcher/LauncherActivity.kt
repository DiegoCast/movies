package com.diego.movies.presentation.launcher

import android.os.Bundle
import android.widget.Toast
import com.diego.movies.R
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class LauncherActivity : DaggerAppCompatActivity(), LauncherView {
    
    @Inject
    lateinit var presenter: LauncherPresenter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        lifecycle.addObserver(presenter)
    }
    
    override fun showError() {
        Toast.makeText(this, R.string.configuration_error, Toast.LENGTH_LONG).show()
    }
    
    override fun close() {
        finish()
    }
}