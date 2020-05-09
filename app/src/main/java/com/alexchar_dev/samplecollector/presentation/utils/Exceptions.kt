package com.alexchar_dev.samplecollector.presentation.utils

import android.content.Context
import com.alexchar_dev.samplecollector.R
import java.io.IOException

class NoConnectivityException : IOException()

class ExceptionUtils {
    companion object {
        fun getApiErrorMessage(context: Context, exception: Throwable): String {
            return when (exception) {
                is NoConnectivityException -> context.getString(R.string.no_internet_connection)
                else -> "Something went wrong"
            }
        }
    }
}