package com.starsolns.e_shop.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.starsolns.e_shop.databinding.CartItemLayoutBinding
import com.starsolns.e_shop.model.Cart

class CartAdapter(
    private val cartList: ArrayList<Cart>,
    private val context: Context
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val cartLayoutBinding: CartItemLayoutBinding =
            CartItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(cartLayoutBinding)
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        val currentProduct = cartList[position]
        holder.bind(currentProduct)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    class ViewHolder(binding: CartItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        val image = binding.cartProductImage
        val pName = binding.cartProductName
        val price = binding.cartProductPrice
        val count = binding.cartProductCount

        fun bind(cart: Cart){
            pName.text = cart.name
            price.text = cart.price
            count.text = cart.quantity
            image.load(cart.image)
        }
    }

}