package com.alexchar_dev.samplecollector.data.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationInterceptorImpl(
    private val tokenProvider: TokenProvider
) : AuthorizationInterceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var mainResponse = chain.proceed(chain.request())

        if (mainResponse.code() == 401) {
            mainResponse.close()
            val updatedAccessToken: String = tokenProvider.refreshToken()
            val request = newRequestWithAccessToken(mainResponse.request(), updatedAccessToken)
            mainResponse = chain.proceed(request)
        }

        return mainResponse
    }

    private fun newRequestWithAccessToken(
        request: Request,
        accessToken: String
    ): Request {
        return request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
    }
}


interface AuthorizationInterceptor : Interceptor