package com.starsolns.e_shop.ui.fragments.home.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentHomeBinding
import com.starsolns.e_shop.databinding.FragmentProductDetailsBinding
import com.starsolns.e_shop.model.Product
import com.starsolns.e_shop.ui.activities.HomeActivity
import com.starsolns.e_shop.ui.adapter.ProductsAdapter
import com.starsolns.e_shop.util.Constants

class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: ProductDetailsFragmentArgs by navArgs()

    private lateinit var sellerProductsList: ArrayList<Product>
    private lateinit var similarProductsList: ArrayList<Product>

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProductDetailsBinding.inflate(layoutInflater, container, false)

        if(requireActivity() is HomeActivity){
            (activity as HomeActivity).hideBottomNavBar()
        }

        val product = args.productItem

        binding.productDetailName.text = product.name
        binding.productDetailImage.load(product.productImage){
            crossfade(true)
        }
        binding.productDetailQuantityImage.load(product.productImage){
            crossfade(true)
            placeholder(R.drawable.ic_gallery)
        }
        binding.productDetailPrice.text = "Ksh ${product.price}"
        binding.productDetailDescription.text = product.description
        binding.sellerName.text = product.user_name
        binding.productDetailQuanity.text = product.quantity
        binding.productDetailRichDescription.text = product.richDescription
        binding.productDetailsRating.rating = 2.5F

        //sellerProductsList = arrayListOf()
        similarProductsList = arrayListOf()


        val productsAdapter2 = ProductsAdapter(requireContext(), similarProductsList)
        binding.productsInSimilarCategoryRecyclerview.showShimmer()
        binding.productsInSimilarCategoryRecyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.productsInSimilarCategoryRecyclerview.adapter = productsAdapter2
        loadSimilarCategoryItems()

        auth = Firebase.auth
        firebaseUser = auth.currentUser!!.uid

        if( firebaseUser != product.user_id){
            binding.addToCartButton.visibility = View.VISIBLE
            binding.orderNowButton.visibility = View.INVISIBLE
        }else {
            binding.addToCartButton.visibility = View.GONE
            binding.orderNowButton.visibility = View.GONE
        }

        addProductToCart()

        return binding.root
    }

    private fun addProductToCart() {

    }


    private fun loadSimilarCategoryItems() {
        val productCategory = args.productItem.category
        val db = Firebase.firestore
        db.collection(Constants.PRODUCTS)
            .whereEqualTo("category", productCategory)
            .get()
            .addOnSuccessListener { document->
                binding.productsInSimilarCategoryRecyclerview.hideShimmer()
                for (doc in document.documents){
                    val products = doc.toObject(Product::class.java)
                    similarProductsList.add(products!!)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}