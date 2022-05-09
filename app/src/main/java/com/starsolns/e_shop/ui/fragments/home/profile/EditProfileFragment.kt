package com.starsolns.e_shop.ui.fragments.home.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentEditProfileBinding
import com.starsolns.e_shop.databinding.FragmentProfileBinding
import com.starsolns.e_shop.ui.activities.HomeActivity
import com.starsolns.e_shop.util.ProgressButton

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? =null
    private val binding get() = _binding!!
    private lateinit var toolbar: Toolbar


    private lateinit var dialog: ProgressButton
    private lateinit var buttonView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)
        (context as HomeActivity).setUpSupportCustomActionBar(binding.editProfileToolBar)

        toolbar = binding.editProfileToolBar
        toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
        }

        buttonView = binding.saveProfileUpdate.loginRegisterAccessButton

        dialog = ProgressButton(requireContext(), buttonView)
        dialog.showSubmit()


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


}