package com.maxrzhe.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.maxrzhe.presentation.ui.impl.contacts.ContactListFragment
import com.maxrzhe.presentation.ui.impl.contacts.CoronavirusFragment

class ContactViewPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ContactListFragment.createInstance(false)
            1 -> ContactListFragment.createInstance(true)
            2 -> CoronavirusFragment.createInstance()
            else -> throw IllegalArgumentException("No fragment in $position position")
        }
    }
}