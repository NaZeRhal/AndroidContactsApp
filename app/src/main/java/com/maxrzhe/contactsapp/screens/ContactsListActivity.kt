package com.maxrzhe.contactsapp.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.maxrzhe.contactsapp.databinding.ActivityListContactsBinding
import com.maxrzhe.contactsapp.model.Contact

class ContactsListActivity : AppCompatActivity(), ContactDetailFragment.OnSaveContactListener {
    private lateinit var binding: ActivityListContactsBinding

    private var isLandscape: Boolean = false
    private var contactListFragment: ContactListFragment? = null
    private var detailFragment: ContactDetailFragment? = null

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

        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


        supportFragmentManager.apply {
            detailFragment =
                supportFragmentManager.findFragmentByTag(ContactDetailFragment.DETAILS_FRAGMENT_TAG_KEY) as? ContactDetailFragment
            detailFragment?.apply {
                arguments = Bundle().apply {
                    putBoolean(IS_LANDSCAPE, isLandscape)
                }
            }
            contactListFragment =
                supportFragmentManager.findFragmentByTag(ContactListFragment.CONTACTS_FRAGMENT_TAG_KEY) as? ContactListFragment
                    ?: ContactListFragment()
            contactListFragment?.apply {
                arguments = Bundle().apply {
                    putBoolean(IS_LANDSCAPE, isLandscape)
                }
            }
        }

        when {
            savedInstanceState == null && !isLandscape -> {
                supportFragmentManager.commit {
                    contactListFragment?.let {
                        add(
                            binding.flContainer.id,
                            it,
                            ContactListFragment.CONTACTS_FRAGMENT_TAG_KEY
                        )
                        setReorderingAllowed(true)
                    }
                }
            }
            isLandscape -> {
                supportFragmentManager.apply {
                    popBackStackImmediate()
                    commit {
                        contactListFragment?.let {
                            replace(
                                binding.flContainer.id,
                                it,
                                ContactListFragment.CONTACTS_FRAGMENT_TAG_KEY
                            )
                        }

                        binding.flDetails?.let { container ->
                            detailFragment?.let { fragment ->
                                replace(container.id, fragment)
                            }
                        }
                        setReorderingAllowed(true)
                    }
                }
            }
            else -> {
                supportFragmentManager.apply {
                    popBackStackImmediate()
                    commit {
                        contactListFragment = ContactListFragment()
                        contactListFragment?.let {
                            replace(
                                binding.flContainer.id,
                                it,
                                ContactListFragment.CONTACTS_FRAGMENT_TAG_KEY
                            )
                            setReorderingAllowed(true)
                        }
                    }
                }
            }
        }
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

    companion object {
        const val SHARED_STORAGE_NAME = "storage"
        const val CONTACT_LIST = "contact_list"
        const val IS_LANDSCAPE = "is_landscape"
    }

    override fun onSave(contact: Contact) {
        contactListFragment?.saveContact(contact)
    }
}

