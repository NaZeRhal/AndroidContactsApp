package com.maxrzhe.contacts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private var _query = MutableLiveData<String>()
    val query: LiveData<String> = _query

    fun setQuery(query: String) {
        _query.value = query
    }

}