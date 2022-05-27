package com.starsolns.e_shop.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.ProductItemLayoutBinding
import com.starsolns.e_shop.model.Product
import com.starsolns.e_shop.ui.fragments.home.home.HomeFragmentDirections


class ProductsAdapter(
    private val context: Context,
    private val productsList: ArrayList<Product>
) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsAdapter.ViewHolder {
        val customBinding: ProductItemLayoutBinding =
            ProductItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(customBinding)
    }

    override fun onBindViewHolder(holder: ProductsAdapter.ViewHolder, position: Int) {
        val currentProduct = productsList[position]
        holder.bind(currentProduct)
        holder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(currentProduct)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    class ViewHolder(binding: ProductItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        private val image = binding.productItemImage
        private val pName = binding.productItemName
        private val price = binding.productItemPrice
        private val desc = binding.productItemDescription


        fun bind(product: Product) {
            pName.text = product.name
            price.text = product.price
            desc.text = product.description
            image.load(product.productImage) {
                crossfade(true)
            }
        }
    }
}