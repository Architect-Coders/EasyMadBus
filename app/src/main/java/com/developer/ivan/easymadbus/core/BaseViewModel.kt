package com.developer.ivan.easymadbus.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.developer.ivan.easymadbus.domain.models.Token
import com.developer.ivan.easymadbus.domain.uc.GetToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel(private val getAccessToken: GetToken? = null) : ViewModel(), Scope by Scope.DefaultScopeImplementation() {

    private val _failure = MutableLiveData<Failure>()
    val failure: LiveData<Failure>
        get() = _failure

    fun handleFailure(failure: Failure) {
        _failure.postValue(failure)
    }

    protected fun executeWithToken(body: (Token) -> Unit) {

        getAccessToken?.let { accessToken ->

                launch {

                    accessToken.execute(
                        GetToken.Params(
                            Constants.EMTApi.USER_EMAIL,
                            Constants.EMTApi.USER_PASSWORD,
                            Constants.EMTApi.API_KEY,
                            Constants.EMTApi.CLIENT_KEY
                        )
                    ).either(::handleFailure, body)

                }
            }

        }


}