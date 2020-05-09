package com.alexchar_dev.samplecollector.data.models

import com.google.gson.annotations.SerializedName

data class QrRegistration (
    @SerializedName("serial")
    var serial: String,

    @SerializedName("lat")
    var lat: Double,

    @SerializedName("lng")
    var long: Double
)