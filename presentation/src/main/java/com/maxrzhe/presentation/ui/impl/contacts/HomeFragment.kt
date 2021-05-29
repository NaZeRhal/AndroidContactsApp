package com.maxrzhe.presentation.ui.impl.contacts

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.adapters.ContactViewPagerAdapter
import com.maxrzhe.presentation.adapters.ZoomOutPageTransformer
import com.maxrzhe.presentation.databinding.FragmentHomeBinding
import com.maxrzhe.presentation.ui.SettingsActivity
import com.maxrzhe.presentation.ui.base.BaseFragmentWithViewModel
import com.maxrzhe.presentation.viewmodel.impl.contacts.HomeFragmentViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragmentWithViewModel<FragmentHomeBinding, HomeFragmentViewModel>() {

    private val onChangeCurrentPositionListener: OnChangeCurrentPositionListener?
        get() = (context as? OnChangeCurrentPositionListener)

    override val viewModel: HomeFragmentViewModel by viewModel()

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
                R.id.bottom_menu_settings ->
                    startActivity(
                        Intent(
                            requireContext(),
                            SettingsActivity::class.java
                        )
                    )
            }
            true
        }
    }

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
        } else {
            @Suppress("DEPRECATION") 
            requireActivity().window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        }
    }


    override fun bindView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
    }

    override fun onReturnToPreviousScreen() {
        requireActivity().finish()
    }


    interface OnChangeCurrentPositionListener {
        fun onChange(position: Int)
    }
}