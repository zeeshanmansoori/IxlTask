package com.example.ixltask.ui.bankDetails

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import coil.load
import com.example.ixltask.R
import com.example.ixltask.databinding.FragmentBankDetailsBinding
import com.example.ixltask.ui.MainViewModel
import com.example.ixltask.utils.DataUtils
import com.example.ixltask.utils.base.BaseFragment
import com.example.ixltask.utils.setEditTextListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class BankDetailsFragment : BaseFragment<FragmentBankDetailsBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBankDetailsBinding::inflate


    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var tempFileUri: Uri
    private var readPermissionGranted = false
    private var writePermissionGranted = false
    private var cameraPermissionGranted = false
    private var imageTaken = false

    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            readPermissionGranted =
                permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermissionGranted
            writePermissionGranted =
                permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: writePermissionGranted
            cameraPermissionGranted =
                permissions[Manifest.permission.CAMERA] ?: cameraPermissionGranted

            if (!readPermissionGranted) {
                showToast("Can't read files without permission.")
            }
            if (!writePermissionGranted) {
                showToast("write permission not granted.")
            }
        }


    private val takeImageLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            imageTaken = isSuccess
            if (isSuccess && this::tempFileUri.isInitialized) {
                binding.previewIv.load(tempFileUri)
            }

        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        init()
    }

    private fun init() {
        viewModel.selectedBankDetails.let { details ->


            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                DataUtils.bankList
            ).also {
                (binding.bankNameAutoCompleteTextView as? AutoCompleteTextView)?.setAdapter(it)
            }


            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                DataUtils.branchList
            ).also {
                (binding.branchNameAutoCompleteTextView as? AutoCompleteTextView)?.setAdapter(it)
            }


            if (!details.isDummy) {
                binding.bankNameAutoCompleteTextView.setText(details.bankName)
                binding.branchNameAutoCompleteTextView.setText(details.bankBranchName)
                binding.accountNumberEt.setText(details.accountNumber)
                binding.ifscCodeEt.setText(details.ifcCode)
                tempFileUri = details.imageUri.toUri()
                imageTaken = details.imageUri.isNotBlank()
                binding.previewIv.load(tempFileUri)
            }

        }


    }

    private fun setListeners() = binding.run {
        selectImageBtn.setOnClickListener {
            updateOrRequestPermissions()
            if (readPermissionGranted && writePermissionGranted && cameraPermissionGranted && this@BankDetailsFragment::tempFileUri.isInitialized) {
                viewLifecycleOwner.lifecycleScope.launchWhenStarted {

                    createTempFile(requireContext())?.let { file ->
                        tempFileUri = FileProvider.getUriForFile(
                            requireContext(),
                            "com.example.ixltask.provider",
                            file
                        )
                        takeImageLauncher.launch(tempFileUri)
                    }

                }

            }

        }

        proceedBtn.setOnClickListener {
            val inputInvalid = isInputInvalid()
            if (inputInvalid) return@setOnClickListener
            val imageUrl =
                if (this@BankDetailsFragment::tempFileUri.isInitialized) tempFileUri.toString() else ""
            viewModel.saveDetails(
                binding.bankNameAutoCompleteTextView.text.toString(),
                binding.branchNameAutoCompleteTextView.text.toString(),
                imageUrl,
            )
            findNavControllerSafely()?.popBackStack(R.id.homeFragment, inclusive = false)
        }

        accountNumberEt.setEditTextListener(accountNumberTl, viewModel::onAccountNumberChanged)
        ifscCodeEt.setEditTextListener(ifscCodeTl, viewModel::onIfscCodeChanged)
    }


    private fun isInputInvalid(): Boolean = binding.run {

        val bankName = bankNameAutoCompleteTextView.text?.toString()
        if (bankName.isNullOrBlank() || bankNameAutoCompleteTextView.text.isNullOrBlank()) {
            showToast("Please Select Bank")
            return true
        }


        val branchName = branchNameAutoCompleteTextView.text?.toString()
        if (branchName.isNullOrBlank() || branchNameAutoCompleteTextView.text.isNullOrBlank()) {
            showToast("Please Select Branch")
            return true
        }


        accountNumberEt.requestFocus()
        if (accountNumberTl.error != null || accountNumberEt.text.isNullOrBlank()) {
            // to trigger the editTextListener
            accountNumberEt.setText("")
            return true
        }


        ifscCodeEt.requestFocus()
        if (ifscCodeTl.error != null || ifscCodeEt.text.isNullOrBlank()) {
            // to trigger the editTextListener
            ifscCodeEt.setText("")
            return true
        }

        if (!imageTaken) {
            showToast("Please capture Passbook Image")
            return true
        }

        return false
    }

    private fun updateOrRequestPermissions() {
        val hasReadPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val hasWritePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        val hasCameraPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        readPermissionGranted = hasReadPermission
        writePermissionGranted = hasWritePermission || minSdk29
        cameraPermissionGranted = hasCameraPermission

        val permissionsToRequest = mutableListOf<String>()
        if (!writePermissionGranted) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!readPermissionGranted) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!cameraPermissionGranted) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        if (permissionsToRequest.isNotEmpty()) {
            permissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    private suspend fun createTempFile(context: Context): File? = withContext(Dispatchers.IO) {
        return@withContext try {
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            File.createTempFile("temp_image", ".jpg", storageDir)
        } catch (e: Exception) {
            showToast("Cant create file exception ${e.message}")
            null
        }
    }
}


