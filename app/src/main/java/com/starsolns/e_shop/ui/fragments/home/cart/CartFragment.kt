package com.starsolns.e_shop.ui.fragments.home.cart

import android.os.Bundle
import android.util.Log
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

        val cartListAdapter = CartAdapter(requireContext(), cartListItems)
        binding.cartItemsList.layoutManager = LinearLayoutManager(requireActivity())
        binding.cartItemsList.adapter = cartListAdapter
        loadCartItems()

        if(cartListItems.size == 0){
            binding.totalPriceAmount.visibility = View.INVISIBLE
            binding.totalPriceTitle.visibility = View.INVISIBLE
            binding.proceedCheckoutButton.visibility = View.INVISIBLE
            binding.emptyCartImageview.visibility = View.VISIBLE
            binding.emptyCartText.visibility = View.VISIBLE
        }

        return binding.root
    }

    private fun loadCartItems(){
        Log.i("TAG", "loading Cart Items")
        val db = Firebase.firestore
        db.collection(Constants.CART)
            .whereEqualTo("user_id", firebaseUser)
            .get()
            .addOnSuccessListener {document->
                binding.cartItemsList.hideShimmer()
                for(doc in document.documents){
                    val cartProduct = doc.toObject(Cart::class.java)
                    cartProduct?.id = doc.id
                    cartProduct?.let {
                        cartListItems.add(it)

                        if(cartListItems.size > 0) {
                            binding.totalPriceAmount.visibility = View.VISIBLE
                            binding.totalPriceTitle.visibility = View.VISIBLE
                            binding.proceedCheckoutButton.visibility = View.VISIBLE
                            binding.emptyCartImageview.visibility = View.INVISIBLE
                            binding.emptyCartText.visibility = View.INVISIBLE
                        }

                        var totalPrice: Int = 0
                        for(items in cartListItems){
                            val price = items.price.toInt()
                            val quantity = items.cart_quantity.toInt()
                            totalPrice += (price * quantity)
                            val totalAmount = totalPrice.toString()
                            binding.totalPriceAmount.text = totalAmount
                        }
                        Log.i("TAG", it.toString())
                    }
                }
            }
            .addOnFailureListener {
                Log.i("TAG","Theres nothing here")
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}