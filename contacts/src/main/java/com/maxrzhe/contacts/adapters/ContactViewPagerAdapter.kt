package com.maxrzhe.contacts.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.maxrzhe.contacts.screens.ContactListFragment

class ContactViewPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ContactListFragment.createInstance(false)
            1 -> ContactListFragment.createInstance(true)
            else -> throw IllegalArgumentException("No fragment in $position position")
        }
    }
}