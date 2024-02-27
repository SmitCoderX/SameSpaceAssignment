package com.smitcoderx.assignment.samespaceassignment.forYou


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForYouModel(
    @SerializedName("data")
    val data: List<Data>?,
    var message: String
) : Parcelable