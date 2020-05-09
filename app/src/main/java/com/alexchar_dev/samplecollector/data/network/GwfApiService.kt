package com.alexchar_dev.samplecollector.data.network

import com.alexchar_dev.samplecollector.Constants
import com.alexchar_dev.samplecollector.Constants.BASE_URL
import com.alexchar_dev.samplecollector.data.models.QrRegistration
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GwfApiService {
    @FormUrlEncoded
    @POST(Constants.REGISTER_URL)
    suspend fun registerQR(@Field("serial") serial: String, @Field("lat") lat: Double, @Field("lng") lng: Double ) : Response<String>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor,
            authorizationInterceptor: AuthorizationInterceptor
        ): GwfApiService {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .addInterceptor(authorizationInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GwfApiService::class.java)
        }
    }
}