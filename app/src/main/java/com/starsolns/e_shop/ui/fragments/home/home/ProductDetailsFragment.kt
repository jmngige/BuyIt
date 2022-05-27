package com.starsolns.e_shop.ui.fragments.home.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentHomeBinding
import com.starsolns.e_shop.databinding.FragmentProductDetailsBinding

class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: ProductDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProductDetailsBinding.inflate(layoutInflater, container, false)

        binding.productDetailName.text = args.productItem.name

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}