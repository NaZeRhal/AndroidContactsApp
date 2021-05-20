package com.maxrzhe.presentation.adapters

import com.example.data_api.model.Contact
import com.maxrzhe.presentation.R
import java.util.*

class ContactsAdapter : GenericBindingAdapter<Contact>(ContactsDifUtil) {

    companion object ContactsDifUtil : GenericItemDiff<Contact> {
        override fun isSame(
            oldItems: List<Contact>,
            newItems: List<Contact>,
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            val oldData = oldItems[oldItemPosition]
            val newData = newItems[newItemPosition]
            return oldData.fbId == newData.fbId
        }

        override fun isSameContent(
            oldItems: List<Contact>,
            newItems: List<Contact>,
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            val oldData = oldItems[oldItemPosition]
            val newData = newItems[newItemPosition]
            return oldData == newData
        }
    }

    private var onSearchResultListener: OnSearchResultListener? = null

    private var copiedList: List<Contact> = emptyList()

    var filter: String? = null
        set(value) {
            field = value
            performFiltering(value)
        }

    override fun getLayoutId(position: Int): Int = R.layout.item_contact

    override fun setList(itemsList: List<Contact>) {
        super.setList(itemsList)
        copiedList = itemsList
    }

    private fun performFiltering(query: String?) {
        val filteredContacts = if (query == null || query.isEmpty()) {
            copiedList
        } else {
            val filterPattern: String = query.toLowerCase(Locale.getDefault()).trim()
            copiedList.filter { anyMatches(it, filterPattern) }
        }

        if (copiedList.size != filteredContacts.size) {
            onSearchResultListener?.onSearchResult(filteredContacts.size)
        } else {
            onSearchResultListener?.onSearchResult(-1)
        }
        super.setList(filteredContacts)
    }

    private fun anyMatches(contact: Contact, pattern: String): Boolean {
        return with(contact) {
            name.toLowerCase(Locale.getDefault()).contains(pattern) ||
                    email.toLowerCase(Locale.getDefault()).contains(pattern) ||
                    phone.toLowerCase(Locale.getDefault()).contains(pattern)
        }
    }

    fun setOnSearchResultListener(listener: OnSearchResultListener) {
        this.onSearchResultListener = listener
    }

    interface OnSearchResultListener {
        fun onSearchResult(resultCount: Int)
    }
}




