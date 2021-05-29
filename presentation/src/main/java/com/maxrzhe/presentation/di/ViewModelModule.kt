package com.maxrzhe.presentation.di

import com.maxrzhe.presentation.util.AppResources
import com.maxrzhe.presentation.util.AppResourcesImpl
import com.maxrzhe.presentation.viewmodel.impl.contacts.*
import com.maxrzhe.presentation.viewmodel.impl.settings.SettingsListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single<AppResources> { AppResourcesImpl(androidContext()) }

    viewModel<ContactListViewModel> { ContactListViewModel(get(), get(), get()) }
    viewModel<ContactDetailViewModel> { ContactDetailViewModel(get(), get(), get(), get()) }
    viewModel<SearchViewModel> { SearchViewModel() }
    viewModel<HomeFragmentViewModel> { HomeFragmentViewModel() }
    viewModel<SettingsListViewModel> { SettingsListViewModel() }
    viewModel<SplashViewModel> { SplashViewModel() }
}