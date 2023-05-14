package com.example.ixltask.ui.userDetails

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.example.ixltask.databinding.FragmentUserDetailsBinding
import com.example.ixltask.ui.MainViewModel
import com.example.ixltask.utils.base.BaseFragment
import com.example.ixltask.utils.setEditTextListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UserDetailsFragment : BaseFragment<FragmentUserDetailsBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUserDetailsBinding::inflate

    private val viewModel by activityViewModels<MainViewModel>()
    private val myCalendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListeners()

    }

    private fun init() {
        if (!viewModel.selectedUser.isDummyUser) {
            viewModel.selectedUser.let { user ->
                binding.firstNameEditText.setText(user.firstName)
                binding.lastNameEditText.setText(user.lastName)
                binding.dateOfBirthEditText.setText(user.dob)
                binding.phoneNumberEditText.setText(user.phoneNo)
                binding.genderAutoCompleteTextView.setText(user.gender)
            }
        }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            listOf("Male", "Female", "Other")
        )
        (binding.genderAutoCompleteTextView as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private var onDateSetCallback =
        DatePickerDialog.OnDateSetListener { _, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            val myFormat = "MM/dd/yy"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            val date = dateFormat.format(myCalendar.time)
            binding.dateOfBirthEditText.setText(date)
            viewModel.onDobChanged(date)
        }

    private fun setListeners() = binding.run {

        proceedBtn.setOnClickListener {
            val isInputValid = isInputInvalid()
            if (isInputValid) return@setOnClickListener
            viewModel.onGenderChanged(genderAutoCompleteTextView.text.toString())
            findNavControllerSafely()?.navigate(UserDetailsFragmentDirections.actionUserDetailsFragmentToEmployeeDetailsFragment())
        }

        dateOfBirthEditText.setOnClickListener {

            val dpd = DatePickerDialog(
                requireContext(),
                onDateSetCallback,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH),
            )
            dpd.show()
        }
        firstNameEditText.setEditTextListener(firstNameInputLayout, viewModel::onFirstNameChanged)
        lastNameEditText.setEditTextListener(lastNameInputLayout, viewModel::onLastNameChanged)
        phoneNumberEditText.setEditTextListener(
            phoneNumberInputLayout,
            viewModel::onPhoneNumberChanged
        )
    }

    private fun isInputInvalid(): Boolean = binding.run {

        firstNameEditText.requestFocus()
        if (firstNameInputLayout.error != null || firstNameEditText.text.isNullOrBlank())
            return true


        lastNameEditText.requestFocus()
        if (lastNameInputLayout.error != null || lastNameEditText.text.isNullOrBlank())
            return true


        phoneNumberEditText.requestFocus()
        if (phoneNumberInputLayout.error != null || phoneNumberEditText.text.isNullOrBlank())
            return true

        val dob = dateOfBirthEditText.text?.toString()
        if (dob.isNullOrBlank() || dateOfBirthEditText.text.isNullOrBlank()) {
            showToast("please select DOB")
            return true
        }

        val gender = genderAutoCompleteTextView.text?.toString()
        if (gender.isNullOrBlank() || genderAutoCompleteTextView.text.isNullOrBlank()) {
            showToast("please select gender")
            return true
        }

        return false
    }

}
