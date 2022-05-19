package com.starsolns.e_shop.ui.fragments.home.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentHomeBinding
import com.starsolns.e_shop.ui.activities.HomeActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        binding.addProductButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addProductFragment)
            if(requireActivity() is HomeActivity){
                (activity as HomeActivity).hideBottomNavBar()
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if(requireActivity() is HomeActivity){
            (activity as HomeActivity).showBottomNavBar()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}