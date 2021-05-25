package com.maxrzhe.presentation.model

import com.example.data_api.model.Contact
import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.util.ClickListener

data class ContactItemViewModel(
    val contact: Contact,
    val clickListener: ClickListener
) : BaseItemViewModel() {

    override val id: Long get() = this.contact.hashCode().toLong()
    override val layoutId: Int get() = R.layout.item_contact

    override fun isTheSameItem(baseItemViewModel: BaseItemViewModel): Boolean {
        return if (baseItemViewModel is ContactItemViewModel) {
            this.contact.fbId == baseItemViewModel.contact.fbId
        } else false
    }

    override fun hasTheSameContent(baseItemViewModel: BaseItemViewModel): Boolean {
        return if (baseItemViewModel is ContactItemViewModel) {
            this.contact == baseItemViewModel.contact
        } else false
    }
}

