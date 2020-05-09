package com.alexchar_dev.samplecollector.presentation.di

import com.alexchar_dev.samplecollector.data.network.GwfApiService
import com.alexchar_dev.samplecollector.presentation.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val navigationModule = module {
    viewModel { MainViewModel(get()) }
}