package com.alexchar_dev.samplecollector.data.network

import com.alexchar_dev.samplecollector.data.models.RefreshRequest
import com.pixplicity.easyprefs.library.Prefs
import retrofit2.HttpException
import java.lang.Exception


class TokenProviderImpl(private val gwfAuthService: GwfAuthService) : TokenProvider {
    override fun getToken(): String {
        return Prefs.getString("AccessToken", null)
    }

    override fun refreshToken(): String{
        var newToken : String? = null
        try {
            val refresh = Prefs.getString("RefreshToken", null)
            val response = gwfAuthService.refreshToken(RefreshRequest(refresh)).execute()

            newToken = response.body()?.access
            //TODO newToken is null
            Prefs.putString("AccessToken", newToken)
        } catch (e: HttpException) {
            println("debug: exception $e")
        }

        return newToken ?: Prefs.getString("AccessToken", null)
    }

    override fun deleteToken() {
        Prefs.clear()
    }
}

interface TokenProvider {
    fun getToken() : String
    fun refreshToken() : String
    fun deleteToken()
}
