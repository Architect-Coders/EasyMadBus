package com.developer.ivan.easymadbus.core

import com.developer.ivan.easymadbus.core.Constants.EMTApi.ENDPOINT
import com.developer.ivan.easymadbus.framework.ApiService
import com.google.android.gms.maps.model.LatLng
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object Constants {

    object EMTApi {
        const val USER_EMAIL = "ivanfr89@gmail.com"
        const val USER_PASSWORD = "Mm12345678"
        const val API_KEY = "0641b5f4-2441-47d3-981e-05d429f61ac7"
        const val CLIENT_KEY = "fc9da71e-87e5-4137-9140-7ae59547310f"
        const val ENDPOINT = "https://openapi.emtmadrid.es/v2/"
        val MADRID_LOC = LatLng(40.416775, -3.703790)

    }

    object Location {
        const val INTERVAL = 3000L
        const val FAST_INTERVAL = 1500L
    }
    object Token {
        const val TOKEN_ID=999
    }




}

val okHttp = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS).build()

val retrofit =
    Retrofit.Builder().baseUrl(ENDPOINT)
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(okHttp)
        .build().create(ApiService::class.java)