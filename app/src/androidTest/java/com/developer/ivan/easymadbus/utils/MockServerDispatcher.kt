package com.developer.ivan.easymadbus.utils

import android.app.Application
import com.developer.ivan.easymadbus.framework.ApiService.Factory.GET_DETAIL
import com.developer.ivan.easymadbus.framework.ApiService.Factory.GET_LOGIN
import com.developer.ivan.easymadbus.framework.ApiService.Factory.MOBILITY_LABS_ENDPOINT
import com.developer.ivan.easymadbus.framework.ApiService.Factory.POST_STOPS
import com.developer.ivan.easymadbus.framework.ApiService.Factory.STOPS_ENDPOINT
import com.developer.ivan.easymadbus.framework.ApiService.Factory.TRANSPORT_ENDPOINT
import com.developer.ivan.easymadbus.framework.ApiService.Factory.USERS_ENDPOINT
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockServerDispatcher(val application: Application) {

    inner class Response : Dispatcher(){
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when(request.path) {
                "/$TRANSPORT_ENDPOINT$STOPS_ENDPOINT$POST_STOPS" ->
                    MockResponse().fromJson(application,"bus_list.json").apply {
                        setResponseCode(200)
                    }
                "/$MOBILITY_LABS_ENDPOINT$USERS_ENDPOINT$GET_LOGIN" ->
                    MockResponse().fromJson(application,"login.json").apply {
                        setResponseCode(200)
                    }
                "/${TRANSPORT_ENDPOINT}${STOPS_ENDPOINT}1/${GET_DETAIL}" ->
                    MockResponse().fromJson(application,"bus_detail.json").apply {
                        setResponseCode(200)
                    }

                else -> MockResponse().fromJson(application,"bus_list.json")
            }
        }

    }
}