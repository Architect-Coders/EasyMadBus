package com.developer.ivan.easymadbus.framework

import retrofit2.Call
import retrofit2.http.HeaderMap


interface ApiService {

    companion object Factory {

        /*Endpoints categories*/
        const val MOBILITY_LABS_ENDPOINT = "mobilitylabs/"

        const val TRANSPORT_ENDPOINT = "transport/busemtmad/"
        const val USERS_ENDPOINT = "user/"
        const val STOPS_ENDPOINT = "stops/"


        const val GET_LOGIN = "login/"
        const val POST_STOPS = "stops/"

    }

    @retrofit2.http.GET(MOBILITY_LABS_ENDPOINT + USERS_ENDPOINT + GET_LOGIN)
    fun getLogin(@HeaderMap headers: Map<String, String>): Call<String>

    @retrofit2.http.GET(TRANSPORT_ENDPOINT + STOPS_ENDPOINT + POST_STOPS)
    fun getBusStops(@HeaderMap headers: Map<String, String>): Call<String>

}