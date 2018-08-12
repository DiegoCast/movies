package com.diego.movies.presentation.launcher

import com.diego.movies.domain.configuration.UpdateConfigurationUseCase
import com.diego.movies.presentation.Navigator
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class LauncherPresenterTest {
    
    private lateinit var presenter: LauncherPresenter
    
    private val view = mock<LauncherView> {}
    private val updateConfigurationUseCase = mock<UpdateConfigurationUseCase> {}
    private val mockNavigator = mock<Navigator> {}
    private val testScheduler = TestScheduler()
    
    @Before
    fun setUp() {
        presenter = LauncherPresenter(view, updateConfigurationUseCase, mockNavigator, testScheduler, testScheduler)
    }
    
    @Test
    fun start() {
        
        // given
        Mockito.`when`(updateConfigurationUseCase.update()).thenReturn(Single.just(true))
        
        // when
        presenter.start()
        testScheduler.triggerActions()
        
        // then
        Mockito.verify(updateConfigurationUseCase).update()
        Mockito.verify(view).close()
        Mockito.verify(mockNavigator).navigateToMovies()
    }
    
    @Test
    fun `start unsuccessful`() {
        
        // given
        Mockito.`when`(updateConfigurationUseCase.update()).thenReturn(Single.just(false))
        
        // when
        presenter.start()
        testScheduler.triggerActions()
        
        // then
        Mockito.verify(updateConfigurationUseCase).update()
        Mockito.verify(view).showError()
        Mockito.verify(view).close()
        Mockito.verify(mockNavigator).navigateToMovies()
    }
    
}