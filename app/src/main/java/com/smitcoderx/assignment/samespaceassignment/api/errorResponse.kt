package com.smitcoderx.assignment.samespaceassignment.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

fun <T> errorResponse(response: Response<T>): Error? {
    val gson = Gson()
    val type = object : TypeToken<Error?>() {}.type
    return gson.fromJson(response.errorBody()!!.charStream(), type)
}