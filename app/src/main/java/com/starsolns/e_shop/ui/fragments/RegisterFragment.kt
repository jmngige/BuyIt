package com.starsolns.e_shop.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)

        binding.registerButton.setOnClickListener {
            validateUserAndRegister()
        }

        return binding.root
    }

    private fun validateUserAndRegister() {
        val firstName = binding.registerFirstName.text.toString().trim()
        val lastName = binding.registerLastName.text.toString().trim()
        val email = binding.registerEmail.text.toString().trim()
        val phone = binding.registerPhone.text.toString().trim()
        val password = binding.registerPassword.text.toString().trim()
        val confPassword = binding.registerConfirmPassword.text.toString().trim()
        binding.termsAndConditions

        if(firstName.isEmpty()){
            binding.txtInputRegisterFirstName.helperText = "First Name Required"
            return
        }else{
            binding.txtInputRegisterFirstName.helperText = null
        }
        if(lastName.isEmpty()){
            binding.txtInputRegisterLastName.helperText = "Last Name Required"
            return
        }else {
            binding.txtInputRegisterLastName.helperText = null
        }
        if(email.isEmpty()){
            binding.txtInputRegisterEmail.helperText = "Email Required"
            return
        }
        else{
            binding.txtInputRegisterEmail.helperText = null
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.txtInputRegisterEmail.helperText = "Enter a valid Email"
            return
        }
        else{
            binding.txtInputRegisterEmail.helperText = null
        }
        if (phone.isEmpty()){
            binding.txtInputRegisterPhone.helperText = "Phone Required"
            return
        } else {
            binding.txtInputRegisterPhone.helperText = null
        }
        if(phone.length != 10){
            binding.txtInputRegisterPhone.helperText = "Phone must be 10 Digits"
            return
        }else {
            binding.txtInputRegisterPhone.helperText = null
        }
        if(password.isEmpty()){
            binding.txtInputRegisterPassword.helperText = "Password Required"
            return
        }else {
            binding.txtInputRegisterPassword.helperText = null
        }
        if(password.length < 6){
            binding.txtInputRegisterPassword.helperText = "Password cannot be less than 6 characters"
            return
        }
        if(!confPassword.equals(password)){
            binding.txtInputRegisterConfirmPassword.helperText = "Passwords do not match"
            return
        }else {
            binding.txtInputRegisterConfirmPassword.helperText = null
        }
        if(!binding.termsAndConditions.isChecked){
            Toast.makeText(requireContext(), "Accept term and Conditions to continue", Toast.LENGTH_LONG).show()
            return
        }

        Toast.makeText(requireContext(), "Everything's okay buddy", Toast.LENGTH_LONG).show()

    }

    private fun setNull(): String?{
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}