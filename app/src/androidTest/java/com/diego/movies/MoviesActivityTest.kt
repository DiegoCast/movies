package com.diego.movies

import RecyclerViewItemCountAssertion.Companion.withItemCount
import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.diego.movies.domain.model.Movie
import com.diego.movies.presentation.movies.MoviesActivity
import com.diego.movies.presentation.movies.MoviesPresenter
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.DispatchingAndroidInjector_Factory
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Provider
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class MoviesActivityTest {
    
    val mockPresenter =  mock(MoviesPresenter::class.java)
    
    @get:Rule
    var activityRule: ActivityTestRule<MoviesActivity> = object : ActivityTestRule<MoviesActivity>(MoviesActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            val app = InstrumentationRegistry.getTargetContext().applicationContext as App
            var activityInjector = app.activityInjector()
            activityInjector = createFakeMainActivityInjector {
                presenter = mockPresenter
            }
        }
    }
    
    lateinit var activity: MoviesActivity
    
    @Before
    @Throws(Exception::class)
    fun setUp() {
        this.activity = activityRule.activity
    }
    
    @Test
    fun create() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withText(R.string.popular_shows)).check(matches(withParent(withId(R.id.toolbar))))
        onView(withId(R.id.itemLoader)).check(matches(isDisplayed()))
    }
    
    @Test
    fun show() {
        val movies = createMovieList(20)
        
        // when
        activityRule.runOnUiThread { activity.show(movies) }
        
        // then
        onView(withId(R.id.moviesRecyclerView)).check(withItemCount(21))
    }
    
    fun createMovieList(size: Int) : List<Movie> {
        val list = mutableListOf<Movie>()
        for (i in 0 until size) list.add(i, createMovie(i))
        return list
    }
    
    fun createMovie(id: Int) : Movie {
        return Movie(id, "name", "posterPath", 6.0f, 22)
    }
    
    fun createFakeMainActivityInjector(block : MoviesActivity.() -> Unit)
            : DispatchingAndroidInjector<Activity> {
        val injector = AndroidInjector<Activity> { instance ->
            if (instance is MoviesActivity) {
                instance.block()
            }
        }
        val factory = AndroidInjector.Factory<Activity> { injector }
        val map = mapOf(Pair<Class <out Activity>,
                Provider<AndroidInjector.Factory<out Activity>>>(MoviesActivity::class.java, Provider { factory }))
        return DispatchingAndroidInjector_Factory.newDispatchingAndroidInjector(map)
    }
}