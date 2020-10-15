package com.example.android.countries.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.countries.model.CountriesService
import com.example.android.countries.model.Country
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var countriesService: CountriesService

    @InjectMocks
    var listViewModel = ListViewModel()

    private var testSingle: Single<List<Country>>? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getCountriesSuccess() {
        val country = Country("countryName", "capital", "url") //mock data
        val countriesList = arrayListOf(country)

        testSingle = Single.just(countriesList)

        Mockito.`when`(countriesService.getCountries()).thenReturn(testSingle)

        listViewModel.refresh() //call actual functions

        Assert.assertEquals(1, listViewModel.countries.value?.size) //expected 1
        Assert.assertEquals(false, listViewModel.countryLoadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getCountriesFail() {
        testSingle = Single.error(Throwable()) //onError case

        Mockito.`when`(countriesService.getCountries()).thenReturn(testSingle)

        listViewModel.refresh()

        Assert.assertEquals(true, listViewModel.countryLoadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Before
    fun setUpRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
            }

        }
        RxJavaPlugins.setInitIoSchedulerHandler { schedular -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { schedular -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { schedular -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { schedular -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { t: Callable<Scheduler> -> immediate }
    }
}