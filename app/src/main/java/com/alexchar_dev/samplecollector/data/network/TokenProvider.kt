package com.alexchar_dev.samplecollector.data.network

import com.pixplicity.easyprefs.library.Prefs
import java.lang.Exception


class TokenProviderImpl(private val gwfAuthService: GwfAuthService) : TokenProvider {
    override fun getToken(): String {
        return Prefs.getString("AccessToken", null)
    }

    override fun refreshToken(): String{
        var newToken : String? = null
        try {
            val response = gwfAuthService.refreshToken(Prefs.getString("AccessToken", null)).execute()
            newToken = response.body()?.access
            Prefs.putString("AccessToken", newToken)
        } catch (e: Exception) {
            TODO()
        }

        return newToken ?: Prefs.getString("AccessToken", null)
    }

}

interface TokenProvider {
    fun getToken() : String
    fun refreshToken() : String
}
