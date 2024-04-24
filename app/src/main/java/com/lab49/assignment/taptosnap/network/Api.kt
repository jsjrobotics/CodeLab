package com.lab49.assignment.taptosnap.network

import com.lab49.assignment.taptosnap.dataStructures.ApiLabelsResponse
import com.lab49.assignment.taptosnap.dataStructures.ApiValidateResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @GET("/api/images")
    fun getLabels(): Call<ApiLabelsResponse?>

    @POST("/api/images")
    fun validateLabels(imageLabel: String, image: String): Call<ApiValidateResponse?>
}