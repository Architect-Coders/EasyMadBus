package com.developer.ivan.easymadbus.presentation.notifications

import com.developer.ivan.usecases.GetIncidents
import com.developer.ivan.usecases.GetToken
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class NotificationsFragmentModule{

    @Provides
    fun notificationsViewModelProvider(
        accessToken: GetToken,
        incidents: GetIncidents

    ): NotificationsViewModel = NotificationsViewModel(accessToken,incidents)

}

@Subcomponent(modules = [(NotificationsFragmentModule::class)])
interface NotificationsFragmentComponent
{
    val notificationsViewModel: NotificationsViewModel
}