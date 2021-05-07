package com.maxrzhe.contacts.viewmodel

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Application
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maxrzhe.contacts.R
import com.maxrzhe.contacts.repository.ContactRepoWithRestApi
import com.maxrzhe.contacts.repository.Repository
import com.maxrzhe.contacts.repository.RepositoryType
import com.maxrzhe.core.model.Contact
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ContactDetailViewModel(private val app: Application) :
    com.maxrzhe.core.viewmodel.BaseViewModel(app) {

    private val repository: Repository =
        ContactRepoWithRestApi.getInstance(app, RepositoryType.PLAIN_SQL)
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    private var _savedMarker = MutableLiveData(false)
    val savedMarker: LiveData<Boolean> = _savedMarker

    private var calendar = Calendar.getInstance()

    private var id: Long? = null
    private var isFavorite = false
    private var fbId: String = ""

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
    val tint = ObservableInt(ContextCompat.getColor(app, R.color.favorite_false_color))

    fun manageSelectedId(selectedId: Long?) {
        isLoading.set(true)
        id = selectedId
        viewModelScope.launch {
            try {
                val contact =
                    if (selectedId != null) repository.findById(fbId) else Contact.New()
                setupFields(contact)
            } catch (e: Exception) {
                Log.i("RE_SP", "manageSelectedId: $e")
            } finally {
                isLoading.set(false)
            }
        }
    }

    private fun setupFields(contact: Contact?) {
        if (contact == null || contact is Contact.New) {
            name.set("")
            email.set("")
            phone.set("")
            image.set("")
            date.set("")
            fbId = ""
            isFavorite = false
            toggleTint()
            imageTextRes.set(R.string.detail_tv_add_image_text)
            buttonTextRes.set(R.string.detail_button_add_text)
        } else {
            name.set(contact.name)
            email.set(contact.email)
            phone.set(contact.phone)
            image.set(contact.image)
            date.set(contact.birthDate)
            isFavorite = contact.isFavorite
            fbId = contact.fbId
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

    fun onChangeFavorite(view: View) {
        isFavorite = !isFavorite
        animateIcon(view)
        toggleTint()
    }

    private fun animateIcon(view: View) {
        val fadeOut = ObjectAnimator.ofFloat(view, "alpha", 0f).apply {
            duration = 150
        }
        val fadeIn = ObjectAnimator.ofFloat(view, "alpha", 1f).apply {
            duration = 150
        }

        val scaleXIn = ObjectAnimator.ofFloat(view, "scaleX", 1.2f).apply {
            duration = 150
        }
        val scaleYIn = ObjectAnimator.ofFloat(view, "scaleY", 1.2f).apply {
            duration = 150
        }
        val scaleIn = AnimatorSet().apply {
            play(scaleXIn).with(scaleYIn).with(fadeOut)
        }
        val scaleXOut = ObjectAnimator.ofFloat(view, "scaleX", 1f).apply {
            duration = 150
        }
        val scaleYOut = ObjectAnimator.ofFloat(view, "scaleY", 1f).apply {
            duration = 150
        }
        val scaleOut = AnimatorSet().apply {
            play(scaleXOut).with(scaleYOut).with(fadeIn)
        }
        AnimatorSet().apply {
            play(scaleIn).before(scaleOut)
            start()
        }
    }

    private fun toggleTint() {
        if (!isFavorite) {
            tint.set(ContextCompat.getColor(app, R.color.favorite_false_color))
        } else {
            tint.set(ContextCompat.getColor(app, R.color.favorite_true_color))
        }
    }

    private fun add(contact: Contact.New) {
        viewModelScope.launch {
            try {
                repository.add(contact)
            } catch (e: Exception) {
                Log.i("RE_SP", "add: ${e.message}")
            }
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
                        fbId = fbId,
                        name = name.get() ?: "",
                        phone = phone.get() ?: "",
                        email = email.get() ?: "",
                        image = image.get() ?: "",
                        birthDate = date.get() ?: "",
                        isFavorite = isFavorite
                    )
                    update(contact)
                }
            } else {
                val contact =
                    Contact.New(
                        fbId = fbId,
                        name = name.get() ?: "",
                        phone = phone.get() ?: "",
                        email = email.get() ?: "",
                        image = image.get() ?: "",
                        birthDate = date.get() ?: "",
                        isFavorite = isFavorite
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