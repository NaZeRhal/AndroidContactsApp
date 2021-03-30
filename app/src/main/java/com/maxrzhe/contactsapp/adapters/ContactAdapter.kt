package com.maxrzhe.contactsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.bumptech.glide.Glide
import com.maxrzhe.contactsapp.R
import com.maxrzhe.contactsapp.databinding.ItemContactBinding
import com.maxrzhe.contactsapp.model.Contact
import java.util.*

class ContactAdapter(
    private val context: Context,
    private val onContactClickListener: OnContactClickListener
) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    var itemList: List<Contact> = emptyList()
        set(value) {
            field = value
            performFiltering(filter)
        }

    var filter: String? = null
        set(value) {
            field = value
            performFiltering(value)
        }

    private val sortedList = SortedList(
        Contact::class.java,
        object : SortedList.Callback<Contact>() {

            override fun compare(contact1: Contact?, contact2: Contact?): Int {
                val name1 = contact1?.name
                val name2 = contact2?.name
                return if (name1 != null && name2 != null) {
                    name1.compareTo(name2)
                } else -1
            }

            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onChanged(position: Int, count: Int) {
                notifyItemRangeChanged(position, count)
            }

            override fun areContentsTheSame(oldContact: Contact?, newContact: Contact?): Boolean {
                return if (oldContact != null && newContact != null) {
                    oldContact == newContact
                } else false
            }

            override fun areItemsTheSame(contact1: Contact?, contact2: Contact?): Boolean {
                return if (contact1 != null && contact2 != null) {
                    contact1.id == contact2.id
                } else false
            }

        })

    inner class ViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = sortedList[position]
        with(holder.binding) {
            tvContactName.text = contact.name

            Glide
                .with(context)
                .load(contact.image)
                .placeholder(R.drawable.person_placeholder)
                .circleCrop()
                .into(ivContactImage)

            root.setOnClickListener {
                onContactClickListener.onClick(position, contact)
            }
        }
    }

    fun addContact(contact: Contact) {
        itemList = listOf(contact) + itemList
    }

    override fun getItemCount(): Int = sortedList.size()

    private fun performFiltering(query: String?) {
        val filteredContacts = if (query == null || query.isEmpty()) {
            itemList
        } else {
            val filterPattern: String = query.toLowerCase(Locale.getDefault()).trim()
            itemList.filter { anyMatches(it, filterPattern) }
        }
        replaceAll(filteredContacts)
    }

    private fun replaceAll(contacts: List<Contact>) {
        sortedList.beginBatchedUpdates()
        (sortedList.size() - 1 downTo 0 step 1).forEach { i ->
            val contact = sortedList[i]
            if (!contacts.contains(contact)) {
                sortedList.remove(contact)
            }
        }
        sortedList.addAll(contacts)
        sortedList.endBatchedUpdates()
    }

    private fun anyMatches(contact: Contact, pattern: String): Boolean {
        return with(contact) {
            name?.toLowerCase(Locale.getDefault())?.contains(pattern) ?: false ||
                    email?.toLowerCase(Locale.getDefault())?.contains(pattern) ?: false ||
                    phone?.toLowerCase(Locale.getDefault())?.contains(pattern) ?: false
        }
    }


    interface OnContactClickListener {
        fun onClick(position: Int, contact: Contact)
    }

}