package com.starsolns.e_shop.ui.fragments.home.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentEditProfileBinding
import com.starsolns.e_shop.databinding.FragmentProfileBinding
import com.starsolns.e_shop.ui.activities.HomeActivity

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? =null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)
        (context as HomeActivity).setUpSupportCustomActionBar(binding.editProfileToolBar)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


}