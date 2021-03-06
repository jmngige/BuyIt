package com.starsolns.e_shop.ui.fragments.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentForgotPasswordBinding
import com.starsolns.e_shop.util.ProgressButton

class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var buttonView: View
    private lateinit var dialog: ProgressButton

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentForgotPasswordBinding.inflate(layoutInflater, container, false)

        val windowInsetsController = ViewCompat.getWindowInsetsController(requireActivity().window.decorView)
        windowInsetsController?.show(WindowInsetsCompat.Type.statusBars())

        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


        auth = Firebase.auth

        buttonView = binding.submitForgotPasswordButton.loginRegisterAccessButton

        dialog = ProgressButton(requireContext(), buttonView)
        dialog.showSubmit()

        buttonView.setOnClickListener {
            if(validateInputs()){
               sendResetPasswordLink()
            }
        }

        binding.resendEmailLikButton.setOnClickListener {
            if(validateInputs()){
                sendResetPasswordLink()
            }
        }


        return binding.root
    }

    private fun validateInputs(): Boolean{
        val email = binding.forgotPasswordEmail.text.toString().trim()
        if(email.isEmpty()){
            binding.txtForgotEmailInput.helperText = "Email required"
            return false
        }else{
            binding.txtForgotEmailInput.helperText = null
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.txtForgotEmailInput.helperText = "Enter a valid Email"
            return false
        }else{
            binding.txtForgotEmailInput.helperText = null
        }
        return true
    }

    private fun sendResetPasswordLink(){
        val email = binding.forgotPasswordEmail.text.toString().trim()
        dialog.showProgressBar()
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task->
            if(task.isSuccessful){
                dialog.dismissResetProgressBar()
                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(R.id.action_forgotPasswordFragment_to_accessFragment)
                }, 1800)
            }else {
                dialog.dismissResetProgressBarError()
                Toast.makeText(requireContext(), task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}