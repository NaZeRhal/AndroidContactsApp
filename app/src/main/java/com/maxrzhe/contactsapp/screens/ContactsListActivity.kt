package com.maxrzhe.contactsapp.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.maxrzhe.contactsapp.R
import com.maxrzhe.contactsapp.databinding.ActivityListContactsBinding
import com.maxrzhe.contactsapp.model.Contact

class ContactsListActivity : AppCompatActivity(), ContactDetailFragment.OnSaveContactListener,
    ContactListFragment.OnAddDetailListener {
    private lateinit var binding: ActivityListContactsBinding

    private var isLandscape: Boolean = false
//    private var contactListFragment: ContactListFragment? = null

    private var toolbar: ActionBar? = null
    private var menuItemSearch: MenuItem? = null

    companion object {
        private const val DETAILS_TAG = "storage"
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListContactsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.tbMain)
        title = null

        toolbar = supportActionBar

        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


        val detailFragment =
            supportFragmentManager.findFragmentByTag(DETAILS_TAG) as? ContactDetailFragment
        val contactListFragment =
            supportFragmentManager.findFragmentByTag(LIST_TAG) as? ContactListFragment
                ?: ContactListFragment()

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.fl_container, contactListFragment)
            }
        }

        when {
            isLandscape -> {
                supportFragmentManager.apply {
                    popBackStackImmediate()
                    commit {
                        replace(R.id.fl_container, contactListFragment, LIST_TAG)

                        detailFragment?.let {
                            replace(R.id.fl_details, it, DETAILS_TAG)
                        }
                        setReorderingAllowed(true)
                    }
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

                    override fun onQueryTextChange(newText: String?): Boolean {
                        val contactListFragment =
                            supportFragmentManager.findFragmentByTag(LIST_TAG) as? ContactListFragment
                                ?: ContactListFragment()
                        contactListFragment.filter(newText)
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
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onStart() {
        super.onStart()
        registerReceiver(batteryBroadcastReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        registerReceiver(wifiBroadcastReceiver, IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(batteryBroadcastReceiver)
        unregisterReceiver(wifiBroadcastReceiver)
    }

    override fun onSave(contact: Contact) {
        if (!isLandscape) {
            menuItemSearch?.isVisible = true
            toolbar?.setDisplayHomeAsUpEnabled(false)
        }

        val contactListFragment =
            supportFragmentManager.findFragmentByTag(LIST_TAG) as? ContactListFragment
                ?: ContactListFragment()
        contactListFragment.saveContact(contact)
        if (!isLandscape) {
            supportFragmentManager.popBackStackImmediate()
        }
        Toast.makeText(this, "Contact saved", Toast.LENGTH_SHORT).show()
    }

    override fun onAddDetails(contact: Contact?) {
        if (!isLandscape) {
            menuItemSearch?.isVisible = false
            toolbar?.setDisplayHomeAsUpEnabled(true)
        }

        val detailFragment = ContactDetailFragment.newInstance(contact)
        supportFragmentManager.commit {
            if (isLandscape) {
                replace(R.id.fl_details, detailFragment, DETAILS_TAG)
            } else {
                add(R.id.fl_container, detailFragment, DETAILS_TAG)
                addToBackStack(null)
            }
            setReorderingAllowed(true)
        }
    }
}

