package com.example.cardreader

import com.example.cardreader.data.Request
import com.example.cardreader.data.Response
import org.koin.dsl.module

val dataRepositoryModule = module {
    factory { DataRepository(get()) }
}

class DataRepository(private val networkApi: NetworkApi) {

    suspend fun scanCard(value: String): Response = networkApi.scanCard(Request(value))

    suspend fun get(): Response = networkApi.getCard()
}