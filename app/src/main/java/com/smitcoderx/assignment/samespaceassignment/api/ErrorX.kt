package com.smitcoderx.assignment.samespaceassignment.api


import com.google.gson.annotations.SerializedName

data class ErrorX(
    @SerializedName("extensions")
    val extensions: ExtensionsError?,
    @SerializedName("message")
    val message: String?
)