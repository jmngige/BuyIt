package com.starsolns.e_shop.ui.fragments.home.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentProfileBinding
import com.starsolns.e_shop.model.Users
import com.starsolns.e_shop.ui.activities.HomeActivity
import com.starsolns.e_shop.util.Constants.Companion.USERS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? =null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: String

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

        binding.addressLayout.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_shippingAddressFragment)
            if(requireActivity() is HomeActivity){
                (activity as HomeActivity).hideBottomNavBar()
            }
        }

        binding.wishlistLayout.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_wishlistFragment)
            if(requireActivity() is HomeActivity){
                (activity as HomeActivity).hideBottomNavBar()
            }
        }

        binding.orderHistoryLayout.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_orderHistoryFragment)
            if(requireActivity() is HomeActivity){
                (activity as HomeActivity).hideBottomNavBar()
            }
        }

        auth = Firebase.auth
        firebaseUser = auth.currentUser!!.uid

        loadProfileDetails()

        return binding.root
    }

    private fun loadProfileDetails(){
        val db = Firebase.firestore

       lifecycleScope.launch(Dispatchers.IO) {
           db.collection(USERS)
               .document(firebaseUser)
               .get()
               .addOnSuccessListener {
                  val user = it.toObject<Users>()
                   //binding.profileName.setText("${ user?.firstName } ${user?.lastName}")
                   if(user?.profilePicture!!.isNotEmpty()){
                       binding.profileImage.load(user.profilePicture){
                           crossfade(true)
                           placeholder(R.drawable.ic_profile_placeholder)
                       }
                   }
               }
       }
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