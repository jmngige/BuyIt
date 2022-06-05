package com.starsolns.e_shop.ui.fragments.home.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentCartBinding
import com.starsolns.e_shop.databinding.FragmentEditProfileBinding
import com.starsolns.e_shop.model.Cart
import com.starsolns.e_shop.ui.adapter.CartAdapter
import com.starsolns.e_shop.util.Constants

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: String

    private lateinit var cartListItems: ArrayList<Cart>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(layoutInflater, container, false)

        auth = Firebase.auth
        firebaseUser = auth.currentUser!!.uid

        cartListItems = arrayListOf()

        val cartListAdapter = CartAdapter(cartListItems, requireContext())
        binding.cartItemsList.layoutManager = LinearLayoutManager(requireActivity())
        binding.cartItemsList.adapter = cartListAdapter
        loadCartItems()

        return binding.root
    }

    private fun loadCartItems(){
        val db = Firebase.firestore
        db.collection(Constants.CART)
            .whereEqualTo("user_id", firebaseUser)
            .get()
            .addOnSuccessListener {document->
                for(doc in document.documents){
                    val products = doc.toObject(Cart::class.java)
                    products?.let {
                        cartListItems.add(it)
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}