package com.maxrzhe.contactsapp.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maxrzhe.contactsapp.R
import com.maxrzhe.contactsapp.databinding.ItemContactBinding
import com.maxrzhe.contactsapp.model.Contact
import java.util.*
import kotlin.collections.ArrayList

class ContactAdapter(
    private val context: Context,
    private val fullContactsList: ArrayList<Contact>,
    private val onContactClickListener: OnContactClickListener
) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>(), Filterable {

    private var filteredList: ArrayList<Contact> = ArrayList(fullContactsList)
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = fullContactsList[position]
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
        fullContactsList.add(0, contact)
        filteredList.add(0, contact)
        notifyItemInserted(0)
    }

    override fun getItemCount(): Int = fullContactsList.size

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredContacts = if (constraint == null || constraint.isEmpty()) {
                filteredList
            } else {
                val filterPattern: String =
                    constraint.toString().toLowerCase(Locale.getDefault()).trim()
                filteredList.filter { anyMatches(it, filterPattern) }
            }

            return FilterResults().apply {
                values = filteredContacts
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            fullContactsList.clear()
            fullContactsList.addAll(((results?.values) as? List<Contact>)!!)
            notifyDataSetChanged()
        }

        private fun anyMatches(contact: Contact, pattern: String): Boolean {
            return with(contact) {
                name.toLowerCase(Locale.getDefault()).contains(pattern) ||
                        email.toLowerCase(Locale.getDefault()).contains(pattern) ||
                        phone.toLowerCase(Locale.getDefault()).contains(pattern)
            }
        }
    }

    interface OnContactClickListener {
        fun onClick(position: Int, contact: Contact)
    }

}