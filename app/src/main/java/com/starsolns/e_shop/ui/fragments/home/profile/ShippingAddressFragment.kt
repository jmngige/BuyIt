package com.starsolns.e_shop.ui.fragments.home.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentProfileBinding
import com.starsolns.e_shop.databinding.FragmentShippingAddressBinding

class ShippingAddressFragment : Fragment() {

    private var _binding: FragmentShippingAddressBinding? =null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShippingAddressBinding.inflate(layoutInflater, container, false)

        binding.addShippingAddressButton.setOnClickListener {
            findNavController().navigate(R.id.action_shippingAddressFragment_to_updateAddressFragment)
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}