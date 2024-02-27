package com.smitcoderx.assignment.samespaceassignment.api


import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("errors")
    val errors: List<ErrorX>?
)