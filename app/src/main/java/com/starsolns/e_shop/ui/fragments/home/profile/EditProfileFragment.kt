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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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
import com.starsolns.e_shop.model.UserEntity
import com.starsolns.e_shop.model.Users
import com.starsolns.e_shop.ui.activities.HomeActivity
import com.starsolns.e_shop.util.Constants
import com.starsolns.e_shop.util.Constants.CAMERA_OPTION_CODE
import com.starsolns.e_shop.util.Constants.GALLERY_OPTION_CODE
import com.starsolns.e_shop.util.Constants.USERS
import com.starsolns.e_shop.util.ProgressButton
import com.starsolns.e_shop.viewmodel.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var toolbar: Toolbar

    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var dialog: ProgressButton
    private lateinit var buttonView: View

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: String

    private  var profileImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)
        (context as HomeActivity).setUpSupportCustomActionBar(binding.editProfileToolBar)

        auth = Firebase.auth
        firebaseUser = auth.currentUser!!.uid

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

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

        lifecycleScope.launch(Dispatchers.IO) {
            db.collection(Constants.USERS)
                .document(firebaseUser)
                .get()
                .addOnSuccessListener { result ->
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

                    if(user.profilePicture!!.isNotEmpty()){
                        binding.editProfileImage.load(user.profilePicture)
                    }

                   sharedViewModel.getUserProfile(firebaseUser).observe(viewLifecycleOwner){ profile->
                       if(profile.isNotEmpty()){
                           binding.editEmailId.setText(profile[0].email)
                           binding.editFirstName.setText(profile[0].firstName)
                           binding.editLastName.setText(profile[0].lastName)
                           binding.editDob.setText(profile[0].dob)
                           binding.editPhone.setText(profile[0].phone)

                       }else {
                           binding.editEmailId.setText(user.email)
                           binding.editFirstName.setText(user.firstName)
                           binding.editLastName.setText(user.lastName)
                           binding.editDob.setText(user.dob)
                           binding.editPhone.setText(user.phone)
                       }
                   }

                    binding.saveProfileUpdate.loginRegisterAccessButton.setOnClickListener {
                        updateUserProfileDetails()
                    }

                }
        }

    }

    private fun updateUserProfileDetails(){
        dialog.showProgressBar()
        val storage= Firebase.storage
        val ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(requireActivity().contentResolver.getType(profileImageUri!!))
        val profileImageRef = storage.getReference("Images/Profile Images").child(firebaseUser + "_" + System.currentTimeMillis() + "." + ext)
        if(profileImageUri != null){
            if(validateEntries()){
                profileImageRef.putFile(profileImageUri!!).addOnSuccessListener { taskSnapShot->
                    taskSnapShot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri->
                        saveImageAndProfileUpdates(uri.toString())
                    }
                }
            }else{
                dialog.dismissResetProgressBarError()
                Toast.makeText(requireContext(), resources.getString(R.string.fill_profile_details), Toast.LENGTH_SHORT).show()
            }
        }else {
            if(validateEntries()){
            saveProfileUpdates()
            }
        }

    }

    private fun validateEntries(): Boolean{
        val firstName = binding.editFirstName.text.toString().trim()
        val lastName = binding.editLastName.text.toString().trim()
        val phone = binding.editPhone.text.toString().trim()

        if(firstName.isEmpty()){
            binding.txtInputEditFirstName.helperText = "First Name Required"
            return false
        }else{
            binding.txtInputEditFirstName.helperText = null
        }
        if(lastName.isEmpty()){
            binding.txtInputEditLastName.helperText = "Last Name Required"
            return false
        }else {
            binding.txtInputEditLastName.helperText = null
        }
        if (phone.isEmpty()){
            binding.txtInputEditPhone.helperText = "Phone Required"
            return false
        } else {
            binding.txtInputEditPhone.helperText = null
        }
        if(phone.length != 10){
            binding.txtInputEditPhone.helperText = "Phone must be 10 Digits"
            return true
        }else {
            binding.txtInputEditPhone.helperText = null
        }
        return true
    }

    private fun saveProfileUpdates(){
        val db = Firebase.firestore
        val gender = if(binding.genderMale.isChecked){
            "male"
        }else {
            "female"
        }

        val userInfo = HashMap<String, Any>()
        userInfo["firstName"] = binding.editFirstName.text.toString().trim()
        userInfo["lastName"] = binding.editLastName.text.toString().trim()
        userInfo["dob"] = binding.editDob.text.toString().trim()
        userInfo["phone"] = binding.editPhone.text.toString().trim()
        userInfo["gender"] = gender


        db.collection(USERS)
            .document(firebaseUser)
            .update(userInfo)
            .addOnSuccessListener { info->
                dialog.dismissProfileUpdateProgressBar()
                updateUserProfileCache()
                findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
            }
            .addOnFailureListener { exception->
                dialog.dismissResetProgressBarError()
                Toast.makeText(requireContext(), exception.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveImageAndProfileUpdates(url: String) {
        val db = Firebase.firestore
        val gender = if(binding.genderMale.isChecked){
            "male"
        }else {
            "female"
        }

        val userInfo = HashMap<String, Any>()
        userInfo["firstName"] = binding.editFirstName.text.toString().trim()
        userInfo["lastName"] = binding.editLastName.text.toString().trim()
        userInfo["dob"] = binding.editDob.text.toString().trim()
        userInfo["phone"] = binding.editPhone.text.toString().trim()
        userInfo["gender"] = gender
        userInfo["profilePicture"] = url


        db.collection(USERS)
            .document(firebaseUser)
            .update(userInfo)
            .addOnSuccessListener { info->
                dialog.dismissProfileUpdateProgressBar()
                updateUserProfileCache()
                findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
            }
            .addOnFailureListener { exception->
                dialog.dismissResetProgressBarError()
                Toast.makeText(requireContext(), exception.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserProfileCache() {
        val db = Firebase.firestore
        db.collection(USERS)
            .document(firebaseUser)
            .get()
            .addOnSuccessListener { data->
                val userInfo = data.toObject<Users>()

                val userUpdate = UserEntity(
                    userInfo!!.firstName,
                    userInfo.lastName,
                    userInfo.email,
                    userInfo.phone,
                    userInfo.dob,
                    userInfo.gender,
                    userInfo.id)

                sharedViewModel.updateUserProfile(userUpdate)
            }
    }

    private fun showBottomSheetOptions() {
        val bottomSheetDialog =
            BottomSheetDialog(requireActivity(), R.style.Theme_BottomSheetCustomStyleTheme)
        val customBinding: ImageProfileBottomSheetBinding =
            ImageProfileBottomSheetBinding.inflate(layoutInflater)
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
                    if (permReport.areAllPermissionsGranted()) {
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

    private fun loadImageFromGallery() {
        Dexter.withContext(requireContext()).withPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
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
            if (requestCode == CAMERA_OPTION_CODE) {
                data?.extras?.let {
                   val profileImage = data.extras!!.get("data") as Bitmap
                    binding.editProfileImage.load(profileImage)
                }
            }
            if (requestCode == GALLERY_OPTION_CODE) {
                data?.let {
                   profileImageUri = data.data!!
                    binding.editProfileImage.load(profileImageUri)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}