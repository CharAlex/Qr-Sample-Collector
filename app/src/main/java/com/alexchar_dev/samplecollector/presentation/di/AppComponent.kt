package com.alexchar_dev.samplecollector.presentation.di

import org.koin.core.module.Module

val appComponent: List<Module> =
    listOf(
        networkModule,
        navigationModule
    )