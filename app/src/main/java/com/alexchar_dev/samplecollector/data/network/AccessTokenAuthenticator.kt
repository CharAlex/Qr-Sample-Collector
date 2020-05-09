package com.alexchar_dev.samplecollector.data.network

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class AccessTokenAuthenticatorImpl(private val tokenRepository: TokenRepository) :
    AccessTokenAuthenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        println("debug: authenticating...")
        val accessToken: String = tokenRepository.getAccessToken()
        if (!isRequestWithAccessToken(response) || accessToken == null) {
            return null
        }
        synchronized(this) {
            val newAccessToken: String = tokenRepository.getAccessToken()
            // Access token is refreshed in another thread.
            if (accessToken != newAccessToken) {
                return newRequestWithAccessToken(response.request(), newAccessToken)
            }

            // Need to refresh an access token
            val updatedAccessToken: String = tokenRepository.refreshAccessToken()
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