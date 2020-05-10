package com.alexchar_dev.samplecollector.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.alexchar_dev.samplecollector.data.network.GwfApiService
import retrofit2.HttpException
import retrofit2.Response

class QrResultViewModel(private val gwfApiService: GwfApiService) : ViewModel() {
    fun sendResult(serial: String?, result: Int?) = liveData<Response<String>> {
        if(serial.isNullOrEmpty() || result == null) return@liveData
        try {
            val response = gwfApiService.sendResult(serial, result)
            emit(response)
        } catch (e: HttpException) {
            println("debug: exception $e")
        }
    }
}