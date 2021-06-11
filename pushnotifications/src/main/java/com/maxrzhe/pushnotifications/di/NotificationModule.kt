package com.maxrzhe.pushnotifications.di

import com.maxrzhe.pushnotifications.ContactNotificationManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val notificationModule = module {
    single<ContactNotificationManager> { ContactNotificationManager(androidContext()) }
}