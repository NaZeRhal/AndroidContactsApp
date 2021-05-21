package com.maxrzhe.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maxrzhe.presentation.model.BaseItemViewModel

class BaseBindingAdapter<T : ViewDataBinding> :
    RecyclerView.Adapter<BaseBindingAdapter.ViewHolder<T>>() {

    private val items = mutableListOf<BaseItemViewModel>()

    override fun getItemViewType(position: Int): Int = items[position].layoutId

    override fun getItemId(position: Int): Long = items[position].id

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val binding =
            DataBindingUtil.inflate<T>(LayoutInflater.from(parent.context), viewType, parent, false)
        return ViewHolder<T>(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) =
        holder.bind(items[position])

    fun setItems(newItems: List<BaseItemViewModel>) {
        val diffResult = DiffUtil.calculateDiff(DiffHelper(newItems, items))
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    private class DiffHelper(
        val newItems: List<BaseItemViewModel>,
        val oldItems: List<BaseItemViewModel>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size
        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].isTheSameItem(newItems[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].hasTheSameContent(newItems[newItemPosition])
    }

    class ViewHolder<out T : ViewDataBinding>(val binding: T) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(baseItemViewModel: BaseItemViewModel) {
            binding.setVariable(BR.item, baseItemViewModel)
            binding.executePendingBindings()
        }
    }
}

