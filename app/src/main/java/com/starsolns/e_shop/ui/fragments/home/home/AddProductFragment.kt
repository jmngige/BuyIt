package com.starsolns.e_shop.ui.fragments.home.home

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddProductBinding.inflate(layoutInflater, container, false)
        (context as HomeActivity).setUpSupportCustomActionBar(binding.addProductToolBar)


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

        binding.addProductCategory.setOnClickListener {

        }

        return binding.root
    }

    private fun loadImagesFromGallery() {
        Dexter.withContext(requireContext()).withPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object: PermissionListener{
            override fun onPermissionGranted(perms: PermissionGrantedResponse?) {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, Constants.GALLERY_OPTION_CODE)
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

        })
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}