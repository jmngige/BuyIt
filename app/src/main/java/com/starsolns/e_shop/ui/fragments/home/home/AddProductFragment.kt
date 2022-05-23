package com.starsolns.e_shop.ui.fragments.home.home

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentAddProductBinding
import com.starsolns.e_shop.ui.activities.HomeActivity
import com.starsolns.e_shop.util.Constants
import com.starsolns.e_shop.util.ProgressButton

class AddProductFragment : Fragment() {

    private lateinit var dialog: ProgressButton

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    private var productImageUri: Uri? = null

    private lateinit var firebaseUser: String
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddProductBinding.inflate(layoutInflater, container, false)
        (context as HomeActivity).setUpSupportCustomActionBar(binding.addProductToolBar)

        auth = Firebase.auth
        firebaseUser = auth.currentUser!!.uid

        val toolbar = binding.addProductToolBar
        toolbar.setOnClickListener {
            findNavController().navigate(R.id.action_addProductFragment_to_homeFragment)
        }


        val buttonView = binding.submitProductDetails.loginRegisterAccessButton
        dialog = ProgressButton(requireContext(), buttonView)
        dialog.showSubmit()

        binding.addProductImage.setOnClickListener {
            loadImagesFromGallery()
        }

        val categories = resources.getStringArray(R.array.categories)
        val categoriesAdapter =
            ArrayAdapter(requireContext(), R.layout.category_list_item, categories)
        binding.addProductCategory.setAdapter(categoriesAdapter)

        binding.submitProductDetails.loginRegisterAccessButton.setOnClickListener {
            val category = binding.addProductCategory.text.toString()
            Toast.makeText(requireContext(), category, Toast.LENGTH_LONG).show()
        }

        binding.submitProductDetails.loginRegisterAccessButton.setOnClickListener {
            if (validateEntries()) {
                uploadProductDetails()
            }
        }

        return binding.root
    }

    private fun uploadProductDetails() {
        val storage = Firebase.storage
        val productsRef = storage.getReference("Images/Product Images")
            .child(firebaseUser + "_" + System.currentTimeMillis() + ".jpg")
        productsRef.putFile(productImageUri!!).addOnSuccessListener { snapshot ->
            snapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                //saveProductDetailsToDatabase(uri.toString())
                Toast.makeText(requireContext(), "Uploaded successfully", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun saveProductDetailsToDatabase(imageUrl: String) {

        val db = Firebase.firestore
        //db.collection("Products").document(firebaseUser).set()

    }

    private fun validateEntries(): Boolean {

        val productName = binding.addProductName.text.toString().trim()
        val price = binding.addProductPrice.text.toString().trim()
        val description = binding.addProductDescription.text.toString().trim()
        val richDescription = binding.addProductRichDescription.text.toString().trim()
        val category = binding.addProductCategory.text.toString().trim()
        val quantity = binding.addProductQuantity.text.toString().trim()

        if (productImageUri == null) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.add_product_image),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        if (productName.isEmpty()) {
            binding.txtInputAddProductName.helperText = "Product name required"
            return false
        } else {
            binding.txtInputAddProductName.helperText = null
        }
        if (price.isEmpty()) {
            binding.txtInputAddProductPrice.helperText = "Price required"
            return false
        } else {
            binding.txtInputAddProductPrice.helperText = null
        }
        if (description.isEmpty()) {
            binding.txtInputAddProductDescription.helperText = "Product description required"
            return false
        } else {
            binding.txtInputAddProductDescription.helperText = null
        }
        if (richDescription.isEmpty()) {
            binding.txtInputAddProductRichDescription.helperText =
                "Rich product description required"
            return false
        } else {
            binding.txtInputAddProductRichDescription.helperText = null
        }
        if (category.isEmpty()) {
            binding.txtInputAddProductCategory.helperText = "Product category required"
            return false
        } else {
            binding.txtInputAddProductCategory.helperText = null
        }
        if (quantity.isEmpty()) {
            binding.txtInputAddProductQuantity.helperText = "Product quantity required"
            return false
        } else {
            binding.txtInputAddProductQuantity.helperText = null
        }
        return true
    }

    private fun loadImagesFromGallery() {

        Dexter.withContext(requireContext()).withPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(perms: PermissionGrantedResponse?) {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, Constants.GALLERY_OPTION_CODE_TWO)
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.permissions_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                showCustomPermissionDialog()
            }

        }).onSameThread().check()
    }

    private fun showCustomPermissionDialog() {
        AlertDialog.Builder(requireActivity())
            .setTitle(resources.getString(R.string.permissions_denied_title))
            .setMessage(resources.getString(R.string.permissions_denied))
            .setPositiveButton("Settings") { _, _ ->
                try {
                    val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", requireContext().packageName, null)
                    settingsIntent.data = uri
                    startActivity(settingsIntent)

                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(requireContext(), "Please try again", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.GALLERY_OPTION_CODE_TWO) {
                data?.let {
                    productImageUri = data.data!!
                    binding.productImage.load(productImageUri)
                    binding.addProductImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.ic_edit_photo
                        )
                    )
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}