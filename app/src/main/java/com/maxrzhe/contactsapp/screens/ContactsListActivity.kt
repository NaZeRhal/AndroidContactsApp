package com.maxrzhe.contactsapp.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.maxrzhe.contactsapp.databinding.ActivityListContactsBinding

class ContactsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListContactsBinding

    private var isLandscape: Boolean = false

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


        val detailsFragment =
            supportFragmentManager.findFragmentByTag(ContactDetailFragment.DETAILS_FRAGMENT_TAG_KEY)
        when {
            savedInstanceState == null && !isLandscape -> {
                supportFragmentManager.commit {
                    add(binding.flContainer.id, ContactListFragment())
                    setReorderingAllowed(true)
                }
            }
            isLandscape -> {
                supportFragmentManager.apply {
                    popBackStackImmediate()
                    val listFragment = ContactListFragment().apply {
                        arguments = Bundle().apply {
                            putBoolean(IS_LANDSCAPE, isLandscape)
                        }
                    }

                    commit {
                        replace(binding.flContainer.id, listFragment)
                        binding.flDetails?.let {
                            if (detailsFragment != null) {
                                detailsFragment.apply {
                                    arguments = Bundle().apply {
                                        putBoolean(IS_LANDSCAPE, isLandscape)
                                    }
                                }
                                replace(it.id, detailsFragment)
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
                        replace(binding.flContainer.id, ContactListFragment())
                        setReorderingAllowed(true)
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
//
//    override fun onResume() {
//        super.onResume()
//        supportFragmentManager.apply {
//            val f1 = findFragmentByTag(ContactListFragment.CONTACTS_FRAGMENT_TAG_KEY)
//            val f2 = findFragmentByTag(ContactDetailFragment.DETAILS_FRAGMENT_TAG_KEY)
//            Log.i(TAG, "ContactListFragment not null=${f1 != null}")
//            Log.i(TAG, "ContactDetailFragment not null=${f2 != null}")
//        }
//
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.i(TAG, "onPause: activity")
//    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop: activity")
        unregisterReceiver(batteryBroadcastReceiver)
        unregisterReceiver(wifiBroadcastReceiver)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        Log.i(TAG, "onDestroy: activity")
//    }

    companion object {
        const val SHARED_STORAGE_NAME = "storage"
        const val CONTACT_LIST = "contact_list"
        const val IS_LANDSCAPE = "is_landscape"
        const val TAG = "LIFE_CYCLE"
    }
}

