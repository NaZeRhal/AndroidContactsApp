package com.maxrzhe.presentation.viewmodel.impl.contacts

import androidx.lifecycle.viewModelScope
import com.maxrzhe.common.util.Resource
import com.maxrzhe.domain.usecases.AddContactAfterPushNotificationUseCase
import com.maxrzhe.presentation.navigation.RouteFragmentDestination
import com.maxrzhe.presentation.navigation.RouteSection
import com.maxrzhe.presentation.navigation.Router
import com.maxrzhe.presentation.viewmodel.base.ViewModelWithRouter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ContactsActivityViewModel(
    private val addContactAfterPushNotificationUseCase: AddContactAfterPushNotificationUseCase,
    router: Router
) : ViewModelWithRouter(router) {

    fun openSettingsSection() {
        router.navigateTo(RouteSection.Settings)
    }

    fun openVolumeSettings() {
        router.navigateTo(RouteSection.VolumeSetting)
    }

    fun openDetailFragment(fbId: String?) {
        fbId?.let {
            router.navigateTo(RouteFragmentDestination.Contacts.ToDetail(fbId))
        }
    }

    fun addContactFromPushData(data: Map<String, String>) {
        viewModelScope.launch {
            addContactAfterPushNotificationUseCase.execute(data).collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.fbId?.let { id ->
                            router.navigateTo(RouteFragmentDestination.Contacts.ToDetail(id))
                        }
                    }
                    is Resource.Error -> {
                        //show some toast
                    }
                }
            }
        }
    }
}