package com.maxrzhe.presentation.adapters

import androidx.recyclerview.widget.DiffUtil

class GenericDiffUtil<T>(
    private val oldItems: List<T>,
    private val newItems: List<T>,
    private val itemDiff: GenericItemDiff<T>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        itemDiff.isSame(oldItems, newItems, oldItemPosition, newItemPosition)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        itemDiff.isSameContent(oldItems, newItems, oldItemPosition, newItemPosition)

}