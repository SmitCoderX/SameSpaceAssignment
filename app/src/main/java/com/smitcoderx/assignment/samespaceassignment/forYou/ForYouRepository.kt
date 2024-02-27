package com.smitcoderx.assignment.samespaceassignment.forYou

import com.smitcoderx.assignment.samespaceassignment.api.SameSpaceAPI
import com.smitcoderx.assignment.samespaceassignment.api.errorResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForYouRepository @Inject constructor(
    private val api: SameSpaceAPI,
) {

    suspend fun getSongsList(): ForYouModel {
        val response = api.getSongsList()
        return try {
            if (response.isSuccessful) {
                ForYouModel(response.body()?.data, "Success")
            } else {
                ForYouModel(null, errorResponse(response)?.errors?.get(0)?.message.toString())
            }
        } catch (e: HttpException) {
            ForYouModel(null, "Something Went Wrong")
        } catch (e: IOException) {
            ForYouModel(null, "No Internet Connection")
        }
    }

    suspend fun getSongsData(id: Int): Data {
        val response = api.getSongData(id)
        return try {
            if (response.isSuccessful) {
                response.body()!!
            } else {
                Data("0")
            }
        } catch (e: HttpException) {
            Data("1")
        } catch (e: IOException) {
            Data("2")
        }
    }

}