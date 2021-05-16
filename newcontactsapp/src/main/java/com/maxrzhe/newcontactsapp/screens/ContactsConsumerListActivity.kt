package com.maxrzhe.newcontactsapp.screens

import android.Manifest
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import com.maxrzhe.presentation.viewmodel.SearchViewModel

class ContactsConsumerListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListContactsBinding

    private var isLandscape: Boolean = false
    private var toolbar: ActionBar? = null
    private var menuItemSearch: MenuItem? = null

    private val searchViewModel by viewModels<SearchViewModel>()

    companion object {
        private const val LIST_TAG = "contact_list"
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
                binding.pbMain?.visibility = View.GONE
                binding.flContainer.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
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

        binding.pbMain?.visibility = View.VISIBLE
        binding.flContainer.visibility = View.GONE

        checkForStoragePermission()

        val contactConsumerListFragment =
            supportFragmentManager.findFragmentByTag(LIST_TAG) as? ContactConsumerListFragment
                ?: ContactConsumerListFragment()

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.fl_container, contactConsumerListFragment, LIST_TAG)
            }
        }

        if (isLandscape) {
            supportFragmentManager.apply {
                popBackStackImmediate()
                commit {
                    replace(R.id.fl_container, contactConsumerListFragment, LIST_TAG)
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
                    this@ContactsConsumerListActivity,
                    R.color.search_view_text_color
                )
            )

            setHintTextColor(
                ContextCompat.getColor(
                    this@ContactsConsumerListActivity,
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
        }
        return super.onOptionsItemSelected(item)
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
                binding.pbMain?.visibility = View.GONE
                binding.flContainer.visibility = View.VISIBLE
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
}

