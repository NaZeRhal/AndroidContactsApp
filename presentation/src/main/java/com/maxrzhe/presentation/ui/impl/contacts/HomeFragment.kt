package com.maxrzhe.presentation.ui.impl.contacts

import android.os.Build
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.adapters.ContactViewPagerAdapter
import com.maxrzhe.presentation.adapters.ZoomOutPageTransformer
import com.maxrzhe.presentation.databinding.FragmentHomeBinding
import com.maxrzhe.presentation.ui.base.BaseFragmentWithBindingAndViewModel
import com.maxrzhe.presentation.viewmodel.impl.contacts.HomeFragmentViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment :
    BaseFragmentWithBindingAndViewModel<FragmentHomeBinding, HomeFragmentViewModel>() {

    private val onChangeCurrentPositionListener: OnChangeCurrentPositionListener?
        get() = (context as? OnChangeCurrentPositionListener)

    override val viewModel: HomeFragmentViewModel by viewModel()

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
                R.id.bottom_menu_coronavirus ->
                    viewPager.currentItem = 2
            }
            true
        }
        binding.fabAdd.setOnClickListener {
            viewModel.addContactClick()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_home

    override fun onResume() {
        super.onResume()
        showSystemUI()
    }

    private fun showSystemUI() {
        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.apply {
                setDecorFitsSystemWindows(true)
                insetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            }
        }
    }

    override fun bindView() {}

    override fun onReturnToPreviousScreen() {
        viewModel.onExitApp()
    }

    interface OnChangeCurrentPositionListener {
        fun onChange(position: Int)
    }

    companion object {

        fun createInstance() = HomeFragment()
    }
}