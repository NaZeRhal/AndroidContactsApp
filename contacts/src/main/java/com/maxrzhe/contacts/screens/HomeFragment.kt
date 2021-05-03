package com.maxrzhe.contacts.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager
import com.maxrzhe.contacts.adapters.ContactViewPagerAdapter
import com.maxrzhe.contacts.databinding.FragmentHomeBinding
import com.maxrzhe.contacts.viewmodel.SharedViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    var viewPager: ViewPager? = null

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
        val view = binding?.root
        viewPager = binding?.vpHome
        val adapter = ContactViewPagerAdapter(childFragmentManager)
        viewPager?.let {
            it.adapter = adapter
            it.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    onChangeCurrentPositionListener?.onChange(position)
                }
            })
        }

        binding?.fabAdd?.setOnClickListener(addContact())
        return view
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