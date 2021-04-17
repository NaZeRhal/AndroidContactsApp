package com.maxrzhe.contactsapp.adapters

import android.net.Uri
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.maxrzhe.contactsapp.R

@BindingAdapter("app:imageUri")
fun imageUri(view: ImageView, imageUriString: String?) {
    if (imageUriString == null || imageUriString.isEmpty()) {
        view.setImageResource(R.drawable.person_placeholder)
    } else {
        view.setImageURI(Uri.parse(imageUriString))
    }
}

@BindingAdapter("app:addOrChangeImageText")
fun addOrChangeImageText(v: TextView, isChange: Boolean) {
    if (!isChange) {
        v.text = v.context.getString(R.string.detail_tv_add_image_text)
    } else {
        v.text = v.context.getString(R.string.detail_tv_change_image_text)
    }
}

@BindingAdapter("app:addOrChangeButtonText")
fun addOrChangeButtonText(v: Button, isChange: Boolean) {
    if (!isChange) {
        v.text = v.context.getString(R.string.detail_button_add_text)
    } else {
        v.text = v.context.getString(R.string.detail_button_save_changes_text)
    }
}