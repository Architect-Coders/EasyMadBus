package com.developer.ivan.easymadbus.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.Token
import com.developer.ivan.usecases.GetToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel(private val getAccessToken: GetToken? = null) : ViewModel(), Scope by Scope.IOScopeImplementation(){

    private val _failure = MutableLiveData<Failure>()
    val failure: LiveData<Failure>
        get() = _failure

    suspend fun handleFailure(failure: Failure) {
        _failure.postValue(failure)
    }

    protected fun executeWithToken(body: suspend (Token) -> Unit) {

        getAccessToken?.let { accessToken ->

            viewModelScope.launch {

                    accessToken.execute(
                        GetToken.Params(
                            Constants.EMTApi.USER_EMAIL,
                            Constants.EMTApi.USER_PASSWORD,
                            Constants.EMTApi.API_KEY,
                            Constants.EMTApi.CLIENT_KEY
                        )
                    ).fold(::handleFailure, body)

            }
        }

    }


}