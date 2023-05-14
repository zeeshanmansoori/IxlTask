package com.example.ixltask.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class BankDetailsEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: Int,
    val bankName: String,
    val bankBranchName: String,
    val accountNumber: String,
    val ifcCode: String,
    val imageUri:String,
) {
    val isDummy get() = userId == -1

    companion object {
        fun getEmptyClass(): BankDetailsEntity {
            return BankDetailsEntity(
                userId = -1,
                bankName = "",
                accountNumber = "",
                bankBranchName = "",
                ifcCode = "",
                imageUri = "",
            )
        }
    }
}