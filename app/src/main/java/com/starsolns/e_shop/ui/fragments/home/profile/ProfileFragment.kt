package com.starsolns.e_shop.ui.fragments.home.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentProfileBinding
import com.starsolns.e_shop.ui.activities.HomeActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? =null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        binding.profileLayout.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
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