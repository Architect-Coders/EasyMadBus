package com.developer.ivan.easymadbus.framework

import retrofit2.Call

object Retrofit
{

}

interface ApiService{

    companion object Factory {

        /*Endpoints categories*/
        const val USERS_ENDPOINT = "user/"




        const val GET_LOGIN = "login/"

    }

    @retrofit2.http.GET(USERS_ENDPOINT+GET_LOGIN) fun getLogin(): Call<String>
}