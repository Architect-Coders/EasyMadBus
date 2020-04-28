package com.developer.ivan.easymadbus.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.developer.ivan.domain.Failure


interface BaseViewModel {
    val mutableFailure: MutableLiveData<Failure>
        get() = MutableLiveData()
    val failure: LiveData<Failure>
        get() = mutableFailure

    suspend fun handleFailure(failure: Failure)

    class BaseViewModelImpl : BaseViewModel {
        override suspend fun handleFailure(failure: Failure) {
            mutableFailure.postValue(failure)
        }

    }
}
