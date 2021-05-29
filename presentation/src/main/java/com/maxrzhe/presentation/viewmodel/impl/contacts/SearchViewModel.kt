package com.maxrzhe.presentation.viewmodel.impl.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

class SearchViewModel : BaseViewModel() {

    private var _query = MutableLiveData<String>()
    val query: LiveData<String> = _query

    fun setQuery(query: String) {
        _query.value = query
    }

    override fun onBackPressed() {}
}