package com.diego.movies.presentation.launcher

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.diego.movies.R
import com.diego.movies.presentation.dependency.launcher.LauncherModule
import com.diego.movies.presentation.getApplicationComponent
import javax.inject.Inject

class LauncherActivity : AppCompatActivity(), LauncherView {
    
    @Inject
    lateinit var presenter: LauncherPresenter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        getApplicationComponent()
                .plusLauncherComponent(LauncherModule(this))
                .inject(this)
        lifecycle.addObserver(presenter)
    }
    
    override fun showError() {
        Toast.makeText(this, R.string.configuration_error, Toast.LENGTH_LONG).show()
    }
    
    override fun close() {
        finish()
    }
}