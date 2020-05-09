package com.alexchar_dev.samplecollector.data.network.response

import com.google.gson.annotations.SerializedName

data class RefreshResponse (
    @SerializedName("access")
    var access: String
)