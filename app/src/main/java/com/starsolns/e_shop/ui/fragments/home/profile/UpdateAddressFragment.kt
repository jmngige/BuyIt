package com.starsolns.e_shop.ui.fragments.home.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentProfileBinding
import com.starsolns.e_shop.databinding.FragmentUpdateAddressBinding

class UpdateAddressFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentUpdateAddressBinding? =null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateAddressBinding.inflate(layoutInflater, container, false)



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}