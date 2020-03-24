package com.developer.ivan.easymadbus.presentation.notifications

import androidx.lifecycle.*
import com.developer.ivan.domain.Incident
import com.developer.ivan.easymadbus.core.BaseViewModel
import com.developer.ivan.easymadbus.presentation.models.UIIncident
import com.developer.ivan.easymadbus.presentation.models.toUIIncident
import com.developer.ivan.usecases.GetIncidents
import com.developer.ivan.usecases.GetToken
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val incidents: GetIncidents
) :
    BaseViewModel by BaseViewModel.BaseViewModelImpl(), ViewModel() {

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

        viewModelScope.launch {

            incidents.execute(GetIncidents.Params()).fold(::handleFailure, ::handleIncidents)

        }

    }

    private suspend fun handleIncidents(incidents: List<Incident>) {
        _indicentsState.value =
            IncidentsScreenState.ShowIncidents(incidents.map { it.toUIIncident() })

    }

    @Suppress("UNCHECKED_CAST")
    class FavouriteViewModelFactory(
        private val accessToken: GetToken,
        private val incidents: GetIncidents
    ) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return NotificationsViewModel(
                incidents
            ) as T
        }
    }
}