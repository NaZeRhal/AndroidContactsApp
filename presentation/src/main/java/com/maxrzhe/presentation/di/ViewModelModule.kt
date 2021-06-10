package com.maxrzhe.presentation.di

import com.maxrzhe.presentation.navigation.Router
import com.maxrzhe.presentation.util.AppResources
import com.maxrzhe.presentation.util.AppResourcesImpl
import com.maxrzhe.presentation.viewmodel.impl.VolumeSettingViewModel
import com.maxrzhe.presentation.viewmodel.impl.contacts.*
import com.maxrzhe.presentation.viewmodel.impl.settings.SettingsListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single<AppResources> { AppResourcesImpl(androidContext()) }
    single<Router> { Router() }

    viewModel<SearchViewModel> { SearchViewModel(router = get()) }
    viewModel<ContactListViewModel> {
        ContactListViewModel(
            appResources = get(),
            getContactsUseCase = get(),
            deleteContactUseCase = get(),
            router = get()
        )
    }
    viewModel<ContactDetailViewModel> { (fbId: String?) ->
        ContactDetailViewModel(
            fbId = fbId,
            appResources = get(),
            findByIdUseCase = get(),
            addContactUseCase = get(),
            updateContactUseCase = get(),
            router = get()
        )
    }
    viewModel<HomeFragmentViewModel> { HomeFragmentViewModel(router = get()) }
    viewModel<ContactsActivityViewModel> {
        ContactsActivityViewModel(
            addContactAfterPushNotificationUseCase = get(),
            router = get()
        )
    }

    viewModel<SettingsListViewModel> { SettingsListViewModel(appResources = get(), router = get()) }

    viewModel<VolumeSettingViewModel> { VolumeSettingViewModel() }
}