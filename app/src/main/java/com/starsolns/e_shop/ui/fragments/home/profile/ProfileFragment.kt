package com.starsolns.e_shop.ui.fragments.home.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? =null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}