package com.example.ixltask.ui.employeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.example.ixltask.databinding.FragmentEmployeeDetailsBinding
import com.example.ixltask.ui.MainViewModel
import com.example.ixltask.utils.DataUtils
import com.example.ixltask.utils.base.BaseFragment
import com.example.ixltask.utils.setEditTextListener

class EmployeeDetailsFragment : BaseFragment<FragmentEmployeeDetailsBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentEmployeeDetailsBinding::inflate

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListeners()

    }

    private fun init() {

        viewModel.selectedEmployeeDetails.let { details ->

            if (!details.isDummy) {
                binding.employeeNoEt.setText(details.employeeNo)
                binding.employeeNameEt.setText(details.employeeName)
                binding.designationEt.setText(details.designation)
                binding.accountTypeAutoCompleteTextView.setText(details.accountType)
                binding.workExpAutoCompleteTextView.setText(details.workExp)

            }

        }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            DataUtils.accountTypes,
        )
        (binding.accountTypeAutoCompleteTextView as? AutoCompleteTextView)?.setAdapter(adapter)

        val workExpAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            DataUtils.expList
        )
        (binding.workExpAutoCompleteTextView as? AutoCompleteTextView)?.setAdapter(workExpAdapter)
    }

    private fun isInputInvalid(): Boolean = binding.run {

        employeeNoEt.requestFocus()
        if (employeeNoTl.error != null || employeeNoEt.text.isNullOrBlank()) {
            // to trigger the editTextListener
            employeeNoEt.setText("")
            return true
        }


        employeeNameEt.requestFocus()
        if (employeeNameTl.error != null || employeeNameEt.text.isNullOrBlank()) {
            // to trigger the editTextListener
            employeeNameEt.setText("")
            return true
        }


        designationEt.requestFocus()
        if (designationTl.error != null || designationEt.text.isNullOrBlank()) {
            // to trigger the editTextListener
            designationEt.setText("")
            return true
        }

        val accType = accountTypeAutoCompleteTextView.text?.toString()
        if (accType.isNullOrBlank() || accountTypeAutoCompleteTextView.text.isNullOrBlank()) {
            showToast("Please select account type")
            return true
        }

        val workExp = workExpAutoCompleteTextView.text?.toString()
        if (workExp.isNullOrBlank() || workExpAutoCompleteTextView.text.isNullOrBlank()) {
            showToast("Please select Work Experience")
            return true
        }

        return false
    }

    private fun setListeners() = binding.run {

        proceedBtn.setOnClickListener {
            val isInputValid = isInputInvalid()
            if (isInputValid) return@setOnClickListener
             viewModel.onAccountTypeChanged(accountTypeAutoCompleteTextView.text.toString())
            viewModel.onWorkExperienceChanged(workExpAutoCompleteTextView.text.toString())
            findNavControllerSafely()?.navigate(EmployeeDetailsFragmentDirections.actionEmployeeDetailsFragmentToBankDetailsFragment())
        }

        employeeNoEt.setEditTextListener(employeeNoTl, viewModel::onEmplyeNumberChanged)
        employeeNameEt.setEditTextListener(employeeNameTl, viewModel::onEmplyeNameChanged)
        designationEt.setEditTextListener(designationTl, viewModel::onDesignataionChanged)

    }
}
