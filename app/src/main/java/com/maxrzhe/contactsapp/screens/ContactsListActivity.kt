package com.maxrzhe.contactsapp.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maxrzhe.contactsapp.R
import com.maxrzhe.contactsapp.adapters.ContactAdapter
import com.maxrzhe.contactsapp.databinding.ActivityListContactsBinding
import com.maxrzhe.contactsapp.model.Contact

class ContactsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListContactsBinding

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

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(binding.flContainer.id, ContactListFragment())
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    companion object {
        const val SHARED_STORAGE_NAME = "storage"
        const val CONTACT_LIST = "contact_list"
    }
}

