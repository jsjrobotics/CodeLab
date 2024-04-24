package com.lab49.assignment.taptosnap.network

import com.lab49.assignment.taptosnap.dataStructures.ApiLabelsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BackendApi @Inject constructor(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory,
    private val networkHelper: NetworkHelper) {

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://veritask.vercel.app")
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()

    private val apiInterface: Api = retrofit.create(Api::class.java)
    fun asyncFetchLabels(): ApiLabelsResponse? {
        val call = apiInterface.getLabels()
        if (networkHelper.isOnline()) {
            val httpResult = call.execute()
            return httpResult.body()
        }
        return null
    }


}