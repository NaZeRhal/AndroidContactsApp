package com.maxrzhe.contactsapp.adapters

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.maxrzhe.contactsapp.R
import com.maxrzhe.contactsapp.model.Contact

@BindingAdapter("app:imageUri")
fun imageUri(view: ImageView, imageUriString: String?) {
    if (imageUriString == null) {
        view.setImageResource(R.drawable.person_placeholder)
    } else {
        view.setImageURI(Uri.parse(imageUriString))
    }
}

@BindingAdapter(
    "app:nullableField",
    "app:textChoiceNull",
    "app:textChoiceNotNull",
    requireAll = true
)
fun TextView.textChoice(
    nullableField: Contact?,
    textChoiceNull: String,
    textChoiceNotNull: String,
) {
    if (nullableField == null) {
        this.text = textChoiceNull
    } else {
        this.text = textChoiceNotNull
    }
}