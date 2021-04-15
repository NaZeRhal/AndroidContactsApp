package com.maxrzhe.contactsapp.adapters

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.maxrzhe.contactsapp.R

@BindingAdapter("app:imageUri")
fun imageUri(view: ImageView, imageUriString: String?) {
    if (imageUriString == null) {
        view.setImageResource(R.drawable.person_placeholder)
    } else {
        view.setImageURI(Uri.parse(imageUriString))
    }
}