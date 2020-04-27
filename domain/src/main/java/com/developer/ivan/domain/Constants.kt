package com.developer.ivan.domain

object Constants {

    object EMTApi {
        const val USER_EMAIL = "ivanfr89@gmail.com"
        const val USER_PASSWORD = "Mm12345678"
        const val API_KEY = "0641b5f4-2441-47d3-981e-05d429f61ac7"
        const val CLIENT_KEY = "fc9da71e-87e5-4137-9140-7ae59547310f"
        const val ENDPOINT = "https://openapi.emtmadrid.es/v2/"
        val MADRID_LOC = Locate(40.416775, -3.703790)
        const val TIMEOUT = 60L

    }

    object Location {
        const val INTERVAL = 3000L
        const val FAST_INTERVAL = 1500L
    }
    object Token {
        const val TOKEN_ID=999
    }

    object Args {
        const val ARG_FAVOURITE = "argFavourite"
    }





}
