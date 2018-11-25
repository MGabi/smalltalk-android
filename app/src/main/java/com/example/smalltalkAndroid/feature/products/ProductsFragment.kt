package com.example.smalltalkAndroid.feature.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smalltalkAndroid.R
import com.example.smalltalkAndroid.databinding.FragmentProductsBinding
import com.example.smalltalkAndroid.model.Product
import com.example.smalltalkAndroid.utils.BundleDelegate
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsFragment : Fragment() {

    companion object {
        var Bundle.products by BundleDelegate.ParcelableList<Product>("products")
        fun newInstance(products: List<Product>) = ProductsFragment().apply { arguments = Bundle().also { it.products = products } }
    }

    lateinit var binding: FragmentProductsBinding
    val viewModel: ProductsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        DataBindingUtil.inflate<FragmentProductsBinding>(inflater, R.layout.fragment_products, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.setLifecycleOwner(viewLifecycleOwner)
        val productsAdapter = ProductsAdapter { id, state, isWishlistButton ->
            //TODO update the item state on the backend
            if (isWishlistButton) {
                viewModel.repository.setWishlist(id, !state)
            } else {
                viewModel.repository.setCart(id, !state)
            }
        }
        binding.productsRecyclerView.run {
            adapter = productsAdapter
            layoutManager = LinearLayoutManager(context)
        }
        arguments?.products?.let { viewModel.products.value = it }
        viewModel.products.observe(viewLifecycleOwner, Observer { productsAdapter.submitList(it) })
    }
}