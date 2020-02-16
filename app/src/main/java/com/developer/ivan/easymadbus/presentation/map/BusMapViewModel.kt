package com.developer.ivan.easymadbus.presentation.map

import androidx.lifecycle.ViewModel
import com.developer.ivan.easymadbus.core.Scope

class BusMapViewModel : ViewModel(), Scope by Scope.IOScopeImplementation()
{
    sealed class BusScreenState{

        object Loading

    }


    init {
        initScope()
    }




    override fun onCleared() {
        super.onCleared()
        cancelScope()
    }
}