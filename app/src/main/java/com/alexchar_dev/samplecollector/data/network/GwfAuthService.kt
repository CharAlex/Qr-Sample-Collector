package com.alexchar_dev.samplecollector.data.network

import com.alexchar_dev.samplecollector.Constants
import com.alexchar_dev.samplecollector.data.models.LoginRequest
import com.alexchar_dev.samplecollector.data.models.RefreshRequest
import com.alexchar_dev.samplecollector.data.network.response.LoginResponse
import com.alexchar_dev.samplecollector.data.network.response.RefreshResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface GwfAuthService {
    @POST(Constants.LOGIN_URL)
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST(Constants.TOKEN_REFRESH_URL)
    fun refreshToken(@Body refreshRequest: RefreshRequest) : Call<RefreshResponse>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): GwfAuthService {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GwfAuthService::class.java)
        }
    }
}