package com.example.ixltask.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val phoneNo: String,
    val dob: String,
) {
    val isDummyUser get() = id == 0
    companion object {
        fun getEmptyUser(): UserEntity {
            return UserEntity(
                id = 0,
                firstName = "",
                lastName = "",
                gender = "",
                phoneNo = "",
                dob = ""
            )
        }
    }
}