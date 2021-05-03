package com.maxrzhe.contacts.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.maxrzhe.contacts.screens.ContactListFragment

class ContactViewPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var pages: List<Fragment>

    override fun getCount(): Int = pages.size

    override fun getItem(position: Int): Fragment = pages[position]

    init {
        val list = ContactListFragment()
        val favorites = ContactListFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ContactListFragment.IS_FAVORITES, true)
            }
        }
        pages = listOf(list, favorites)
    }
}