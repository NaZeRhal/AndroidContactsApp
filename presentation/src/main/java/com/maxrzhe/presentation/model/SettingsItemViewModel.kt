package com.maxrzhe.presentation.model

import androidx.annotation.DrawableRes
import com.maxrzhe.presentation.R

class SettingsItemViewModel(
    @DrawableRes val iconId: Int,
    val pathId: Int,
    val title: String
) : BaseItemViewModel() {


    override val id: Long get() = this.iconId.hashCode().toLong()
    override val layoutId: Int get() = R.layout.item_settings

    override fun isTheSameItem(otherItemViewModel: BaseItemViewModel): Boolean =
        (otherItemViewModel is SettingsItemViewModel) && this.id == otherItemViewModel.id

    override fun hasTheSameContent(otherItemViewModel: BaseItemViewModel): Boolean =
        (otherItemViewModel is SettingsItemViewModel) && this.iconId == otherItemViewModel.iconId &&
                this.title == otherItemViewModel.title

}