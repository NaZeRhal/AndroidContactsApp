package com.maxrzhe.presentation.viewmodel.impl.contacts

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import com.example.data_api.model.Contact
import com.maxrzhe.common.util.Resource
import com.maxrzhe.domain.usecases.AddContactUseCase
import com.maxrzhe.domain.usecases.FindByIdUseCase
import com.maxrzhe.domain.usecases.UpdateContactUseCase
import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.navigation.RouteBack
import com.maxrzhe.presentation.navigation.Router
import com.maxrzhe.presentation.util.AppResources
import com.maxrzhe.presentation.viewmodel.base.ViewModelWithRouter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ContactDetailViewModel internal constructor(
    private val fbId: String?,
    private val appResources: AppResources,
    private val findByIdUseCase: FindByIdUseCase,
    private val addContactUseCase: AddContactUseCase,
    private val updateContactUseCase: UpdateContactUseCase,
    router: Router
) :
    ViewModelWithRouter(router) {

    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    private var calendar = Calendar.getInstance()
    private var isFavorite = false
    private val _fbId: MutableLiveData<String?> by lazy { MutableLiveData(fbId) }

    val isLoading = _fbId.distinctUntilChanged().switchMap { id ->
        liveData {
            emit(true)
            if (id != null) {
                val flow: Flow<Resource<Contact>> = findByIdUseCase.execute(id)
                flow.collect {
                    when (it) {
                        is Resource.Success -> setupFields(it.data)
                        is Resource.Error -> _errorMessage.value = it.error.message
                        is Resource.Loading -> emit(true)
                    }
                    emit(false)
                }
            } else {
                setupFields(null)
                emit(false)
            }
        }
    }

    private val _errorMessage: MutableLiveData<String?> =
        MutableLiveData(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _validationMessage: MutableLiveData<String?> =
        MutableLiveData(null)
    val validationMessage: LiveData<String?> = _validationMessage

    val name = ObservableField<String?>()
    val email = ObservableField<String?>()
    val phone = ObservableField<String?>()
    val image = ObservableField<String?>()
    val date = ObservableField<String?>()

    val year = ObservableInt()
    val month = ObservableInt()
    val day = ObservableInt()

    val imageTextRes = ObservableInt(R.string.detail_tv_add_image_text)
    val buttonTextRes = ObservableInt(R.string.detail_button_add_text)
    val tint = ObservableInt(appResources.getColor(R.color.favorite_false_color))

    private fun onSaveItemClickNavigation() {
        router.navigateTo(RouteBack.Back)
    }

    private fun setupFields(contact: Contact?) {
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
            tint.set(appResources.getColor(R.color.favorite_false_color))
        } else {
            tint.set(appResources.getColor(R.color.favorite_true_color))
        }
    }

    private fun add(contact: Contact) {
        viewModelScope.launch {
            addContactUseCase.execute(contact)
                .collect {
                    if (it is Resource.Success) {
                        onSaveItemClickNavigation()
                    } else if (it is Resource.Error) {
                        _errorMessage.value = it.error.message
                        onSaveItemClickNavigation()
                    }
                }
        }
    }

    private fun update(contact: Contact) {
        viewModelScope.launch {
            updateContactUseCase.execute(contact)
                .collect {
                    if (it is Resource.Success) {
                        onSaveItemClickNavigation()
                    } else if (it is Resource.Error) {
                        _errorMessage.value = it.error.message
                        onSaveItemClickNavigation()
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
                _validationMessage.value = "Please enter a name"
                false
            }
            !android.util.Patterns.PHONE.matcher(phone.get().toString()).matches() -> {
                _validationMessage.value = "Please enter correct phone number"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email.get().toString()).matches() -> {
                _validationMessage.value = "Please enter correct email"
                false
            }
            else -> true
        }
    }
}