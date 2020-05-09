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

    single<TokenRepository> {
        TokenRepositoryImpl()
    }


    //Services
    single { GwfApiService(get(), get()) }
}