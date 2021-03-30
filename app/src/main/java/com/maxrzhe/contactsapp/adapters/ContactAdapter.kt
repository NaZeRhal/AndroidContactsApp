package com.maxrzhe.contactsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

    private var filteredList: List<Contact> = itemList

    inner class ViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = filteredList[position]
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

    override fun getItemCount(): Int = filteredList.size

    private fun performFiltering(query: String?) {
        val filteredContacts = if (query == null || query.isEmpty()) {
            itemList
        } else {
            val filterPattern: String = query.toLowerCase(Locale.getDefault()).trim()
            itemList.filter { anyMatches(it, filterPattern) }
        }
        filteredList = filteredContacts
        notifyDataSetChanged()
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