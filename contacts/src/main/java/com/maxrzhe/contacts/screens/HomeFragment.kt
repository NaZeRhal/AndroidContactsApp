package com.maxrzhe.contacts.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager
import com.maxrzhe.contacts.R
import com.maxrzhe.contacts.adapters.ContactViewPagerAdapter
import com.maxrzhe.contacts.adapters.ZoomOutPageTransformer
import com.maxrzhe.contacts.databinding.FragmentHomeBinding
import com.maxrzhe.contacts.viewmodel.SharedViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val onAddContactListener: OnAddContactListener?
        get() = (context as? OnAddContactListener)

    private val onChangeCurrentPositionListener: OnChangeCurrentPositionListener?
        get() = (context as? OnChangeCurrentPositionListener)

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val bottomNavigationView = binding?.bnvMain
        val adapter = ContactViewPagerAdapter(childFragmentManager)
        val viewPager = binding?.vpHome
        viewPager?.setPageTransformer(true, ZoomOutPageTransformer())

        viewPager?.let {
            it.adapter = adapter
            it.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    bottomNavigationView?.menu?.getItem(position)?.isChecked = true
                    onChangeCurrentPositionListener?.onChange(position)
                }
            })
        }

        bottomNavigationView?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_menu_home -> {
                    viewPager?.currentItem = 0
                }
                R.id.bottom_menu_fav -> {
                    viewPager?.currentItem = 1
                }
                else -> {
                }
            }
            true
        }

        binding?.fabAdd?.setOnClickListener(addContact())
        return binding?.root
    }

    private fun addContact() = View.OnClickListener {
        sharedViewModel.select(null)
        onAddContactListener?.onAdd()
    }

    interface OnAddContactListener {
        fun onAdd()
    }

    interface OnChangeCurrentPositionListener {
        fun onChange(position: Int)
    }
}