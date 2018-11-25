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
import com.example.smalltalkAndroid.utils.isBuy
import com.example.smalltalkAndroid.utils.isWishlist

typealias ItemClickListener = (id: Int, state: Boolean, isWishlistButton: Boolean) -> Unit

class ProductsAdapter(private val onClick: ItemClickListener) : ListAdapter<Product, ProductsAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.create(parent, onClick)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = getItem(position)?.let { holder.setModel(it) } as Unit

    class ViewHolder(private val binding: ItemProductBinding, private val onClick: ItemClickListener) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup, onClick: ItemClickListener) =
                ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_product, parent, false), onClick)
        }

        init {
            binding.wishlistButton.setOnClickListener {
                binding.viewModel?.let { viewModel ->
                    onClick(viewModel.id, viewModel.isWishlist, true)
                    viewModel.isWishlist = !viewModel.isWishlist
                    binding.wishlistButton.isWishlist(viewModel.isWishlist)
                }
            }
            binding.buyButton.setOnClickListener {
                binding.viewModel?.let { viewModel ->
                    onClick(viewModel.id, viewModel.isBuy, false)
                    viewModel.isBuy = !viewModel.isBuy
                    binding.buyButton.isBuy(viewModel.isBuy)

                }
            }
        }

        fun setModel(product: Product) {
            binding.viewModel = ProductItemViewModel(product)
        }
    }
}