package com.developer.ivan.easymadbus.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.Token
import com.developer.ivan.usecases.GetToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


interface BaseViewModel
{
    val mutableFailure: MutableLiveData<Failure>
        get() = MutableLiveData()
    val failure: LiveData<Failure>
        get() = mutableFailure

    suspend fun handleFailure(failure: Failure)

    class BaseViewModelImpl: BaseViewModel{
        override suspend fun handleFailure(failure: Failure) {
            mutableFailure.postValue(failure)
        }

    }
}
