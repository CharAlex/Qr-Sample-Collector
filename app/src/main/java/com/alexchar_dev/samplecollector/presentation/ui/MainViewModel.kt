package com.alexchar_dev.samplecollector.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.alexchar_dev.samplecollector.data.models.LoginRequest
import com.alexchar_dev.samplecollector.data.network.GwfApiService
import com.alexchar_dev.samplecollector.data.network.GwfAuthService
import com.alexchar_dev.samplecollector.data.network.response.LoginResponse
import com.alexchar_dev.samplecollector.presentation.utils.NoConnectivityException
import com.pixplicity.easyprefs.library.Prefs
import retrofit2.HttpException
import java.lang.Exception

class MainViewModel(private val gwfAuthService: GwfAuthService) : ViewModel() {

    fun login(loginRequest: LoginRequest) = liveData<LoginResponse> {
        try {
            val response = gwfAuthService.login(loginRequest)

            Prefs.putString("AccessToken", response.access)
            Prefs.putString("RefreshToken", response.refresh)

            emit(response)
        } catch (e: NoConnectivityException) {
            println("debug: exception $e")
        }
    }
}