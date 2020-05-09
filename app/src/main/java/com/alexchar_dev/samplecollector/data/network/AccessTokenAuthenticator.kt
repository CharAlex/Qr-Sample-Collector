package com.alexchar_dev.samplecollector.data.network

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class AccessTokenAuthenticatorImpl(
    private val tokenProvider: TokenProvider) :
    AccessTokenAuthenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val accessToken: String = tokenProvider.getToken()

        // Check if the request made was previously made as an authenticated request.
        if (!isRequestWithAccessToken(response)) {
            return null
        }

        synchronized(this) {
            val newAccessToken: String = tokenProvider.getToken()
            // Access token is refreshed in another thread.
            if (accessToken != newAccessToken) {
                return newRequestWithAccessToken(response.request(), newAccessToken)
            }

            // Need to refresh an access token synchronously
            val updatedAccessToken: String = tokenProvider.refreshToken()
            return newRequestWithAccessToken(response.request(), updatedAccessToken)
        }
    }

    private fun isRequestWithAccessToken(response: Response): Boolean {
        val header = response.request().header("Authorization")
        return header != null && header.startsWith("Bearer")
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

interface AccessTokenAuthenticator : Authenticator