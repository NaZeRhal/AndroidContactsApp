package com.maxrzhe.presentation.adapters

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.model.ContactItemViewModel
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
fun RecyclerView.bindAdapter(baseAdapter: BaseBindingAdapter<*>) {
    this.run {
        hasFixedSize()
        layoutManager = LinearLayoutManager(this.context)
        adapter = baseAdapter
    }
}

@BindingAdapter("data")
fun setData(rv: RecyclerView, contactsList: List<ContactItemViewModel>?) {
    if (rv.adapter is BaseBindingAdapter<*>) {
        contactsList?.let {
            (rv.adapter as BaseBindingAdapter<*>).setItems(it)
        }
    }
}

@BindingAdapter("searchResult")
fun setSearchResult(v: TextView, resultCount: Int?) {
    if (resultCount != null) {
        if (resultCount >= 0) {
            v.visibility = View.VISIBLE
            val result =
                v.resources.getQuantityString(
                    R.plurals.search_result_plurals,
                    resultCount,
                    resultCount
                )
            v.text = result
        } else {
            v.visibility = View.GONE
            v.text = ""
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

