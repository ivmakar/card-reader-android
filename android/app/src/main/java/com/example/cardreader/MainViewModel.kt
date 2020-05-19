package com.example.cardreader

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cardreader.data.Response
import kotlinx.coroutines.*
import org.koin.dsl.module

val viewModelModule = module {
    factory { MainViewModel(get()) }
}

const val TAG = "TAG"

class MainViewModel(private val dataRepository: DataRepository) : ViewModel() {

    suspend fun scanCard(value: String): Response = dataRepository.scanCard(value)

    suspend fun get(): Response = dataRepository.get()
}