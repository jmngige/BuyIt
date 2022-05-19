package com.starsolns.e_shop.ui.fragments.home.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentAddProductBinding
import com.starsolns.e_shop.databinding.FragmentHomeBinding
import com.starsolns.e_shop.ui.activities.HomeActivity

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddProductBinding.inflate(layoutInflater, container, false)
        (context as HomeActivity).setUpSupportCustomActionBar(binding.addProductToolBar)



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}