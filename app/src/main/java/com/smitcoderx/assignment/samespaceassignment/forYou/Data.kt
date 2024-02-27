package com.smitcoderx.assignment.samespaceassignment.forYou


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    @SerializedName("accent")
    var accent: String? = "",
    @SerializedName("artist")
    var artist: String? = "",
    @SerializedName("cover")
    var cover: String? = "",
    @SerializedName("date_created")
    var dateCreated: String? = "",
    @SerializedName("date_updated")
    var dateUpdated: String? = "",
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("sort")
    var sort: Int? = null,
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("top_track")
    var topTrack: Boolean? = false,
    @SerializedName("url")
    var url: String? = "",
    @SerializedName("user_created")
    var userCreated: String? = "",
    @SerializedName("user_updated")
    var userUpdated: String? = ""
) : Parcelable