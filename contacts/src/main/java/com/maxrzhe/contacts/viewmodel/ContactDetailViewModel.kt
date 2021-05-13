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
import androidx.lifecycle.*
import com.maxrzhe.contacts.R
import com.maxrzhe.contacts.remote.Resource
import com.maxrzhe.contacts.repository.ContactRepository
import com.maxrzhe.core.model.Contact
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ContactDetailViewModel(private val app: Application) :
    com.maxrzhe.core.viewmodel.BaseViewModel(app) {

    private val mainRepo = ContactRepository.getInstance(app)
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    private var _savedMarker = MutableLiveData(false)
    val savedMarker: LiveData<Boolean> = _savedMarker

    private var calendar = Calendar.getInstance()
    private var isFavorite = false
    private var _fbId = MutableLiveData<String?>(null)

    private val _contact: LiveData<Resource<Contact>> =
        _fbId.distinctUntilChanged().switchMap { id ->
            liveData {
                if (id != null) {
                    mainRepo.findById(id)
                        .collect { emit(it) }
                } else {
                    emit(Resource.Success(null))
                }
            }
        }
    val contact: LiveData<Resource<Contact>> = _contact

    private val _errorMessage: MutableLiveData<String?> =
        MutableLiveData(null)
    val errorMessage: LiveData<String?> = _errorMessage

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

    fun setSelectedId(selectedId: String?) {
        _fbId.value = selectedId
    }

    fun setupFields(contact: Contact?) {
        if (contact == null) {
            name.set("")
            email.set("")
            phone.set("")
            image.set("")
            date.set("")
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

    private fun add(contact: Contact) {
        viewModelScope.launch {
            mainRepo.add(contact)
                .collect {
                    if (it is Resource.Success) {
                        _savedMarker.value = true
                    } else {
                        _errorMessage.value = it.error?.message
                    }
                }
        }
    }

    private fun update(contact: Contact) {
        viewModelScope.launch {
            mainRepo.update(contact)
                .collect {
                    if (it is Resource.Success) {
                        _savedMarker.value = true
                    } else {
                        _errorMessage.value = it.error?.message
                    }
                }
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
            val contact = Contact(
                fbId = _fbId.value ?: "",
                name = name.get() ?: "",
                phone = phone.get() ?: "",
                email = email.get() ?: "",
                image = image.get() ?: "",
                birthDate = date.get() ?: "",
                isFavorite = isFavorite
            )
            if (_fbId.value != null) {
                update(contact)
            } else {
                add(contact)
            }
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