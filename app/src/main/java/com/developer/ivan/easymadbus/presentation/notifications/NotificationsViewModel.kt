package com.developer.ivan.easymadbus.presentation.notifications

import androidx.lifecycle.*
import com.developer.ivan.domain.*
import com.developer.ivan.easymadbus.core.BaseViewModel
import com.developer.ivan.easymadbus.presentation.models.*
import com.developer.ivan.usecases.GetIncidents
import com.developer.ivan.usecases.GetToken
import kotlinx.coroutines.*

class NotificationsViewModel(
    accessToken: GetToken,
    private val incidents: GetIncidents
) :
    BaseViewModel(accessToken) {

    init {
        initScope()
    }

    sealed class IncidentsScreenState {

        object Loading : IncidentsScreenState()
        class ShowIncidents(
            val incidents: List<UIIncident>
        ) : IncidentsScreenState()

    }

    private val _indicentsState = MutableLiveData<IncidentsScreenState>()
    val incidentsState: LiveData<IncidentsScreenState>
        get() = _indicentsState


    fun obtainInfo() {

        _indicentsState.value =
            IncidentsScreenState.Loading


        executeWithToken { token ->
            viewModelScope.launch {

                incidents.execute(GetIncidents.Params(token)).fold(::handleFailure,::handleIncidents)

            }

        }
    }

    private suspend fun handleIncidents(incidents: List<Incident>){
        _indicentsState.value = IncidentsScreenState.ShowIncidents(incidents.map { it.toUIIncident() })

    }

    override fun onCleared() {
        super.onCleared()
        cancelScope()
    }

    @Suppress("UNCHECKED_CAST")
    class FavouriteViewModelFactory(
        private val accessToken: GetToken,
        private val incidents: GetIncidents
    ) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return NotificationsViewModel(
                accessToken,
                incidents
            ) as T
        }
    }
}