package com.starsolns.e_shop.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.starsolns.e_shop.databinding.AddressItemLayoutBinding
import com.starsolns.e_shop.model.Address
import com.starsolns.e_shop.ui.fragments.home.profile.ShippingAddressFragmentDirections

class AddressAdapter(
    private val context: Context
): RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    var addressList = emptyList<Address>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressAdapter.ViewHolder {
       val customBinding: AddressItemLayoutBinding = AddressItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(customBinding)
    }

    override fun onBindViewHolder(holder: AddressAdapter.ViewHolder, position: Int) {
        val currentAddress = addressList[position]
        holder.bind(currentAddress)

    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    fun setData(newList: List<Address>){
        this.addressList = newList
        notifyDataSetChanged()
    }
    
    inner class ViewHolder(binding: AddressItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        val location = binding.addressLocationName
        val name = binding.userAddressFullName
        val phone = binding.userAddressPhone

        fun bind(address: Address){
            location.text = address.address
            name.text = "${address.firstName} ${address.lastName}"
            phone.text = address.phone
        }
        
    }
}