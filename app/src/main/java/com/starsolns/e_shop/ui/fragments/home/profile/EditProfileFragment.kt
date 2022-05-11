package com.starsolns.e_shop.ui.fragments.home.profile

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentEditProfileBinding
import com.starsolns.e_shop.databinding.ImageProfileBottomSheetBinding
import com.starsolns.e_shop.model.Users
import com.starsolns.e_shop.ui.activities.HomeActivity
import com.starsolns.e_shop.util.Constants
import com.starsolns.e_shop.util.Constants.Companion.CAMERA_OPTION_CODE
import com.starsolns.e_shop.util.Constants.Companion.GALLERY_OPTION_CODE
import com.starsolns.e_shop.util.ProgressButton
import com.starsolns.e_shop.viewmodel.SharedViewModel


class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? =null
    private val binding get() = _binding!!
    private lateinit var toolbar: Toolbar

    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var dialog: ProgressButton
    private lateinit var buttonView: View

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)
        (context as HomeActivity).setUpSupportCustomActionBar(binding.editProfileToolBar)

        auth = Firebase.auth
        firebaseUser = auth.currentUser!!.uid


        toolbar = binding.editProfileToolBar
        toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
        }


        buttonView = binding.saveProfileUpdate.loginRegisterAccessButton
        dialog = ProgressButton(requireContext(), buttonView)
        dialog.showSubmit()


        binding.changeProfileImage.setOnClickListener {
            showBottomSheetOptions()
        }


        loadUserDetails()


        setHasOptionsMenu(true)

        return binding.root
    }

    private fun loadUserDetails() {
        val db = Firebase.firestore

        db.collection(Constants.USERS)
            .document(firebaseUser)
            .get()
            .addOnSuccessListener {result->
                val user = result.toObject<Users>()
                binding.editEmailId.setText(user!!.email)
                binding.editFirstName.setText(user.firstName)
                binding.editLastName.setText(user.lastName)
                binding.editPhone.setText(user.phone)
                binding.editProfileImage.load(user?.profilePicture)
            }
    }

    private fun showBottomSheetOptions() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.Theme_BottomSheetCustomStyleTheme)
        val customBinding: ImageProfileBottomSheetBinding = ImageProfileBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(customBinding.root)

        customBinding.apply {
            chooseFromCameraText.setOnClickListener {
                loadImageFromCamera()
                bottomSheetDialog.dismiss()
            }
            chooseFromGalleryText.setOnClickListener {
                loadImageFromGallery()
                bottomSheetDialog.dismiss()
            }
            removeProfileText.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.show()

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
                        startActivityForResult(cameraIntent, Constants.CAMERA_OPTION_CODE)
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

    private fun loadImageFromGallery(){
        Dexter.withContext(requireContext()).withPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(perms: PermissionGrantedResponse?) {
                val galleryIntent = Intent(Intent.ACTION_PICK,  MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, Constants.GALLERY_OPTION_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CAMERA_OPTION_CODE){
                data?.extras?.let {
                    val imageBitmap = data.extras!!.get("data") as Bitmap
                    binding.editProfileImage.load(imageBitmap)
                }
            }
            if(requestCode == GALLERY_OPTION_CODE){
                data?.let {
                val imageBitmap = data.data
                binding.editProfileImage.load(imageBitmap)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}