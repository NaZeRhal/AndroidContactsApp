package com.maxrzhe.contactsapp.screens

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.maxrzhe.contactsapp.R
import com.maxrzhe.contactsapp.databinding.FragmentContactDetailBinding
import com.maxrzhe.contactsapp.model.Contact
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.random.Random

class ContactDetailFragment : Fragment() {
    private var _binding: FragmentContactDetailBinding? = null
    private val binding get() = _binding

    private var contact: Contact? = null
    private var isNew: Boolean = false
    private var isLandscape: Boolean = false

    private var contactId: Int = -1
    private var name: String = ""
    private var email: String = ""
    private var phone: String = ""
    private var imageUri: String = ""

    private var onSaveContactListener: OnSaveContactListener? = null

    private val imageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    val contentUri = it.data as Uri
                    try {
                        val selectedImage = getCapturedImage(contentUri)
                        imageUri = saveImageToInternalStorage(selectedImage).toString()
                        binding?.ivAvatar?.setImageBitmap(selectedImage)
                    } catch (e: IOException) {
                        Toast.makeText(
                            requireContext(),
                            "Failed to load the Image from Gallery!",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
        }

    private val permissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val isAllowed = permissions.entries.all { it.value != false }
            if (isAllowed) {
                choosePhotoFromGallery()

            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSaveContactListener) {
            onSaveContactListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        onSaveContactListener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.apply {
            name = getString(NAME, "")
            email = getString(EMAIL, "")
            phone = getString(PHONE, "")
        }

        arguments?.let {
            contact = it.getParcelable(CONTACT)
            isNew = it.getBoolean(IS_NEW_CONTACT, false)
            isLandscape = it.getBoolean(ContactsListActivity.IS_LANDSCAPE, false)
        }
        contactId = savedInstanceState?.getInt(CONTACT_ID)
            ?: if (isNew) Random.nextInt(until = Int.MAX_VALUE) else contact?.id ?: -1

        imageUri = if (savedInstanceState != null) {
            savedInstanceState.getString(IMAGE, resources.getString(R.string.placeholder_uri))
        } else {
            if (isNew) resources.getString(R.string.placeholder_uri) else contact?.image
                ?: resources.getString(R.string.placeholder_uri)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactDetailBinding.inflate(inflater, container, false)

        return binding?.let {
            val view = it.root

            with(it) {
                if (!isNew) {
                    tvAddImage.text = resources.getString(R.string.detail_tv_change_image_text)
                    btnDetailsAdd.text =
                        resources.getString(R.string.detail_button_save_changes_text)
                    if (contact != null) {
                        contact?.let { cont ->
                            etName.setText(cont.name)
                            etPhone.setText(cont.phone)
                            etEmail.setText(cont.email)
                        }
                    } else {
                        etName.setText(name)
                        etPhone.setText(phone)
                        etEmail.setText(email)
                    }
                }
                binding?.ivAvatar?.setImageURI((Uri.parse(imageUri)))
                btnDetailsAdd.setOnClickListener(saveContact())
                tvAddImage.setOnClickListener(
                    checkForStoragePermission()
                )
            }
            view
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (!isLandscape) {
            inflater.inflate(R.menu.main_menu, menu)
            val searchIcon = menu.findItem(R.id.menu_item_search)
            searchIcon.isVisible = false
            (context as? ContactsListActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            super.onCreateOptionsMenu(menu, inflater)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveContact() = View.OnClickListener {
        if (validateInput()) {
            binding?.let {
                val contact = Contact(
                    id = contactId,
                    name = it.etName.text.toString(),
                    phone = it.etPhone.text.toString(),
                    email = it.etEmail.text.toString(),
                    image = imageUri
                )
                if (contactId >= 0) {
                    onSaveContactListener?.onSave(contact)
                }

                if (!isLandscape) {
                    parentFragmentManager.popBackStackImmediate()
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        return binding?.let {
            when {
                it.etName.text.isNullOrEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Please enter a name",
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
                !Patterns.PHONE.matcher(it.etPhone.text).matches() -> {
                    Toast.makeText(
                        requireContext(),
                        "Please enter correct phone number",
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
                !Patterns.EMAIL_ADDRESS.matcher(it.etEmail.text).matches() -> {
                    Toast.makeText(
                        requireContext(),
                        "Please enter correct email",
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
                else -> true
            }
        } ?: true
    }

    private fun checkForStoragePermission() =
        View.OnClickListener {
            val storagePermissions = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        storagePermissions[0]
                    ) + ContextCompat.checkSelfPermission(
                        requireContext(),
                        storagePermissions[1]
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (shouldShowRequestPermissionRationale(storagePermissions[0]) ||
                        shouldShowRequestPermissionRationale(storagePermissions[1])
                    ) {
                        showDialog(storagePermissions)
                    } else {
                        permissionRequestLauncher.launch(storagePermissions)
                    }
                } else {
                    choosePhotoFromGallery()
                }
            }
        }

    private fun showDialog(permissions: Array<String>) {
        AlertDialog.Builder(requireContext()).apply {
            setMessage("Permission to access your STORAGE is required to use this app")
            setTitle("Permission required")
            setPositiveButton("OK") { _, _ ->
                permissionRequestLauncher.launch(permissions)
            }
        }
            .show()
    }


    private fun choosePhotoFromGallery() {
        val galleryIntent =
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        imageResultLauncher.launch(galleryIntent)
    }

    private fun getCapturedImage(contentUri: Uri): Bitmap {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
                val source =
                    ImageDecoder.createSource(requireActivity().contentResolver, contentUri)
                ImageDecoder.decodeBitmap(source)
            }
            else -> MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, contentUri)
        }
    }

    private fun saveImageToInternalStorage(selectedImage: Bitmap): Uri {
        val wrapper = ContextWrapper(requireContext())
        val dir = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)

        val existingImage = dir.listFiles()?.firstOrNull {
            it.name.endsWith("_${contactId}.jpg")
        }
        if (existingImage != null) {
            requireContext().deleteFile(existingImage.name)
        }

        val file = File(dir, "${UUID.randomUUID()}_${contactId}.jpg")
        try {
            val stream: OutputStream = FileOutputStream(file)
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 75, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            binding?.let {
                putString(IMAGE, imageUri)
                putInt(CONTACT_ID, contactId)
                binding?.let {
                    putString(NAME, it.etName.text.toString())
                    putString(EMAIL, it.etEmail.text.toString())
                    putString(PHONE, it.etPhone.text.toString())
                }
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
//        Log.i(ContactsListActivity.TAG, "onResume: detail")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.i(ContactsListActivity.TAG, "onPause: detail")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.i(ContactsListActivity.TAG, "onStop: detail")
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        Log.i(ContactsListActivity.TAG, "onDestroyView: detail")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.i(ContactsListActivity.TAG, "onDestroy: detail")
//    }

    companion object {
        private const val IMAGE_DIRECTORY = "imageDir"
        const val CONTACT = "contact"
        const val CONTACT_TO_SAVE = "contact_to_save"
        const val IS_NEW_CONTACT = "is_new_contact"
        const val DETAILS_FRAGMENT_TAG_KEY = "DETAILS_FRAGMENT_TAG_KEY"

        const val CONTACT_ID = "contact_id"
        const val NAME = "name"
        const val EMAIL = "email"
        const val PHONE = "phone"
        const val IMAGE = "image"
    }

    interface OnSaveContactListener {
        fun onSave(contact: Contact)
    }
}