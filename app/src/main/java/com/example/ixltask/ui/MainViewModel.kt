package com.example.ixltask.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ixltask.data.local.IxlDatabase
import com.example.ixltask.data.local.model.BankDetailsEntity
import com.example.ixltask.data.local.model.EmployeeEntity
import com.example.ixltask.data.local.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val database: IxlDatabase) : ViewModel() {

    val allUsers = database.getUserDao().getUsersAsFlow()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    private var _selectedUser: UserEntity? = null
    private var _selectedEmployeeDetails: EmployeeEntity? = null
    private var _selectedBankDetails: BankDetailsEntity? = null

    val selectedUser get() = _selectedUser ?: UserEntity.getEmptyUser()
    val selectedEmployeeDetails get() = _selectedEmployeeDetails ?: EmployeeEntity.getEmptyClass()
    val selectedBankDetails get() = _selectedBankDetails ?: BankDetailsEntity.getEmptyClass()

    private fun setEmpDetails(userId: Int?) = viewModelScope.launch(Dispatchers.IO) {
        _selectedEmployeeDetails = if (userId == null) null else database.getEmployeeDao()
            .getEmployeeDetailsByUserId(userId)
    }

    private fun setBankDetails(userId: Int?) = viewModelScope.launch(Dispatchers.IO) {
        _selectedBankDetails = if (userId == null) null else database.getBankDetailsDao()
            .getBankDetailsByUserId(userId)
    }

    fun setSelectedUser(userEntity: UserEntity?) {
        _selectedUser = userEntity
        setEmpDetails(userEntity?.id)
        setBankDetails(userEntity?.id)
    }


    companion object {
        fun getViewModelFactory(database: IxlDatabase): MyViewModelFactory {
            return MyViewModelFactory(database)
        }
    }

    class MyViewModelFactory(private val database: IxlDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    fun onFirstNameChanged(name: String?) {
        _selectedUser = selectedUser.copy(firstName = name ?: "")
    }

    fun onLastNameChanged(lastName: String?) {
        _selectedUser = selectedUser.copy(lastName = lastName ?: "")
    }

    fun onPhoneNumberChanged(str: String?) {
        _selectedUser = selectedUser.copy(phoneNo = str ?: "")
    }

    fun onGenderChanged(gender: String) {
        _selectedUser = selectedUser.copy(gender = gender)
    }

    fun onDobChanged(date: String) {
        _selectedUser = selectedUser.copy(dob = date)
    }

    fun onEmplyeNumberChanged(str: String?) {
        _selectedEmployeeDetails = selectedEmployeeDetails.copy(employeeNo = str ?: "")
    }

    fun onEmplyeNameChanged(str: String?) {
        _selectedEmployeeDetails = selectedEmployeeDetails.copy(employeeName = str ?: "")
    }

    fun onDesignataionChanged(str: String?) {
        _selectedEmployeeDetails = selectedEmployeeDetails.copy(designation = str ?: "")
    }


    fun onAccountTypeChanged(str: String) {
        _selectedEmployeeDetails = selectedEmployeeDetails.copy(accountType = str )
    }

    fun onWorkExperienceChanged(string: String) {
        _selectedEmployeeDetails = selectedEmployeeDetails.copy(workExp = string )

    }


    fun onAccountNumberChanged(s: String?) {
        _selectedBankDetails = selectedBankDetails.copy(accountNumber = s ?: "")
    }

    fun onIfscCodeChanged(s: String?) {
        _selectedBankDetails = selectedBankDetails.copy(ifcCode = s ?: "")
    }


    fun saveDetails(bankName: String, branchName: String, imageUri: String) =
        viewModelScope.launch(Dispatchers.IO) {
            val id = database.getUserDao().insert(selectedUser)
            database.getEmployeeDao().insert(selectedEmployeeDetails.copy(userId = id.toInt()))
            database.getBankDetailsDao().insert(
                selectedBankDetails.copy(
                    userId = id.toInt(),
                    bankName = bankName,
                    bankBranchName = branchName,
                    imageUri = imageUri
                )
            )
        }


}
