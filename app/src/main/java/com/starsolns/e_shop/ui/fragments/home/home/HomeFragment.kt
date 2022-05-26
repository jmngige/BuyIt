package com.starsolns.e_shop.ui.fragments.home.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentHomeBinding
import com.starsolns.e_shop.model.Product
import com.starsolns.e_shop.model.UserEntity
import com.starsolns.e_shop.model.Users
import com.starsolns.e_shop.ui.activities.HomeActivity
import com.starsolns.e_shop.ui.adapter.ProductsAdapter
import com.starsolns.e_shop.util.Constants
import com.starsolns.e_shop.viewmodel.SharedViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseUser: String
    private lateinit var auth: FirebaseAuth

    private lateinit var productsList: ArrayList<Product>

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        auth = Firebase.auth
        firebaseUser = auth.currentUser!!.uid


        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        loadUserDetails()
        sharedViewModel.getUserProfile(firebaseUser).observe(viewLifecycleOwner){profile->
            if(profile.isNotEmpty()){
                binding.homeUserName.text = "Hello, ${profile[0].firstName}"
            }
        }

        productsList = arrayListOf()

        val productsAdapter = ProductsAdapter(requireContext(), productsList)
        binding.productsRv.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.productsRv.adapter = productsAdapter
        binding.productsRv.setHasFixedSize(true)

        Log.i("TAG", productsList.toString())
        Log.i("TAG", "No products")

        binding.addProductButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addProductFragment)
            if(requireActivity() is HomeActivity){
                (activity as HomeActivity).hideBottomNavBar()
            }
        }
        getProductsFromFirebase()
        binding.productsRv.showShimmer()

        return binding.root
    }

    private fun loadUserDetails(){
        val db = Firebase.firestore
        db.collection(Constants.USERS)
            .document(firebaseUser)
            .get()
            .addOnSuccessListener { result->
                val user = result.toObject<Users>()

                val userEntity =  UserEntity(
                    user!!.firstName,
                    user.lastName,
                    user.email,
                    user.phone,
                    user.dob,
                    user.gender,
                    user.id
                )

                sharedViewModel.insertUserProfile(userEntity)
                sharedViewModel.saveSellerUserName(user.firstName)
                binding.homeProfileImage.load(user.profilePicture){
                    crossfade(true)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun getProductsFromFirebase(){
        val db = Firebase.firestore
        db.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document->
                binding.productsRv.hideShimmer()
                for (doc in document.documents){
                    val products = doc.toObject(Product::class.java)
                    productsList.add(products!!)
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