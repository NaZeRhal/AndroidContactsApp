package com.maxrzhe.presentation.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.databinding.ActivityListContactsBinding
import com.maxrzhe.presentation.ui.impl.ContactDetailFragment
import com.maxrzhe.presentation.ui.impl.ContactDetailFragment.*
import com.maxrzhe.presentation.ui.impl.ContactListFragment.*
import com.maxrzhe.presentation.viewmodel.impl.SearchViewModel

class ContactsListActivity : AppCompatActivity(), OnSaveContactListener,
    OnSelectContactListener, OnTakeImageListener, HomeFragment.OnAddContactListener,
    HomeFragment.OnChangeCurrentPositionListener {
    private lateinit var binding: ActivityListContactsBinding

    private val searchViewModel by viewModels<SearchViewModel>()

    private var isLandscape: Boolean = false
    private var toolbar: ActionBar? = null
    private var menuItemSearch: MenuItem? = null
    private var currentPosition = 0

    companion object {
        private const val DETAILS_TAG = "storage"
        private const val HOME_TAG = "contacts_home"
        private const val CURRENT_POSITION = "current_position"
    }

    private val batteryBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val level: Int = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale: Int = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            val percentage: Float = level * 100 / scale.toFloat()

            if (level > 0 && scale > 0) {
                title = "Contacts ${percentage.toInt()}%"
            }
        }
    }

    private val wifiBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val wifiState =
                intent?.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
            if (wifiState == WifiManager.WIFI_STATE_DISABLING) {
                Toast.makeText(context, "WiFi disconnected. Please turn on WiFi", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private val permissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val isAllowed = permissions.entries.all { it.value != false }
            if (isAllowed) {
                choosePhotoFromGallery()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private val imageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    val contentUri = it.data as Uri
                    val detailFragment =
                        supportFragmentManager.findFragmentByTag(DETAILS_TAG) as? ContactDetailFragment
                    detailFragment?.setupImage(contentUri)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListContactsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.tbMain)
        title = null
        toolbar = supportActionBar

        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        val homeFragment =
            supportFragmentManager.findFragmentByTag(HOME_TAG) as? HomeFragment ?: HomeFragment()
        val detailFragment =
            supportFragmentManager.findFragmentByTag(DETAILS_TAG) as? ContactDetailFragment

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.fl_container, homeFragment, HOME_TAG)
            }
        } else {
            currentPosition = savedInstanceState.getInt(CURRENT_POSITION)
        }

        if (isLandscape) {
            supportFragmentManager.apply {
                popBackStackImmediate()
                commit {
                    replace(R.id.fl_container, homeFragment, HOME_TAG)

                    detailFragment?.let {
                        replace(R.id.fl_details, it, DETAILS_TAG)
                    }
                    setReorderingAllowed(true)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchView = (menu?.findItem(R.id.menu_item_search))?.actionView as SearchView
        val searchEditText = searchView.findViewById<EditText>(R.id.search_src_text)

        menuItemSearch = menu.findItem(R.id.menu_item_search)

        searchEditText.apply {
            setTextColor(
                ContextCompat.getColor(
                    this@ContactsListActivity,
                    R.color.search_view_text_color
                )
            )

            setHintTextColor(
                ContextCompat.getColor(
                    this@ContactsListActivity,
                    R.color.search_view_hint_color
                )
            )
        }

        searchView.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            queryHint = resources.getString(R.string.toolbar_search_hint)

            setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        searchViewModel.setQuery(newText)
                        return true
                    }
                })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                menuItemSearch?.isVisible = true
                toolbar?.setDisplayHomeAsUpEnabled(false)
                onBackPressed()
//                binding.tvSearchResult?.visibility = View.VISIBLE
            }
            R.id.menu_item_volume -> {
                startActivity(Intent(this, VolumeSettingActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt(CURRENT_POSITION, currentPosition)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(batteryBroadcastReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        registerReceiver(wifiBroadcastReceiver, IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(batteryBroadcastReceiver)
        unregisterReceiver(wifiBroadcastReceiver)
    }

    override fun onSave() {
        if (!isLandscape) {
            menuItemSearch?.isVisible = true
            toolbar?.setDisplayHomeAsUpEnabled(false)
        }

        if (!isLandscape) {
            supportFragmentManager.popBackStackImmediate()
        }
        Toast.makeText(this, "Contact saved", Toast.LENGTH_SHORT).show()
    }

    override fun onAdd() {
        onSelect()
    }

    override fun onSelect() {
        if (!isLandscape) {
            menuItemSearch?.isVisible = false
            toolbar?.setDisplayHomeAsUpEnabled(true)
        }

        val detailFragment = ContactDetailFragment()
        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            if (isLandscape) {
                replace(R.id.fl_details, detailFragment, DETAILS_TAG)
            } else {
                add(R.id.fl_container, detailFragment, DETAILS_TAG)
                addToBackStack(null)
            }
            setReorderingAllowed(true)
        }
    }

    private fun checkForStoragePermission() {
        val storagePermissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    storagePermissions[0]
                ) + ContextCompat.checkSelfPermission(
                    this,
                    storagePermissions[1]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (shouldShowRequestPermissionRationale(storagePermissions[0]) ||
                    shouldShowRequestPermissionRationale(storagePermissions[1])
                ) {
                    showDialog(storagePermissions)
                } else {
                    permissionRequestLauncher.launch(storagePermissions)
                }
            } else {
                choosePhotoFromGallery()
            }
        }
    }

    private fun showDialog(permissions: Array<String>) {
        AlertDialog.Builder(this).apply {
            setMessage("Permission to access your STORAGE is required to use this app")
            setTitle("Permission required")
            setPositiveButton("OK") { _, _ ->
                permissionRequestLauncher.launch(permissions)
            }
        }
            .show()
    }

    private fun choosePhotoFromGallery() {
        val galleryIntent =
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        imageResultLauncher.launch(galleryIntent)
    }

    override fun onTakeImage() {
        checkForStoragePermission()
    }

    override fun onChange(position: Int) {
        currentPosition = position
    }
}