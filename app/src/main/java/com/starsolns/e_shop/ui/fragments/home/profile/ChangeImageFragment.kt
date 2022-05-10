package com.starsolns.e_shop.ui.fragments.home.profile

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentChangeImageBinding
import com.starsolns.e_shop.util.Constants.Companion.CAMERA_OPTION_CODE
import com.starsolns.e_shop.util.Constants.Companion.GALLERY_OPTION_CODE
import com.starsolns.e_shop.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class ChangeImageFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentChangeImageBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: SharedViewModel

    private var imageUrl: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChangeImageBinding.inflate(layoutInflater, container, false)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        binding.chooseFromGalleryText.setOnClickListener {

        }

        binding.chooseFromCameraText.setOnClickListener {
            loadImageFromCamera()
        }


        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_OPTION_CODE){
                data?.extras?.let {
                    val imageBitmap: Bitmap = data.extras?.get("data") as Bitmap
                    imageUrl = BitMapToString(imageBitmap)
                    Log.i("TAG", "ImageUrl = $imageUrl")
                    sharedViewModel.saveImageString(imageUrl)
                    findNavController().navigate(R.id.action_changeImageFragment_to_editProfileFragment)
                }
            }
        }
    }

    fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

        private fun loadImageFromCamera() {
        Dexter.withContext(requireContext()).withPermissions(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(permReport: MultiplePermissionsReport?) {
                permReport?.let {
                    if(permReport.areAllPermissionsGranted()) {
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(cameraIntent, CAMERA_OPTION_CODE)
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permsList: MutableList<PermissionRequest>?,
                permToken: PermissionToken?
            ) {
                showCustomPermissionDialog()
            }

        }).onSameThread().check()
    }

    private fun requestGalleryAccessPermissions(){
        Dexter.withContext(requireContext()).withPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(perms: PermissionGrantedResponse?) {
                val galleryIntent = Intent(Intent.ACTION_PICK,  MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, GALLERY_OPTION_CODE)
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(requireContext(), resources.getString(R.string.permissions_denied), Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionRationaleShouldBeShown(
                permsRequest: PermissionRequest?,
                permsToken: PermissionToken?
            ) {
                showCustomPermissionDialog()
            }

        }).onSameThread().check()
    }

    private fun showCustomPermissionDialog() {
        AlertDialog.Builder(requireActivity())
            .setTitle(resources.getString(R.string.permissions_denied_title))
            .setMessage(resources.getString(R.string.permissions_denied))
            .setPositiveButton("Settings"){_,_->
                try{
                    val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", requireContext().packageName, null)
                    settingsIntent.data = uri
                    startActivity(settingsIntent)

                }catch (e: ActivityNotFoundException){
                    Toast.makeText(requireContext(), "Please try again", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel"){dialog,_->
                dialog.dismiss()
            }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}