package com.example.smalltalkAndroid.feature.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smalltalkAndroid.R
import com.example.smalltalkAndroid.databinding.ItemProductBinding
import com.example.smalltalkAndroid.model.Product

class ProductsAdapter : ListAdapter<Product, ProductsAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.create(parent)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = getItem(position)?.let { holder.setModel(it) } as Unit

    class ViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup) = ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_product, parent, false))
        }

        fun setModel(product: Product) {
            binding.viewModel = ProductItemViewModel(product)
        }
    }
}