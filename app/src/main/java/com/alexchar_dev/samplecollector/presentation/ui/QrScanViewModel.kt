package com.alexchar_dev.samplecollector.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.alexchar_dev.samplecollector.data.models.QrRegistration
import com.alexchar_dev.samplecollector.data.network.GwfApiService
import com.alexchar_dev.samplecollector.data.network.GwfAuthService
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception

class QrScanViewModel(private val gwfApiService: GwfApiService) : ViewModel() {

    fun sendQrGeolocation(serial: String, lat: Double, long: Double) = liveData<Response<String>> {
        try {
            val response = gwfApiService.registerQR(serial, lat, long)
            emit(response)
        } catch (e: HttpException) {
            println("debug: exception $e")
        }
    }
}