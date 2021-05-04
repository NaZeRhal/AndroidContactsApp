package com.maxrzhe.contacts.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maxrzhe.contacts.R
import com.maxrzhe.contacts.repository.Repository
import com.maxrzhe.contacts.repository.RepositoryFactory
import com.maxrzhe.contacts.repository.RepositoryType
import com.maxrzhe.core.model.Contact
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ContactDetailViewModel(private val app: Application) :
    com.maxrzhe.core.viewmodel.BaseViewModel(app) {

    private val repository: Repository = RepositoryFactory.create(app, RepositoryType.PLAIN_SQL)
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    private var _savedMarker = MutableLiveData(false)
    val savedMarker: LiveData<Boolean> = _savedMarker

    private var calendar = Calendar.getInstance()

    private var id: Long? = null
    private var isFavorite = ObservableBoolean(false)

    val name = ObservableField<String?>()
    val email = ObservableField<String?>()
    val phone = ObservableField<String?>()
    val image = ObservableField<String?>()
    val date = ObservableField<String?>()
    val isLoading = ObservableBoolean(true)

    val year = ObservableInt()
    val month = ObservableInt()
    val day = ObservableInt()

    val imageTextRes = ObservableInt(R.string.detail_tv_add_image_text)
    val buttonTextRes = ObservableInt(R.string.detail_button_add_text)
    val tint = ObservableInt(R.color.favorite_false_color)

    fun manageSelectedId(selectedId: Long?) {
        isLoading.set(true)
        id = selectedId
        viewModelScope.launch {
            val contact =
                if (selectedId != null) repository.findById(selectedId) else Contact.New()
            setupFields(contact)
            isLoading.set(false)
        }
    }

    private fun setupFields(contact: Contact?) {
        if (contact == null || contact is Contact.New) {
            name.set("")
            email.set("")
            phone.set("")
            image.set("")
            date.set("")
            isFavorite.set(false)
            toggleTint()
            imageTextRes.set(R.string.detail_tv_add_image_text)
            buttonTextRes.set(R.string.detail_button_add_text)
        } else {
            name.set(contact.name)
            email.set(contact.email)
            phone.set(contact.phone)
            image.set(contact.image)
            date.set(contact.birthDate)
            isFavorite.set(contact.isFavorite)
            parseDate(contact.birthDate)
            toggleTint()
            imageTextRes.set(R.string.detail_tv_change_image_text)
            buttonTextRes.set(R.string.detail_button_save_changes_text)
        }
    }

    fun resetMarker() {
        _savedMarker.value = false
    }

    fun manageImageUri(imageUri: String) {
        image.set(imageUri)
        imageTextRes.set(R.string.detail_tv_change_image_text)
    }

    fun onChangeFavorite() {
        isFavorite.set(!isFavorite.get())
        toggleTint()
    }

    private fun toggleTint() {
        if (!isFavorite.get()) {
            tint.set(R.color.favorite_false_color)
        } else {
            tint.set(R.color.favorite_true_color)
        }
    }

    private fun add(contact: Contact.New) {
        viewModelScope.launch {
            repository.add(contact)
        }
    }

    private fun update(contact: Contact.Existing) {
        viewModelScope.launch {
            repository.update(contact)
        }
    }

    fun onDateChanged(year: Int, month: Int, day: Int) {
        calendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }
        updateDate(calendar.time)
    }

    private fun updateDate(time: Date) {
        date.set(sdf.format(time).toString())
    }

    private fun parseDate(date: String) {
        val birthday = sdf.parse(date)
        birthday?.let {
            calendar.time = it
            year.set(calendar.get(Calendar.YEAR))
            month.set(calendar.get(Calendar.MONTH))
            day.set(calendar.get(Calendar.DAY_OF_MONTH))
        }
    }

    fun addOrUpdate() {
        if (validateInput()) {
            if (id != null) {
                id?.let {
                    val contact = Contact.Existing(
                        id = it,
                        name = name.get() ?: "",
                        phone = phone.get() ?: "",
                        email = email.get() ?: "",
                        image = image.get() ?: "",
                        birthDate = date.get() ?: "",
                        isFavorite = isFavorite.get()
                    )
                    update(contact)
                }
            } else {
                val contact =
                    Contact.New(
                        name = name.get() ?: "",
                        phone = phone.get() ?: "",
                        email = email.get() ?: "",
                        image = image.get() ?: "",
                        birthDate = date.get() ?: "",
                        isFavorite = isFavorite.get()
                    )
                add(contact)
            }
            _savedMarker.value = true
        }
    }

    private fun validateInput(): Boolean {
        return when {
            name.get().isNullOrEmpty() -> {
                android.widget.Toast.makeText(
                    app.applicationContext,
                    "Please enter a name",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                false
            }
            !android.util.Patterns.PHONE.matcher(phone.get().toString()).matches() -> {
                android.widget.Toast.makeText(
                    app.applicationContext,
                    "Please enter correct phone number",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email.get().toString()).matches() -> {
                android.widget.Toast.makeText(
                    app.applicationContext,
                    "Please enter correct email",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> true
        }
    }
}