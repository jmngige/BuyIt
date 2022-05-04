package com.starsolns.e_shop.ui.fragments

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentLoginBinding
import com.starsolns.e_shop.util.ProgressButton

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var buttonView: View
    private lateinit var dialog: ProgressButton

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        auth = Firebase.auth

        buttonView = binding.loginButton.loginRegisterAccessButton

        dialog = ProgressButton(requireContext(), buttonView)
        dialog.showLogin()

        buttonView.setOnClickListener {
            validateAndLoginUser()
        }

        binding.loginForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_accessFragment_to_forgotPasswordFragment)
        }

        return binding.root
    }

    private fun validateAndLoginUser() {
        val email = binding.loginEmail.text.toString().trim()
        val password = binding.loginPassword.text.toString().trim()

        if(email.isEmpty()){
            binding.txtInputLoginEmail.helperText = "Email Required"
            return
        }else{
            binding.txtInputLoginEmail.helperText = null
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.txtInputLoginEmail.helperText = "Enter a valid Email"
            return
        }
        else {
            binding.txtInputLoginEmail.helperText = null
        }
        if(password.isEmpty()){
            binding.txtInputLoginPassword.helperText = "Password Required"
            return
        }else {
            binding.txtInputLoginPassword.helperText = null
        }

        loginUser(email, password)

    }

    private fun loginUser(email: String, password: String) {
        dialog.showProgressBar()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
            if(task.isSuccessful){
                dialog.dismissProgressBar()
                findNavController().navigate(R.id.action_accessFragment_to_homeActivity)
            }
            else {
                dialog.dismissProgressBar()
                Toast.makeText(requireContext(), task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}