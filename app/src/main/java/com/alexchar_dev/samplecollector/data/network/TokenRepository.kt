package com.alexchar_dev.samplecollector.data.network

import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TokenRepositoryImpl : TokenRepository {
    override fun getAccessToken(): String {
        return Prefs.getString("AccessToken", null)
    }

    override fun refreshAccessToken(): String{
//        GlobalScope.launch(Dispatchers.IO) {
//            val accessToken = gwfApiService.refreshToken(Prefs.getString("AccessToken", null)).access
//            Prefs.putString("AccessToken", accessToken)
//        }
        TODO()
    }

}

interface TokenRepository {
    fun getAccessToken() : String
    fun refreshAccessToken() : String
}
