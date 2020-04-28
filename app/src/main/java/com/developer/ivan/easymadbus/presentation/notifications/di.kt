package com.developer.ivan.easymadbus.presentation.notifications

import com.developer.ivan.usecases.GetIncidents
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class NotificationsFragmentModule {

    @Provides
    fun notificationsViewModelProvider(
        incidents: GetIncidents

    ): NotificationsViewModel = NotificationsViewModel(incidents)

}

@Subcomponent(modules = [(NotificationsFragmentModule::class)])
interface NotificationsFragmentComponent {
    val notificationsViewModel: NotificationsViewModel
}
