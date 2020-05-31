package com.example.cardreader

import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_URL = "http://192.168.1.100:3000/"

const val TAG = "TAG"

class MainViewModel : ViewModel() {

    val retrofit = Retrofit
        .Builder()
        .baseUrl(API_URL)
        .client(
            OkHttpClient()
            .newBuilder()
            .readTimeout(180, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor())
            .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val networkApi = retrofit.create(NetworkApi::class.java)

    val imageUri = ObservableField<Uri?>()

    val text = ObservableField("")
    var splitedText = ArrayList<String>()
    val phone = ObservableField("")
    val email = ObservableField("")
    val site = ObservableField("")

}