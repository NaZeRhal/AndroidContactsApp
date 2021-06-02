package com.maxrzhe.presentation.viewmodel.impl.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.presentation.navigation.Router
import com.maxrzhe.presentation.viewmodel.base.ViewModelWithRouter

class SearchViewModel(router: Router) : ViewModelWithRouter(router) {

    private var _query = MutableLiveData<String>()
    val query: LiveData<String> = _query

    fun setQuery(query: String) {
        _query.value = query
    }
}