package com.starsolns.e_shop.ui.fragments.home.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentAddUpdateShippingAddressBinding
import com.starsolns.e_shop.databinding.FragmentShippingAddressBinding
import com.starsolns.e_shop.model.Address
import com.starsolns.e_shop.util.Constants
import com.starsolns.e_shop.util.ProgressButton
import com.starsolns.e_shop.viewmodel.SharedViewModel


class AddUpdateShippingAddressFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddUpdateShippingAddressBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var shippingAddressDialog: BottomSheetDialog

    private lateinit var buttonView: View
    private lateinit var dialog: ProgressButton


    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddUpdateShippingAddressBinding.inflate(layoutInflater, container, false)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]


        buttonView = binding.submitShippingAddressButton.loginRegisterAccessButton
        dialog = ProgressButton(requireContext(), buttonView)
        dialog.showSubmit()

        auth = Firebase.auth
        firebaseUser = auth.currentUser!!.uid

        binding.submitShippingAddressButton.loginRegisterAccessButton.setOnClickListener {
            addShippingAddress()
        }

        return binding.root
    }

    private fun addShippingAddress() {

        sharedViewModel.getUserProfile(firebaseUser).observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.shippingUserFirstName.setText(it[0].firstName)
                binding.shippingUserLastName.setText(it[0].lastName)
                binding.shippingUserPhone.setText(it[0].phone)
            }
        }

        binding.submitShippingAddressButton.loginRegisterAccessButton.setOnClickListener {
            validateInputsAndSubmit()
        }


    }

    private fun validateInputsAndSubmit() {
        val firstName = binding.shippingUserFirstName.text.toString()
        val lastName = binding.shippingUserLastName.text.toString()
        val phone = binding.shippingUserPhone.text.toString()
        val region = binding.shippingUserRegion.text.toString()
        val town = binding.shippingUserTown.text.toString()
        val address = binding.shippingUserAddress.text.toString()
        val addInfo = binding.shippingUserAddressInfo.text.toString()

        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || region.isEmpty() || town.isEmpty() || address.isEmpty() || addInfo.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all the details to continue", Toast.LENGTH_SHORT).show()
        }else {

            val address = Address(
                firebaseUser, firstName, lastName, phone, region, town, address, addInfo
            )

            dialog.showProgressBar()

            val db = Firebase.firestore
            db.collection(Constants.ADDRESSES)
                .document()
                .set(address, SetOptions.merge())
                .addOnSuccessListener {
                    dialog.dismissProgressBar()
                    shippingAddressDialog.dismiss()
                }
                .addOnFailureListener {
                    shippingAddressDialog.dismiss()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }
}