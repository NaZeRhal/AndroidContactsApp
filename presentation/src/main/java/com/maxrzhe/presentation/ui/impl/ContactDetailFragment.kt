package com.maxrzhe.presentation.ui.impl

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.maxrzhe.presentation.databinding.FragmentContactDetailBinding
import com.maxrzhe.presentation.ui.base.BaseFragment
import com.maxrzhe.presentation.viewmodel.impl.ContactDetailViewModel
import com.maxrzhe.presentation.viewmodel.impl.SharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class ContactDetailFragment : BaseFragment<FragmentContactDetailBinding, ContactDetailViewModel>() {

    private var imageUri: String? = null

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override val viewModel: ContactDetailViewModel by viewModel()

    private val onSaveContactListener: OnSaveContactListener?
        get() = (context as? OnSaveContactListener)

    private val onTakeImageListener: OnTakeImageListener?
        get() = (context as? OnTakeImageListener)

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactDetailBinding =
        FragmentContactDetailBinding::inflate

    override fun bindView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
    }

    override fun initView() {
        sharedViewModel.contactId.observe(viewLifecycleOwner, {
            viewModel.setSelectedId(it)
            subscribeUi()
        })
        viewModel.savedMarker.observe(viewLifecycleOwner, {
            if (it) {
                viewModel.resetMarker()
                onSaveContactListener?.onSave()
            }
        })
        binding.tvAddImage.setOnClickListener { onTakeImageListener?.onTakeImage() }
    }

    private fun subscribeUi() {
        viewModel.errorMessage.observe(viewLifecycleOwner, { msg ->
            if (msg != null) {
                showErrorSnackBar(msg)
            }
        })
        viewModel.validationMessage.observe(viewLifecycleOwner, { msg ->
            if (msg != null) {
                showValidationToast(msg)
            }
        })
    }

    private fun showErrorSnackBar(msg: String) {
        view?.let {
            Snackbar.make(it, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
            }.show()
        }
    }

    private fun showValidationToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    fun setupImage(contentUri: Uri) {
        try {
            val selectedImage = getCapturedImage(contentUri)
            imageUri = saveImageToExternalStorage(selectedImage).toString()
            imageUri?.let {
                viewModel.manageImageUri(it)
            }
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

    private fun saveImageToExternalStorage(selectedImage: Bitmap): Uri {
        val filePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.getStorageDirectory()
        } else {
            Environment.getExternalStorageDirectory()
        }
        val dir = File("${filePath.absolutePath}/$IMAGE_DIRECTORY")
        dir.mkdir()
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

    companion object {
        private const val IMAGE_DIRECTORY = "imageDir"
    }
}