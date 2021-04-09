package com.maxrzhe.contactsapp.screens

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.maxrzhe.contactsapp.databinding.FragmentContactDetailBinding
import com.maxrzhe.contactsapp.model.Contact
import com.maxrzhe.contactsapp.viewmodel.ContactViewModelFactory
import com.maxrzhe.contactsapp.viewmodel.SharedViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class ContactDetailFragment : Fragment() {
    private var _binding: FragmentContactDetailBinding? = null
    private val binding get() = _binding

    private var contact: Contact? = null
    private var contactId: Long = -1
    private var imageUri: String? = null

    private val onSaveContactListener: OnSaveContactListener?
        get() = (context as? OnSaveContactListener)

    private val onTakeImageListener: OnTakeImageListener?
        get() = (context as? OnTakeImageListener)

    private val sharedViewModel: SharedViewModel by activityViewModels {
        ContactViewModelFactory(
            requireActivity().application
        )
    }

    companion object {
        private const val IMAGE_DIRECTORY = "imageDir"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactDetailBinding.inflate(inflater, container, false)
        binding?.let {
            it.viewModel = sharedViewModel
            it.lifecycleOwner = requireActivity()
            it.btnDetailsAdd.setOnClickListener(saveContact())
            it.tvAddImage.setOnClickListener { onTakeImageListener?.onTakeImage() }
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.selectedContact.observe(viewLifecycleOwner, { selectedContact ->
            this.contact = selectedContact
            contactId = contact?.id ?: -1
            imageUri = contact?.image
        })
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

                if (contactId < 0) {
                    sharedViewModel.add(contact)
                } else {
                    sharedViewModel.update(contact)
                }
                onSaveContactListener?.onSave()
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
                !Patterns.PHONE.matcher(it.etPhone.text.toString()).matches() -> {
                    Toast.makeText(
                        requireContext(),
                        "Please enter correct phone number",
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
                !Patterns.EMAIL_ADDRESS.matcher(it.etEmail.text.toString()).matches() -> {
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

    fun setupImage(contentUri: Uri) {
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

        val contactImage = contact?.image?.split("/")?.last()
        val existingImage = dir.listFiles()?.firstOrNull {
            it.name.endsWith(contactImage.toString())
        }
        if (existingImage != null) {
            requireContext().deleteFile(existingImage.name)
        }
        val imageName = "${UUID.randomUUID()}.jpg"
        val file = File(dir, imageName)
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

    interface OnSaveContactListener {
        fun onSave()
    }

    interface OnTakeImageListener {
        fun onTakeImage()
    }
}