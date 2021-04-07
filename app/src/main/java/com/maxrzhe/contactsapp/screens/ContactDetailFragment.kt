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

    private var contactId: Int = -1
    private var name: String = ""
    private var email: String = ""
    private var phone: String = ""
    private var imageUri: String = ""

    private val onSaveContactListener: OnSaveContactListener?
        get() = (context as? OnSaveContactListener)

    private val onTakeImageListener: OnTakeImageListener?
        get() = (context as? OnTakeImageListener)

    companion object {
        private const val IMAGE_DIRECTORY = "imageDir"
        private const val CONTACT_TO_SAVE = "contact_to_save"

        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val PHONE = "phone"
        private const val IMAGE = "image"

        fun newInstance(contact: Contact?) = ContactDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(CONTACT_TO_SAVE, contact)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            name = savedInstanceState.getString(NAME, "")
            email = savedInstanceState.getString(EMAIL, "")
            phone = savedInstanceState.getString(PHONE, "")
        }

        arguments?.let {
            contact = it.getParcelable(CONTACT_TO_SAVE)
        }

        isNew = contact == null
        contactId = savedInstanceState?.getInt(ID)
            ?: if (isNew) Random.nextInt(until = Int.MAX_VALUE) else contact?.id ?: -1

        imageUri = if (savedInstanceState != null) {
            savedInstanceState.getString(IMAGE, "")
        } else {
            if (isNew) "" else contact?.image ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactDetailBinding.inflate(inflater, container, false)

        return binding?.let { initView(it, contact) }
    }

    private fun initView(binding: FragmentContactDetailBinding, contact: Contact?): View {
        with(binding) {
            etName.setText(contact?.name ?: name)
            etPhone.setText(contact?.phone ?: phone)
            etEmail.setText(contact?.email ?: email)

            if (!isNew) {
                tvAddImage.text = resources.getString(R.string.detail_tv_change_image_text)
                btnDetailsAdd.text =
                    resources.getString(R.string.detail_button_save_changes_text)
            }

            if (imageUri.isEmpty()) {
                ivAvatar.setImageResource(R.drawable.person_placeholder)
            } else {
                ivAvatar.setImageURI((Uri.parse(imageUri)))
            }

            btnDetailsAdd.setOnClickListener(saveContact())
            tvAddImage.setOnClickListener { onTakeImageListener?.onTakeImage() }
        }
        return binding.root
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
                putInt(ID, contactId)
                putString(NAME, it.etName.text.toString())
                putString(EMAIL, it.etEmail.text.toString())
                putString(PHONE, it.etPhone.text.toString())
            }
        }
    }

    interface OnSaveContactListener {
        fun onSave(contact: Contact)
    }

    interface OnTakeImageListener {
        fun onTakeImage()
    }
}