package com.alexchar_dev.samplecollector.data.network

import com.alexchar_dev.samplecollector.Constants
import com.alexchar_dev.samplecollector.Constants.BASE_URL
import com.alexchar_dev.samplecollector.Constants.LOGIN_URL
import com.alexchar_dev.samplecollector.Constants.TOKEN_REFRESH_URL
import com.alexchar_dev.samplecollector.data.models.LoginRequest
import com.alexchar_dev.samplecollector.data.network.response.LoginResponse
import com.alexchar_dev.samplecollector.data.network.response.RefreshResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface GwfApiService {
    @POST(LOGIN_URL)
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST(TOKEN_REFRESH_URL)
    suspend fun refreshToken(@Body refreshTkn: String) : RefreshResponse

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor,
            accessTokenAuthenticator: AccessTokenAuthenticator
        ): GwfApiService {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .authenticator(accessTokenAuthenticator)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GwfApiService::class.java)
        }
    }
}