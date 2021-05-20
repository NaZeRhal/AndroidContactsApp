package com.maxrzhe.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class GenericBindingAdapter<T>() :
    RecyclerView.Adapter<GenericBindingAdapter<T>.ViewHolder>() {

    constructor(diffUtil: GenericItemDiff<T>) : this() {
        this.diffUtil = diffUtil
    }

    private var items = mutableListOf<T>()
    private var diffUtil: GenericItemDiff<T>? = null
    private var itemClickListener: OnItemClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), viewType, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutId(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], itemClickListener)
    }

    override fun getItemCount(): Int = items.size

    fun addOnItemClickListener(listener: OnItemClickListener<T>) {
        itemClickListener = listener
    }

    fun getItemAt(position: Int): T = items[position]

    fun getList(): List<T> {
        return items
    }

    open fun setList(itemsList: List<T>) {
        if (diffUtil != null) {
            val result = DiffUtil.calculateDiff(GenericDiffUtil(items, itemsList, diffUtil!!))

            items.clear()
            items.addAll(itemsList)
            result.dispatchUpdatesTo(this)
        } else {
            items = itemsList.toMutableList()
            notifyDataSetChanged()
        }
    }

    protected abstract fun getLayoutId(position: Int): Int

    interface OnItemClickListener<T> {
        fun onClickItem(data: T)
    }

    inner class ViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T, listener: OnItemClickListener<T>?) {
            binding.setVariable(BR.data, item)
            binding.root.setOnClickListener { listener?.onClickItem(item) }
            binding.executePendingBindings()
        }
    }

}