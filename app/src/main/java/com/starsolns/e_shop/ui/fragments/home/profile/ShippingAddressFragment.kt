package com.starsolns.e_shop.ui.fragments.home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentShippingAddressBinding
import com.starsolns.e_shop.model.Address
import com.starsolns.e_shop.ui.adapter.AddressAdapter
import com.starsolns.e_shop.util.Constants
import com.starsolns.e_shop.util.OnAddressItemSwipe
import com.todkars.shimmer.ShimmerRecyclerView

class ShippingAddressFragment : Fragment() {

    private var _binding: FragmentShippingAddressBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: String

    private lateinit var addressList: ArrayList<Address>
    private lateinit var addressAdapter: AddressAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShippingAddressBinding.inflate(layoutInflater, container, false)

        binding.addShippingAddressButton.setOnClickListener {
            findNavController().navigate(R.id.action_shippingAddressFragment_to_addUpdateShippingAddressFragment)
        }

        auth = Firebase.auth
        firebaseUser = auth.currentUser!!.uid

        addressList = arrayListOf()
        addressAdapter = AddressAdapter(requireContext())
        addressAdapter.setData(addressList)
        binding.shippingAddressListRv.layoutManager = LinearLayoutManager(requireActivity())
        binding.shippingAddressListRv.adapter = addressAdapter

        loadUserAddresses()

        OnItemSwiped(binding.shippingAddressListRv)

        return binding.root
    }

    private fun OnItemSwiped(recyclerView: ShimmerRecyclerView){
        val onSwipeCallback = object : OnAddressItemSwipe(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = addressAdapter.addressList[viewHolder.adapterPosition]
                deleteAddress(item)
                addressAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                restoreItem(viewHolder.itemView, item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(onSwipeCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun deleteAddress(address: Address){
        val db = Firebase.firestore
        db.collection(Constants.ADDRESSES)
            .document(address.id)
            .delete()
            .addOnSuccessListener {
                loadUserAddresses()
            }
            .addOnFailureListener {

            }
    }

    private fun restoreItem(view: View, address: Address){
        val snackBar =
            Snackbar.make(view, "Successfully deleted ${address.address}", Snackbar.LENGTH_LONG)
        snackBar.setAction("Undo") {
            val db = Firebase.firestore
            db.collection(Constants.ADDRESSES)
                .document()
                .set(address, SetOptions.merge())
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
        }
        snackBar.show()


    }

    private fun loadUserAddresses(){
        val db = Firebase.firestore
        db.collection(Constants.ADDRESSES)
            .whereEqualTo("user_id", firebaseUser)
            .get()
            .addOnSuccessListener { document->
                binding.shippingAddressListRv.hideShimmer()
                for(doc in document.documents){
                    val addresses = doc.toObject(Address::class.java)
                    addresses?.id = doc.id
                    addresses?.let { addressList.add(it) }
                }
            }
            .addOnFailureListener {

            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}