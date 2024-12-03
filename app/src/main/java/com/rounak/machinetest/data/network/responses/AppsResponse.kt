package com.rounak.machinetest.data.network.responses


import com.google.gson.annotations.SerializedName

data class AppsResponse(
    @SerializedName("data")
    val actualData: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)