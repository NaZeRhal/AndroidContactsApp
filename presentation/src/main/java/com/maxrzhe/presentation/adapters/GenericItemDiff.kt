package com.maxrzhe.presentation.adapters

interface GenericItemDiff<T> {
    fun isSame(
        oldItems: List<T>,
        newItems: List<T>,
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean

    fun isSameContent(
        oldItems: List<T>,
        newItems: List<T>,
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean
}