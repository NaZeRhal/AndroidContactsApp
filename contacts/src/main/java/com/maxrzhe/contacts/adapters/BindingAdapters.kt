package com.maxrzhe.contacts.adapters

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.maxrzhe.contacts.R
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

