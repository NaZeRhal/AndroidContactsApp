package com.maxrzhe.presentation.ui.impl

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager
import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.adapters.ContactViewPagerAdapter
import com.maxrzhe.presentation.adapters.ZoomOutPageTransformer
import com.maxrzhe.presentation.databinding.FragmentHomeBinding
import com.maxrzhe.presentation.ui.SettingsActivity
import com.maxrzhe.presentation.ui.base.CoreFragment
import com.maxrzhe.presentation.viewmodel.impl.SharedViewModel

class HomeFragment : CoreFragment<FragmentHomeBinding>() {

    private val onAddContactListener: OnAddContactListener?
        get() = (context as? OnAddContactListener)

    private val onChangeCurrentPositionListener: OnChangeCurrentPositionListener?
        get() = (context as? OnChangeCurrentPositionListener)

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate

    override fun initView() {
        val bottomNavigationView = binding.bnvMain
        val adapter = ContactViewPagerAdapter(childFragmentManager)
        val viewPager = binding.vpHome
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())

        viewPager.let {
            it.adapter = adapter
            it.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    bottomNavigationView.menu.getItem(position)?.isChecked = true
                    onChangeCurrentPositionListener?.onChange(position)
                }
            })
        }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_menu_home -> {
                    viewPager.currentItem = 0
                }
                R.id.bottom_menu_fav -> {
                    viewPager.currentItem = 1
                }
                R.id.bottom_menu_settings -> startActivity(
                    Intent(
                        requireContext(),
                        SettingsActivity::class.java
                    )
                )
            }
            true
        }

        binding.fabAdd.setOnClickListener(addContact())
    }

    override fun bindView() {}

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