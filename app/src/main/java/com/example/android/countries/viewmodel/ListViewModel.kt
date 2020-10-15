package com.example.android.countries.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.countries.di.DaggerApiComponent
import com.example.android.countries.model.CountriesService
import com.example.android.countries.model.Country
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel : ViewModel() {

    @Inject
    lateinit var countriesService: CountriesService

    private val disposable = CompositeDisposable()

    val countries = MutableLiveData<List<Country>>()
    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun refresh() {
        fetchCountries()
    }

    private fun fetchCountries() {
        loading.value = true
        //create disposable
        disposable.add(
            countriesService.getCountries()
                .subscribeOn(Schedulers.newThread()) //processing on separate thread
                .observeOn(AndroidSchedulers.mainThread()) // get results on main thread
                .subscribeWith(object : DisposableSingleObserver<List<Country>>() {
                    override fun onSuccess(t: List<Country>) {
                        countries.value = t
                        countryLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        countryLoadError.value = true
                        loading.value = false
                    }

                })
        )

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}