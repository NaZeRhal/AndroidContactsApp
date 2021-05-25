package com.maxrzhe.presentation.model

abstract class BaseItemViewModel {
    abstract val id: Long
    abstract val layoutId: Int
    abstract fun isTheSameItem(baseItemViewModel: BaseItemViewModel): Boolean
    abstract fun hasTheSameContent(baseItemViewModel: BaseItemViewModel): Boolean
}