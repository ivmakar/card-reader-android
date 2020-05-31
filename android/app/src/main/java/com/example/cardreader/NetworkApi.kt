package com.example.cardreader

import com.example.cardreader.data.Request
import com.example.cardreader.data.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NetworkApi {
    @POST("/scan")
    fun scanCard(@Body request: Request): Call<Response>

    @GET("/scan")
    fun getCard(): Call<Response>
}