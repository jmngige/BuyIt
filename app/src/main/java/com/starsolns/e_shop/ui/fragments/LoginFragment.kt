package com.starsolns.e_shop.ui.fragments

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBindings
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var buttonView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        buttonView = binding.loginButton.loginRegisterAccessButton

        buttonView.setOnClickListener {
            validateAndLoginUser()
        }

        return binding.root
    }

    private fun validateAndLoginUser() {
        val email = binding.loginEmail.text.toString().trim()
        val password = binding.loginPassword.text.toString().trim()

        if(email.isEmpty()){
            binding.loginEmail.error = "Email Required"
            binding.loginEmail.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.loginEmail.error = "Enter a valid email "
            binding.loginEmail.requestFocus()
            return
        }
        if(password.isEmpty()){
            binding.loginPassword.error = "Password Required"
            binding.loginPassword.requestFocus()
            return
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}