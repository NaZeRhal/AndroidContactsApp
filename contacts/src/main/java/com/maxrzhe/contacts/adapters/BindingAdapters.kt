package com.maxrzhe.contacts.adapters

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.maxrzhe.contacts.R
import java.io.File

@BindingAdapter("imageUri")
fun imageUri(view: ImageView, imageUriString: String?) {
    if (imageUriString == null || imageUriString.isEmpty()) {
        view.setImageResource(R.drawable.person_placeholder)
    } else {
        view.setImageURI(Uri.fromFile(File(imageUriString)))
    }
}

@BindingAdapter("toggleVisibility")
fun toggleVisibility(v: View, isVisible: Boolean) {
    v.visibility = if (isVisible) View.VISIBLE else View.GONE
}