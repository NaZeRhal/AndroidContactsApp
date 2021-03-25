package com.maxrzhe.contactsapp.adapters

import android.content.Context
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

class ContactAdapter(private val context: Context, private val contacts: ArrayList<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>(), Filterable {

    private var onContactClickListener: OnContactClickListener? = null
    private var contactsFull: List<Contact>? = ArrayList(contacts)

    inner class ViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]
        with(holder.binding) {
            tvContactName.text = contact.name

            Glide
                .with(context)
                .load(getDrawableIdByName(contact.image))
                .placeholder(R.drawable.person_placeholder)
                .circleCrop()
                .into(ivContactImage)

            if (onContactClickListener != null) {
                root.setOnClickListener {
                    onContactClickListener!!.onClick(position, contact)
                }
            }
        }
    }

    override fun getItemCount(): Int = contacts.size

    private fun getDrawableIdByName(drawableName: String): Int {
        return context.resources.getIdentifier(drawableName, "drawable", context.packageName)
    }

    fun setOnContactClickListener(onContactClickListener: OnContactClickListener) {
        this.onContactClickListener = onContactClickListener
    }

    interface OnContactClickListener {
        fun onClick(position: Int, contact: Contact)
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredContacts = ArrayList<Contact>()

            if (constraint == null || constraint.isEmpty()) {
                contactsFull?.let { filteredContacts.addAll(it) }
            } else {
                val filterPattern: String =
                    constraint.toString().toLowerCase(Locale.getDefault()).trim()
                contactsFull?.forEach {
                    if (anyMatches(it, filterPattern)) {
                        filteredContacts.add(it)
                    }
                }
            }

            return FilterResults().apply {
                values = filteredContacts
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            contacts.clear()
            contacts.addAll((results?.values) as List<Contact>)
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

}