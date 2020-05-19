package com.example.cardreader

import com.example.cardreader.data.Request
import com.example.cardreader.data.Response
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NetworkApi {
    @POST("/scan")
    suspend fun scanCard(@Body request: Request): Response

    @GET("/scan")
    suspend fun getCard(): Response
}