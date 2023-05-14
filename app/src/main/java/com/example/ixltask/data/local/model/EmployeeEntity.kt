package com.example.ixltask.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: Int,
    val employeeNo: String,
    val employeeName: String,
    val designation: String,
    val accountType: String,
    val workExp: String,
) {
    val isDummy get() = userId == -1

    companion object {
        fun getEmptyClass(): EmployeeEntity {
            return EmployeeEntity(
                userId = -1,
                employeeName = "",
                employeeNo = "",
                designation = "",
                accountType = "",
                workExp = "",
            )
        }
    }
}