package com.alexchar_dev.samplecollector.data.models

import com.google.gson.annotations.SerializedName

data class RefreshRequest (
    @SerializedName("refresh")
    var refresh: String
)