package com.maxrzhe.contactsapp

import android.app.Application
import com.maxrzhe.data.di.dataModule
import com.maxrzhe.domain.di.domainModule
import com.maxrzhe.presentation.di.viewModelModule
import com.maxrzhe.pushnotifications.di.notificationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ContactsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ContactsApp)
            modules(listOf(dataModule, viewModelModule, domainModule, notificationModule))
        }
    }
}