package com.maxrzhe.contacts.adapters

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maxrzhe.contacts.R
import com.maxrzhe.core.model.Contact
import com.maxrzhe.volumeslider.ui.VolumeSlider
import java.io.File

@BindingAdapter("imageUri")
fun imageUri(view: ImageView, imageUriString: String?) {
    if (imageUriString == null || imageUriString.isEmpty()) {
        view.setImageResource(R.drawable.person_placeholder)
    } else {
        view.setImageURI(Uri.fromFile(File(imageUriString)))
    }
}

@BindingAdapter("circleImageUri")
fun circleImageUri(view: ImageView, imageUriString: String?) {
    if (imageUriString == null || imageUriString.isEmpty()) {
        Glide.with(view)
            .load(R.drawable.person_placeholder)
            .placeholder(R.drawable.person_placeholder)
            .circleCrop()
            .into(view)
    } else {
        Glide.with(view)
            .load(Uri.fromFile(File(imageUriString)))
            .placeholder(R.drawable.person_placeholder)
            .circleCrop()
            .into(view)
    }
}

@BindingAdapter("bindAdapter")
fun RecyclerView.bindAdapter(contactAdapter: ContactAdapter?) {
    this.run {
        hasFixedSize()
        layoutManager = LinearLayoutManager(this.context)
        contactAdapter?.let {
            adapter = contactAdapter
        }
    }
}

@BindingAdapter("data")
fun setData(rv: RecyclerView, contactsList: List<Contact>?) {
    if (rv.adapter is ContactAdapter) {
        contactsList?.let {
            (rv.adapter as? ContactAdapter)?.itemList = it
        }
    }
}

@BindingAdapter("toggleVisibility")
fun toggleVisibility(v: View, isVisible: Boolean) {
    v.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("tint")
fun setTint(v: ImageView, resId: Int?) {
    if (resId != null) {
        v.setColorFilter(resId)
    }
}

@BindingAdapter("currentValueAttrChanged")
fun setListener(volumeSlider: VolumeSlider, listener: InverseBindingListener) {
    volumeSlider.setSliderRotationListener(object : VolumeSlider.SliderRotationListener {
        override fun onRotate(value: Int) {
            listener.onChange()
        }
    })
}

@BindingAdapter("currentValue")
fun setCurrentValue(volumeSlider: VolumeSlider, currentValue: Int) {
    if (!volumeSlider.isSameValue(currentValue)) {
        volumeSlider.currentValue = currentValue
    }
}

@InverseBindingAdapter(attribute = "currentValue")
fun getCurrentValue(volumeSlider: VolumeSlider): Int {
    return volumeSlider.currentValue
}

