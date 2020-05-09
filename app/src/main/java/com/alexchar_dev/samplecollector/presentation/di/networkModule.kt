package com.alexchar_dev.samplecollector.presentation.di

import com.alexchar_dev.samplecollector.data.network.*
import org.koin.dsl.module

val networkModule = module {
    single<ConnectivityInterceptor> {
        ConnectivityInterceptorImpl(
            get()
        )
    }

    single<AccessTokenAuthenticator> {
        AccessTokenAuthenticatorImpl(get())
    }

    single<TokenProvider> {
        TokenProviderImpl(get())
    }

    single { GwfApiService(get(), get()) }

    single { GwfAuthService(get()) }
}