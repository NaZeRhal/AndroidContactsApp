package com.maxrzhe.data.app

import android.app.Application
import com.maxrzhe.data.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ContactsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ContactsApp)
            modules(listOf(dataModule, ))
        }
    }
}