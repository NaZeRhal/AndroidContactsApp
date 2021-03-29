package com.maxrzhe.contactsapp.screens

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.maxrzhe.contactsapp.databinding.ActivityDetailAddBinding
import com.maxrzhe.contactsapp.model.Contact
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.random.Random

class AddDetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailAddBinding

    private var contactId: Int = -1
    private var imageUri: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAddBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.tbDetails)
        val actionBar = supportActionBar
        title = null

        actionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbDetails.setNavigationOnClickListener {
            onBackPressed()
        }

        contactId = Random.nextInt(until = Int.MAX_VALUE)
        binding.btnDetailsAdd.setOnClickListener(this@AddDetailActivity)
        binding.tvAddImage.setOnClickListener(this@AddDetailActivity)
    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v) {
                binding.btnDetailsAdd -> saveContact()
                binding.tvAddImage -> choosePhotoFromGallery()
            }
        }
    }

    private fun saveContact() {
        if (validateInput()) {
            val contact = Contact(
                id = contactId,
                name = binding.etName.text.toString(),
                phone = binding.etPhone.text.toString(),
                email = binding.etEmail.text.toString(),
                image = imageUri
            )

            saveToSharedPreferences(contact)

            val returnedIntent = Intent()
            returnedIntent.putExtra(NEW_CONTACT, contact)
            setResult(Activity.RESULT_OK, returnedIntent)
            finish()
        }
    }

    private fun validateInput(): Boolean {
        return with(binding) {
            when {
                etName.text.isNullOrEmpty() -> {
                    Toast.makeText(
                        this@AddDetailActivity,
                        "Please enter a name",
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
                !Patterns.PHONE.matcher(etPhone.text).matches() -> {
                    Toast.makeText(
                        this@AddDetailActivity,
                        "Please enter correct phone number",
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
                !Patterns.EMAIL_ADDRESS.matcher(etEmail.text).matches() -> {
                    Toast.makeText(
                        this@AddDetailActivity,
                        "Please enter correct email",
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
                else -> true
            }
        }
    }

    private fun choosePhotoFromGallery() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
            .withListener(object : MultiplePermissionsListener {

                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            val galleryIntent =
                                Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                )
                            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
                        }
                        if (report.isAnyPermissionPermanentlyDenied) {
                            showSettingsDialog()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    request: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(this)
            .setMessage(
                "It looks like you have turned off permission required for this feature. " +
                        "It can be enabled under the Application Settings"
            )
            .setPositiveButton("GO TO SETTINGS") { _, _ ->
                try {
                    openSettings()
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                data?.let {
                    val contentUri = data.data as Uri
                    try {
                        val selectedImage = getCapturedImage(contentUri)
                        imageUri = saveImageToInternalStorage(selectedImage).toString()
                        binding.ivAvatar.setImageBitmap(selectedImage)
                    } catch (e: IOException) {
                        Toast.makeText(
                            this,
                            "Failed to load the Image from Gallery!",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
        }
    }

    private fun getCapturedImage(contentUri: Uri): Bitmap {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
                val source = ImageDecoder.createSource(this.contentResolver, contentUri)
                ImageDecoder.decodeBitmap(source)
            }
            else -> MediaStore.Images.Media.getBitmap(this.contentResolver, contentUri)
        }
    }

    private fun saveImageToInternalStorage(selectedImage: Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)
        val dir = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        val file = File(dir, "${contactId}.jpg")
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

    private fun saveToSharedPreferences(contact: Contact) {
        val sp = this.getSharedPreferences(ContactsListActivity.SHARED_STORAGE_NAME, MODE_PRIVATE)
        val savedJsonContacts = sp.getString(ContactsListActivity.CONTACT_LIST, null)
        val type = object : TypeToken<ArrayList<Contact>>() {}.type
        val savedContacts =
            Gson().fromJson<ArrayList<Contact>>(savedJsonContacts, type) ?: ArrayList()

        savedContacts.add(contact)
        val jsonMap = Gson().toJson(savedContacts)
        sp.edit {
            putString(ContactsListActivity.CONTACT_LIST, jsonMap)
            apply()
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 111
        const val NEW_CONTACT = "changed_contact"
        const val IMAGE_DIRECTORY = "imageDir"
    }


}