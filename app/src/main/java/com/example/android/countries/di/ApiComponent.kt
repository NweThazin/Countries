package com.example.android.countries.di

import com.example.android.countries.model.CountriesService
import com.example.android.countries.viewmodel.ListViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(service: CountriesService)
    fun inject(viewModel: ListViewModel)
}