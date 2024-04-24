package com.lab49.assignment.taptosnap

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


class BackendApi @Inject constructor(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory) {
    fun fetchLabels(): Boolean {
        return false
    }

    private val retrofit: Retrofit by lazy {

        Retrofit.Builder()
            .baseUrl("https://reqres.in")
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }
}