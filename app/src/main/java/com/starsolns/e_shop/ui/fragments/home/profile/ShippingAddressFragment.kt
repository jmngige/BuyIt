package com.starsolns.e_shop.ui.fragments.home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentShippingAddressBinding
import com.starsolns.e_shop.databinding.FragmentUpdateAddressBinding
import com.starsolns.e_shop.model.Address
import com.starsolns.e_shop.util.Constants
import com.starsolns.e_shop.viewmodel.SharedViewModel

class ShippingAddressFragment : Fragment() {

    private var _binding: FragmentShippingAddressBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var customBinding: FragmentUpdateAddressBinding
    private lateinit var shippingAddressDialog: BottomSheetDialog

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShippingAddressBinding.inflate(layoutInflater, container, false)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        auth = Firebase.auth
        firebaseUser = auth.currentUser!!.uid

        binding.addShippingAddressButton.setOnClickListener {
            addShippingAddress()
        }


        return binding.root
    }

    private fun addShippingAddress() {
        shippingAddressDialog =
            BottomSheetDialog(requireActivity(), R.style.Theme_BottomSheetCustomStyleTheme)
        customBinding = FragmentUpdateAddressBinding.inflate(layoutInflater)
        shippingAddressDialog.setContentView(customBinding.root)

        sharedViewModel.getUserProfile(firebaseUser).observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                customBinding.shippingUserFirstName.setText(it[0].firstName)
                customBinding.shippingUserLastName.setText(it[0].lastName)
                customBinding.shippingUserPhone.setText(it[0].phone)
            }
        }

        customBinding.submitShippingAddressButton.loginRegisterAccessButton.setOnClickListener {
            validateInputsAndSubmit()
        }

        shippingAddressDialog.show()

    }

    private fun validateInputsAndSubmit() {
        val firstName = customBinding.shippingUserFirstName.text.toString()
        val lastName = customBinding.shippingUserLastName.text.toString()
        val phone = customBinding.shippingUserPhone.text.toString()
        val region = customBinding.shippingUserRegion.text.toString()
        val town = customBinding.shippingUserTown.text.toString()
        val address = customBinding.shippingUserAddress.text.toString()
        val addInfo = customBinding.shippingUserAddressInfo.text.toString()

        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || region.isEmpty() || town.isEmpty() || address.isEmpty() || addInfo.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all the details to continue", Toast.LENGTH_SHORT).show()
        }else {

            val address = Address(
                firebaseUser, firstName, lastName, phone, region, town, address, addInfo
            )

            val db = Firebase.firestore
            db.collection(Constants.ADDRESSES)
                .document(firebaseUser)
                .set(address, SetOptions.merge())
                .addOnSuccessListener {
                    shippingAddressDialog.dismiss()
                }
                .addOnFailureListener {
                    shippingAddressDialog.dismiss()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}