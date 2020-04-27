package com.developer.ivan.easymadbus.framework

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.Path


interface ApiService {

    companion object Factory {

        /*Endpoints categories*/
        const val MOBILITY_LABS_ENDPOINT = "mobilitylabs/"

        const val TRANSPORT_ENDPOINT = "transport/busemtmad/"
        const val USERS_ENDPOINT = "user/"
        const val STOPS_ENDPOINT = "stops/"
        const val ARRIVES_ENDPOINT = "arrives/"
        const val LINES_ENDPOINT = "lines/"


        const val GET_LOGIN = "login/"
        const val POST_STOPS = "list/"
        const val GET_INCIDENTS = "incidents/"
        const val GET_DETAIL = "detail/"

    }

    @retrofit2.http.GET(MOBILITY_LABS_ENDPOINT + USERS_ENDPOINT + GET_LOGIN)
    fun getLogin(@HeaderMap headers: Map<String, String>): Call<String>

    @retrofit2.http.POST(TRANSPORT_ENDPOINT + STOPS_ENDPOINT + POST_STOPS)
    fun getBusStops(@HeaderMap headers: Map<String, String>): Call<String>

    @retrofit2.http.POST("$TRANSPORT_ENDPOINT$STOPS_ENDPOINT{stopId}/$ARRIVES_ENDPOINT")
    fun getArrivesEndpoint(@Path("stopId") stopId: String,
                           @Body jsonBody: String,
                           @HeaderMap headers: Map<String, String>): Call<String>

    @retrofit2.http.GET("$TRANSPORT_ENDPOINT$LINES_ENDPOINT$GET_INCIDENTS"+"all")
    fun getIncidents(@HeaderMap headers: Map<String, String>): Call<String>

    @retrofit2.http.GET("$TRANSPORT_ENDPOINT$STOPS_ENDPOINT{stopId}/$GET_DETAIL")
    fun getStopDetail(@HeaderMap headers: Map<String, String>,
                      @Path("stopId") stopId: String): Call<String>


}
