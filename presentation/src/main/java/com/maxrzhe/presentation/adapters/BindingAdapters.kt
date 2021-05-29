package com.maxrzhe.presentation.adapters

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.model.BaseItemViewModel
import com.maxrzhe.presentation.util.ClickListener
import com.maxrzhe.volumeslider.ui.VolumeSlider
import java.io.File

@BindingConversion
fun convertLambdaToClickListener(clickListener: ClickListener?): View.OnClickListener? =
    if (clickListener != null) {
        View.OnClickListener { clickListener() }
    } else {
        null
    }

@BindingAdapter("imageUriFromFile")
fun imageUriFromFile(view: ImageView, imageUriString: String?) {
    if (imageUriString == null || imageUriString.isEmpty()) {
        view.setImageResource(R.drawable.person_placeholder)
    } else {
        view.setImageURI(Uri.fromFile(File(imageUriString)))
    }
}

@BindingAdapter("srcDrawable")
fun srcDrawable(view: ImageView, @DrawableRes res: Int?) {
    res?.let {
        view.setImageResource(it)
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
fun setData(rv: RecyclerView, itemsList: List<BaseItemViewModel>?) {
    if (rv.adapter is BaseBindingAdapter<*>) {
        itemsList?.let {
            (rv.adapter as BaseBindingAdapter<*>).setItems(it)
        }
    }
}

@BindingAdapter("setTextOrHide")
fun TextView.setTextOrHide(text: String?) {
    if (text != null) {
        this.visibility = View.VISIBLE
        this.text = text
    } else {
        this.visibility = View.GONE
        this.text = ""
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

