package com.starsolns.e_shop.ui.fragments.home.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.Coil
import coil.load
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentEditProfileBinding
import com.starsolns.e_shop.databinding.FragmentProfileBinding
import com.starsolns.e_shop.ui.activities.HomeActivity
import com.starsolns.e_shop.util.ProgressButton
import com.starsolns.e_shop.viewmodel.SharedViewModel
import kotlinx.coroutines.flow.collect

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? =null
    private val binding get() = _binding!!
    private lateinit var toolbar: Toolbar

    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var dialog: ProgressButton
    private lateinit var buttonView: View
    private var mImageString: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)
        (context as HomeActivity).setUpSupportCustomActionBar(binding.editProfileToolBar)



        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        sharedViewModel.readImageString.observe(viewLifecycleOwner){ imageStr->
                mImageString = imageStr
        }

        toolbar = binding.editProfileToolBar
        toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
        }

        buttonView = binding.saveProfileUpdate.loginRegisterAccessButton

        dialog = ProgressButton(requireContext(), buttonView)
        dialog.showSubmit()

        binding.changeProfileImage.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_changeImageFragment)
        }

        if(mImageString != ""){
            val imageBitmap = stringToBitmap(mImageString)
            binding.editProfileImage.load(imageBitmap)
        }


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun stringToBitmap(image: String): Bitmap{
        val imageBytes = Base64.decode(image, 0)
        return  BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }


}