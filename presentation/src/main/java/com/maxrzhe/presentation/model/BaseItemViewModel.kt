package com.maxrzhe.presentation.model

abstract class BaseItemViewModel {
    abstract val id: Long
    abstract val layoutId: Int
    abstract fun isTheSameItem(otherItemViewModel: BaseItemViewModel): Boolean
    abstract fun hasTheSameContent(otherItemViewModel: BaseItemViewModel): Boolean
}