package com.maxrzhe.contactsapp.repository.factory

import android.content.Context
import com.maxrzhe.contactsapp.repository.Repository

interface RepositoryFactory {
    fun create(context: Context): Repository
}